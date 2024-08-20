package com.mongodb.bankaccount.application.config;

import com.mongodb.ClientEncryptionSettings;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateEncryptedCollectionParams;
import com.mongodb.client.vault.ClientEncryptions;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class EncryptionFieldConfig {

    protected boolean collectionExists(MongoDatabase db, String collectionName) {
        return db.listCollectionNames().into(new ArrayList<>()).contains(collectionName);
    }

    protected void createEncryptedCollection(MongoDatabase db, ClientEncryptionSettings clientEncryptionSettings) {
        var clientEncryption = ClientEncryptions.create(clientEncryptionSettings);
        var encryptedCollectionParams = new CreateEncryptedCollectionParams("local")
                .masterKey(new BsonDocument());

        var createCollectionOptions = new CreateCollectionOptions().encryptedFields(encryptFields());

        clientEncryption.createEncryptedCollection(db, "accounts", createCollectionOptions, encryptedCollectionParams);
    }

    private BsonDocument encryptFields() {
        return new BsonDocument().append("fields",
                new BsonArray(Arrays.asList(
                        createEncryptedField("accountNumber", "string", "equality"),
                        createEncryptedField("cardVerificationCode", "int",  "equality"),
                        createEncryptedField("accountBalance", "double",  "range")
                )));
    }

    private BsonDocument createEncryptedField(String path, String bsonType, String queryType) {

        return new BsonDocument()
                .append("keyId", new BsonNull())
                .append("path", new BsonString(path))
                .append("bsonType", new BsonString(bsonType))
                .append("queries", query(queryType));
    }

    private BsonDocument query(String type) {
        return new BsonDocument().append("queryType", new BsonString(type));
    }


}
