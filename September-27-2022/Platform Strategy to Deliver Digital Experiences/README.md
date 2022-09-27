
# Welcome to Choreo workshop

Rough agenda,
1. A service that reads data from Snowflake
2. How Choreo make life easy for developers
    1. Connector market place
        - Public and private marketplace
        - Use public connectors
        - Use private connectors within the organization
    2. Familiar dev/test workflow
    3. Handling credentials outside of the main program
3. Publish an API and create a connector
4. Developing the process API - reading data from above API and query Salesforce campaigns
5. Publish a connector for the process API
6. Creating the experience API of reading Salesforce campaigns and sending a slack message

## 1. Developing a Ballerina program locally

Using VScode to develop a hello world Ballerina program (service)

### Writing a simple HTTP service

```
import ballerina/http;

service / on new http:Listener(9000) {
    resource function get greeting() returns string {
        return "hello, world!";
    }
}
```

Run with `$ bal run <filename.bal>`

If you create the ballerina project with `$ bal new myproject` you can just say `$ bal run`

## 2 Creating the CRUD API (Reading data in this case)

Use [Choreo](https://choreo.dev) for rest of the tutorial. Sign up for a trial account

### Creating a service to expose data stored in Snowflake

1. Create the service skeleton
2. Use snowflake connector (imports). `$ bal build` will pull the connector to the current workspace
```
import ballerinax/snowflake;
import ballerinax/snowflake.driver as _;
```
3. Configure credentials to connect to Snowflake
```
string account_identifier = "fv00928.west-us-2.azure";
configurable string user = "";
configurable string password = "";
```
Note: Configurable values. Should be configured externally. Can be done through config file and environment variables
4. Define snowflake options
```
snowflake:Options sfOptions = {
    requestGeneratedKeys: snowflake:NONE,
    properties: ({"warehouse": "mywarehouse"})
};
```
5. Connect and create a query
```
snowflake:Client snowflakeClient = check new(account_identifier, user, password, sfOptions);
sql:ParameterizedQuery sqlQuery = `SELECT * FROM SNOWFLAKE_SAMPLE_DATA.TPCDS_SF100TCL.CUSTOMER LIMIT 10`;
```
6. Execute and get results. Note the general record{} strucuture
```
stream<record{}, error?> resultStream = snowflakeClient->query(sqlQuery);
```
7. Define data types for dealing with messages. Optional values with ? By default this record type is an open record. Additional fields will be ignored. Can define closed records with record{| ... |} syntax. All attributes should be mapped in closed records
```
type InCustomer record {
    string? C_FIRST_NAME;
    string? C_EMAIL_ADDRESS;
    string? C_BIRTH_COUNTRY;
};
```
8. Change result query with proper type
```
stream<InCustomer, error?> resultStream = snowflakeClient->query(sqlQuery);
```
9. Query Expressions - Back to query expression slides
```
check from InCustomer c in resultStream
            do {
                // InCustomer c = check result.cloneWithType(InCustomer);
                // myCustomers.push(c);
                customers.push({
                    Name: c.C_FIRST_NAME,
                    Email: c.C_EMAIL_ADDRESS,
                    Country: c.C_BIRTH_COUNTRY
                });
            };
```
10. Exectue and see results. Then define a titleCase function for converting the country to title case. Notice function input string? which means input can either be a string or nil. First if condition act as a type guard. Ballerina "forcing" the develope to handle nullable instances explicitly. Within the if block it's guaranteed that words in not nil (null)
```
function titleCase(string? words) returns string {
    if words is string {
        string[] w = regex:split(words, " ");
        int i = 0;
        while (i < w.length()) {
            string s = w[i];
            w[i] = s[0] + s.substring(1, s.length()).toLowerAscii();            
            i+=1;
        }
        return string:'join(" ", ...w);
    } else {
        return "";
    }
}
```
11. Execute and check
Full program should look like below
```
import ballerina/http;
import ballerinax/snowflake;
import ballerinax/snowflake.driver as _;
import ballerina/sql;
import ballerina/regex;

string account_identifier = "fv00928.west-us-2.azure";
configurable string user = "";
configurable string password = "";
snowflake:Options sfOptions = {
    requestGeneratedKeys: snowflake:NONE,
    properties: ({"warehouse": "mywarehouse"})
};

type InCustomer record {
    string? C_FIRST_NAME;
    string? C_EMAIL_ADDRESS;
    string? C_BIRTH_COUNTRY;
};

type Customer record {
    string? Name;
    string? Email;
    string? Country;
};

service /customer on new http:Listener(9000) {
    resource function get data() returns Customer[]|error? {
        snowflake:Client snowflakeClient = check new (account_identifier, user, password, sfOptions);
        sql:ParameterizedQuery sqlQuery = `SELECT * FROM SNOWFLAKE_SAMPLE_DATA.TPCDS_SF100TCL.CUSTOMER ORDER BY C_CUSTOMER_ID LIMIT 10`;
        stream<InCustomer, error?> resultStream = snowflakeClient->query(sqlQuery);
        Customer[] customers = [];
        check from InCustomer c in resultStream
            do {
                // InCustomer c = check result.cloneWithType(InCustomer);
                // myCustomers.push(c);
                customers.push({
                    Name: c.C_FIRST_NAME,
                    Email: c.C_EMAIL_ADDRESS,
                    Country: titleCase(c.C_BIRTH_COUNTRY)
                });
            };
        return customers;
    }
}

function titleCase(string? words) returns string {
    if words is string {
        string[] w = regex:split(words, " ");
        int i = 0;
        while (i < w.length()) {
            string s = w[i];
            w[i] = s[0] + s.substring(1, s.length()).toLowerAscii();
            i += 1;
        }
        return string:'join(" ", ...w);
    } else {
        return "";
    }
}
```
12. Copy paste the program to Choreo. Add the connector to make sure connector is installed into the current workspace.
13. Run and try out the program. Notice platform is popping up to accept username/password (configurable values in the program)
14. Publish and expose a connector out of this service

## 2 Create the API with Salesforce integration using response from above CRUD API

Full program,

```
import ballerinax/salesforce;
import chintanawilamuna/myapp1;
import ballerina/http;

type CompaignSummary record {
    string StartDate;
    decimal ExpectedRevenue;
    decimal ActualCost;
    string EndDate;
    decimal BudgetedCost;
    string Name;
};

configurable string sfRefreshToken = ?;
configurable string sfClientID = ?;
configurable string sfClientSecret = ?;

configurable string myapp1ClientID = ?;
configurable string myapp1ClientSecret = ?;

salesforce:Client salesforceEp = check new (salesforceConfig = {
    baseUrl: "https://abcd-2c1-dev-ed.my.salesforce.com",
    clientConfig: {
        refreshUrl: "https://login.salesforce.com/services/oauth2/token",
        refreshToken: sfRefreshToken,
        clientId: sfClientID,
        clientSecret: sfClientSecret
    }
});
myapp1:Client myapp1Ep = check new (clientConfig = {
    auth: {
        clientId: myapp1ClientID,
        clientSecret: myapp1ClientSecret
    }
});

service / on new http:Listener(9090) {

    resource function get getSummary() returns CompaignSummary|error? {

        myapp1:Customer[] customers = check myapp1Ep->getData();
        CompaignSummary summary;
        stream<record {}, error?> sfResults = check salesforceEp->getQueryResultStream("SELECT Name, BudgetedCost, ActualCost, ExpectedRevenue, StartDate, EndDate from Campaign where Name ='" + customers[0].Email.toString() + "'");
        check from record {} v in sfResults
            do {
                summary = check v.cloneWithType(CompaignSummary);
            };
        return summary;

    }
}

```

## 3 Create the experience API that calls Salesforce integration API, get campaign data and send a slack message

Full program,

```
import chintanawilamuna/mysfapp1;
import ballerinax/slack;
import ballerina/http;

configurable string mysfappClientSecret = ?;

configurable string mysfappClientID = ?;

mysfapp1:Client mysfapp1Ep = check new (clientConfig = {
    auth: {
        clientId: mysfappClientID,
        clientSecret: mysfappClientSecret
    }
});
configurable string slackToken = ?;

slack:Client slackEp = check new (config = {
    auth: {
        token: slackToken
    }
});

service / on new http:Listener(9090) {

    resource function get greeting(string name) returns error? {

        mysfapp1:CompaignSummary getGetsummaryResponse = check mysfapp1Ep->getGetsummary();

        string str = "Campaign summary: " + getGetsummaryResponse.Name + " \n\t" +
            getGetsummaryResponse.StartDate + "-" + getGetsummaryResponse.EndDate + " \t Expected revenue: "+
            getGetsummaryResponse.ExpectedRevenue.toString() + " :rocket:";
        slack:Message msg = {
            channelName: "general",
            text: str,
            mrkdwn: true
        };
        var _ = check slackEp->postMessage(msg);
    }
}
```

## Notes

If you're using the trial, make sure to sign up for the developer edition. If you just sign up for the trial it (at the time of this writing) will give you a professional edition trial which doesn't have the API component to connect and work with data remotely

### Get Salesforce consumer key, secret and refresh token

Follow [this excellent blog by Pramodya](https://medium.com/@bpmmendis94/obtain-access-refresh-tokens-from-salesforce-rest-api-a324fe4ccd9b) to get necessary credentials.

### Snowflake access

Create a trial account and attach a warehouse

### Slack setup

Refer to Ballerina Slack API docs to configure the Slack app and obtain tokens - https://lib.ballerina.io/ballerinax/slack/3.0.0
