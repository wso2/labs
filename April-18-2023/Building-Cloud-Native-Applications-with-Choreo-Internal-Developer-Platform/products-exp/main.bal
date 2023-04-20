import ballerinax/mysql;
import ballerinax/mysql.driver as _;
import ballerina/http;

configurable int dbPort = ?;
configurable string dbName = ?;
configurable string dbPass = ?;
configurable string dbUser = ?;
configurable string dbHost = ?;

mysql:Client mysqlEp = check new (host = dbHost, user = dbUser, password = dbPass, database = dbName, port = dbPort);

type Product record {
    int id;
    string name;
    string description;
    int price;
    string imageUrl;
};

service / on new http:Listener(9090) {
    resource function get products() returns Product[]|error {
        stream<Product, error?> resp = mysqlEp->query(`SELECT * FROM product_details`, Product);
        Product[] products = [];
        check resp.forEach(function(Product product) {
            products.push(product);
        });
        
        return products;
    }
}

