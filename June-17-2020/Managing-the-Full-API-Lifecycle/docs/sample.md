
# Mobile Store API Documentation
---
### INTRODUCTION
Sample api documentation for sample project.

# Allowed HTTPs requests:
```
PUT     : To create resource 
POST    : Update resource
GET     : Get a resource or list of resources
DELETE  : To delete resource
```

# Description Of Usual Server Responses:
- 200 `OK` - the request was successful (some API calls may return 201 instead).
- 201 `Created` - the request was successful and a resource was created.
- 204 `No Content` - the request was successful but there is no representation to return (i.e. the response is empty).
- 400 `Bad Request` - the request could not be understood or was missing required parameters.
- 401 `Unauthorized` - authentication failed or user doesn't have permissions for requested operation.
- 403 `Forbidden` - access denied.
- 404 `Not Found` - resource was not found.
- 405 `Method Not Allowed` - requested method is not supported for resource.

# Some sample
    Test sample                 |   test column two
    First column sadf asdfads   |   sfsdsadf

---
    test one                    | Another column    
---


# Code Sample
```java
    String sample = "";
```

[REFERENCE](https://raw.githubusercontent.com/tmkasun/labs/master/June-17-2020/Managing-the-Full-API-Lifecycle/APIDefinitions/MobileStore-0.9.yaml)