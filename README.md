# BankAccount Service

This is a brief guide on how to use the BankAccount service, including how to call the different endpoints.

## Insert One
To insert a new bank account, make a `POST` request to `http://localhost:8080/bank`. The body of the request should include the following fields:

- `accountHolderName` (required, string)
- `accountNumber` (required, string)
- `cardVerificationCode` (required, string)
- `accountBalance` (required, number)

### Example 

```
     curl --location 'http://localhost:8080/bank' \
        --header 'Content-Type: application/json' \
        --data '{
        "accountHolderName": "Ricas",
        "accountNumber": "9872334",
        "cardVerificationCode": "192",
        "accountBalance": 2100.2
}'
```

## Compass Encrypted data
![Encrypted](src/main/resources/images/encrypted.PNG)

## Find All
To retrieve all bank accounts, make a GET request to http://localhost:8080/bank.

## Find by Account Number
To find a bank account by its account number, make a GET request to http://localhost:8080/bank/{accountNumber}, replacing {accountNumber} with the actual account number of interest.


## Compass IN-USE Encryption

![Decrypted](src/main/resources/images/decrypted.PNG)
