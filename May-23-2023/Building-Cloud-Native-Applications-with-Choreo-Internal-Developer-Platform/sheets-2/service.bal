import ballerinax/mysql;
import ballerinax/mysql.driver as _;
import ballerinax/openai.images;
import ballerinax/openai.text;
import ballerina/http;
import ballerina/io;
import ballerina/sql;

configurable string priceEndpoint = ?;
http:Client priceClient = check new (priceEndpoint);

type SheetsEvent record {
    (int|string|float)[][] newValues;
};

type PriceMsg record {
    int ProductPrice;
};

type ProductInfo record {
    string name;
    string short_description;
    string description;
    string 'type;
};

service / on new http:Listener(9090) {

    resource function post .(http:Caller caller, http:Request req) returns error? {
        // Send a response back to the caller.
        // io:println(req.getTextPayload());

        json sheetsJson = check req.getJsonPayload();
        SheetsEvent sheetsEvent = check sheetsJson.cloneWithType(SheetsEvent);
        io:println(sheetsEvent.newValues);

        ProductInfo productInfo = convertSheetEventToProduct(sheetsEvent);

        json priceJson = check priceClient->/.get();
        PriceMsg priceMsg = check priceJson.cloneWithType(PriceMsg);
        io:println(priceMsg.ProductPrice);

        text:CreateCompletionRequest completionRequest = {
            prompt: "Generate a product description in 150 words for " + productInfo.name + " with the description " + productInfo.description,
            max_tokens: 150,
            model: "text-davinci-003"
        };
        text:CreateCompletionResponse completionRes = check textEp->/completions.post(completionRequest);
        io:println(completionRes.choices[0].text);
        string description = completionRes.choices[0].text.toString();

        images:CreateImageRequest imageRequest = {
            prompt: "Generate an image for " + productInfo.name + " with the description " + description
        };
        images:ImagesResponse imageRes = check imagesEp->/images/generations.post(imageRequest);
        io:println("--> Image URL: ", imageRes.data[0].url);
        string imageURL = imageRes.data[0].url.toString();

        sql:ParameterizedQuery insertQuery = `insert into product_info (name, description, price, image_url) values (${productInfo.name}, ${description}, ${priceMsg.ProductPrice}, ${imageURL})`;
        sql:ExecutionResult executionResult = check mysqlEp->execute(insertQuery);
        io:println(executionResult.lastInsertId);
    }
}

text:Client textEp = check new (config = {
    auth: {
        token: openAIToken
    }
});

configurable string openAIToken = ?;

function convertSheetEventToProduct(SheetsEvent sheetsEvent) returns ProductInfo {
    ProductInfo productInfo = {
        name: sheetsEvent.newValues[0][0].toString(),
        short_description: sheetsEvent.newValues[0][1].toString(),
        description: sheetsEvent.newValues[0][2].toString(),
        'type: sheetsEvent.newValues[0][3].toString()
    };
    return productInfo;
}

images:Client imagesEp = check new (config = {
    auth: {
        token: openAIToken
    }
});
mysql:Client mysqlEp = check new (host = dbHost, user = dbUser, password = dbPassword, database = dbName, port = dbPort);
configurable string dbHost = ?;
configurable string dbUser = ?;
configurable string dbName = ?;
configurable string dbPassword = ?;
configurable int dbPort = ?;

