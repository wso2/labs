import ballerinax/mysql;
import ballerinax/mysql.driver as _;
import ballerina/http;

type ProductInfo record {
    int id;
    string name;
    string description;
    decimal price;
    string image_url;
};

service / on new http:Listener(9090) {

    resource function get getProducts() returns ProductInfo[]|error? {
        // Send a response back to the caller.
        ProductInfo[] products = [];
        stream<ProductInfo, error?> resStream = mysqlEp->query(`SELECT * FROM product_info`);

        check from ProductInfo p in resStream 
        do  {
            products.push(p);
        };
        return products;
    }
}

mysql:Client mysqlEp = check new (host = dbHost, user = dbUser, password = dbPassword, database = dbName, port = dbPort);
configurable string dbHost = ?;
configurable string dbUser = ?;
configurable string dbName = ?;
configurable string dbPassword = ?;
configurable int dbPort = ?;