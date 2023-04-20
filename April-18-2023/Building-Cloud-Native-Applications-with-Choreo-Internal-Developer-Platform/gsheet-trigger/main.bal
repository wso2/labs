import ballerinax/mysql;
import ballerinax/mysql.driver as _;
import ballerinax/openai.images;
import ballerinax/openai.text;
import ballerina/io;
import ballerina/http;
import ballerina/sql;

configurable string openAIToken = ?;
configurable string priceEndpoint = ?;

http:Client priceClient = checkpanic new (priceEndpoint);

type PriceResMsg record {
    int ProductPrice;
};

type GsheetEvent record {
    (int|string)[][] newValues;
};

type GsheetProduct record {
    string id;
    string name;
    string desc;
    string 'type;
};

text:Client textEp = check new (config = {
    auth: {
        token: openAIToken
    }
});

images:Client imagesEp = check new (config = {
    auth: {
        token: openAIToken
    }
});

configurable string dbHost = ?;
configurable string dbUser = ?;
configurable string dbPassword = ?;
configurable string dbDatabase = ?;
configurable int dbPort = ?;
mysql:Client mysqlEp = check new (host = dbHost, user = dbUser, password = dbPassword, database = dbDatabase, port = dbPort);

service / on new http:Listener(9090) {
    resource function post .(http:Caller caller, http:Request req) returns error? {
        // 1. Get trigger event from Gsheet
        json gsheetPayload = check req.getJsonPayload();
        // io:println("Got payload: ", gsheetPayload);
        GsheetEvent gsheetEvent = check gsheetPayload.cloneWithType(GsheetEvent);

        // io:println("Got event: ", gsheetEvent);

        io:println("prices URL: ", priceEndpoint);

        // 2. Get item price from golang service
        http:Response priceRes = check priceClient->/.get();
        json priceR = check priceRes.getJsonPayload();
        PriceResMsg price = check priceR.cloneWithType(PriceResMsg);
        GsheetProduct product = convertToGsheetProduct(gsheetEvent.newValues);

        // io:println("Got product: ", product.toJsonString());

        // 3. Call OpenAI text gen - generate product description
        text:CreateCompletionRequest completionRequest = {
            prompt: "Generate a product description in 40 words for: " + product.desc,
            max_tokens: 50,
            model: "text-davinci-002"
        };
        text:CreateCompletionResponse completionResponse = check textEp->/completions.post(completionRequest);
        string description = check completionResponse.choices[0].text.ensureType();

        io:println("#### Got description: ", description);

        // 4. Call OpenAI image gen - generate product image
        images:CreateImageRequest imageRequest = {
            prompt: "Generate an image for: " + description
        };
        images:ImagesResponse imageResponse = check imagesEp->/images/generations.post(imageRequest);
        string imageUrl = check imageResponse.data[0].url.ensureType();

        io:println("#### Got image url: ", imageUrl);

        // 5. Save data to DB
        sql:ParameterizedQuery insertQuery = `INSERT INTO product_details (id, name, description, price, imageUrl) VALUES (${product.id}, ${product.name}, ${description}, ${price.ProductPrice}, ${imageUrl})`;
        sql:ExecutionResult result = check mysqlEp->execute(insertQuery);
        io:println("#### Inserted row count: ", result?.affectedRowCount);
    }
}

// Function for converting newValues to GsheetProduct
function convertToGsheetProduct((int|string)[][] newValues) returns GsheetProduct {
    GsheetProduct product = {
        id: newValues[0][0].toString(),
        name: newValues[0][1].toString(),
        desc: newValues[0][2].toString(),
        'type: newValues[0][3].toString()
    };
    return product;
}
