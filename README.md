
#Restful Banking
Ironhack Bootcamp Midterm Project
by Shaun Reilly

[TOC]

### Project Spec

This is the basis for a banking aplication that implements a RESTful API enabling transactions and account management to be done through HTTP requests. This project implements security measures with multiple roles to limit access for users to their own account details as well as admins with greater control.

### Getting started

For the best experience, these programs are recommended for testing functionality.

- Your favourite IDE
- MySQL WorkBench
- Postman

To enbable the connection to the SQL database;
- Create a new SQL schema in MySQL Workbench called "midterm"
- Create new credentials with the username "ironhacker" and the password "1r0nhacker"
- You can also change the datasource.url, datasource.username and datasource.password values within application.properties to pre-existing schema and credentials

After cloning the repo and setting up the SQL connection, you can start the program by running the main method in ShaunrlyMidtermProjectApplication.

Although there are routes to create new uses during runtime, for ease of testing, after running the application, go into MySQL Workbench and run these commands;

    USE midterm;
    
    INSERT INTO user (username, password) VALUES
    ("USER", "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e"),
    ("admin","$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e");
    
    INSERT INTO role (role_name, user_id) VALUES
    ("USER", 1),
    ("ADMIN", 2);

You can now use password "123456" as login credentials for both user and admin users.

### Usage

All routes use a common format and as such, all routes can be accessed using the following format. Some exceptions exist for the Third Party account type but they will be pointd out.

Note, all account commands in postman should be made with the correct credentials. All account creation routes and routes with the keyword "account" can be made with basic USER roles, all routes that use the keyword "admin" must be made using credentials with the ADMIN role.

##### Account Type
- checking
- student
- credit
- savings
- thirdparty

##### User Access
- account
- admin

#### POST requests

Post requests are made using the following format;

http://localhost:8080/[AccountType]/new
- Note, for security purposes only admins can create third party as such the route for creating a new admin is http://localhost:8080/thirdparty/admin/new

Each request must be made with a request body containing a valid object of the requested account type. An example of such a request body can be seen below;

    {
        "balance" : "1000",
        "primaryOwner" : {
            "name" : "Shaun",
            "dateOfBirth" : "1992-12-05",
            "primaryAddress" : {
                "residenceNumber" : 12,
                "streeName" : "The Road",
                "area" : "The area",
                "county" : "The county",
                "country" : "The country",
                "postcode" : "The postcode"
            },
            "mailingAddress" : {
                "residenceNumber" : 22,
                "streeName" : "The Other Road",
                "area" : "The Other area",
                "county" : "The Other county",
                "country" : "The Other country",
                "postcode" : "The Other postcode"
                },
            "username" : "USER"
        }, 
        "secondaryOwner" : null
    }

Please note the "username" field as this is important. Only users with a matching username to the username contained on the account file will have access to their accounts. For this example, the example USER credentials will work.

#### Put Requests

Put request are made by an admin to update an accounts details. The account will only be updated with the data that is passed to it and will not update any fields with NULL values. The pathway for Put requests is;

http://localhost:8080/[AccountType]/admin/{id}

Like Post requests, each put request must be made with a valid JSON representation of the type of account in question. Null values can be passed for data values that are not being altered.

##### Get Requests

There are two types of Get requests that can be used by either admins or users.

http://localhost:8080/[AccountType]/admin/getall

This route returns a list of all accounts of the given account type

http://localhost:8080/[AccountType]/account/{id}

This route returns the account requests but only if the username of the USER matches the username on the account file or the user is an ADMIN

#### Delete Requests

There is only one routing for delete requests and they can only be made by an admin.

http://localhost:8080/[AccountType]/admin/{id}

#### Patch Requests / Transfer Requests

This is the route used to transfer money between accounts. This is a Patch request.

http://localhost:8080/[AccountType]/account/transfer/{id}

The ID provided in the URL is the id of the account of which money is being taken out. Each transfer request must be made with a valid TransferDTO body, an example of which can be found below;

    {"transferAmount" : "100", 
	"targetId" : "2", 
	"name" : "shaun"}

The transfer will only succeed if the username of the current user matches the username on the account of which money is being taken out of and the username on the target account matches the supplied name in the request body.

Transfers for third party accounts use the same routing format but instead must supply;
- hashedKey matching the hashedKey on the third party account
- transfer amount
- target Id
- secretKey matching the secret key on the target account

