package Backend;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;


@Service
public class MongoDbCache implements UserCache {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> mongoCollection;

    public MongoDbCache() {
        mongoClient = MongoClients.create("mongodb://localhost:27017/");
        mongoDatabase = mongoClient.getDatabase("bank");
        mongoCollection = mongoDatabase.getCollection("currentAccounts");
    }

    @Override
    public void addAccount(Account account) {
        Document document = new Document("bankId", account.getId())
                .append("balance", account.getBalance())
                .append("firstName", account.getFirstName())
                .append("lastName", account.getLastName())
                .append("bankPin", account.getBankPin());
        mongoCollection.insertOne(document);
    }

    public Account getAccount(long bankId, long bankPin) {
        Document document = mongoCollection.find(Filters.and(Filters.eq("bankId", bankId)
                , Filters.eq("bankPin", bankPin))).first();
        if (document != null) {
            return new Account(document.getLong("bankId"),
                    document.getDouble("balance"),
                    document.getString("firstName"),
                    document.getString("lastName"),
                    document.getLong("bankPin"));
        }
        return null;
    }

    public boolean accountExists(long bankId, long bankPin, String firstName, String lastName) {
        Document document = mongoCollection.find(Filters.and(
                Filters.eq("bankId", bankId),
                Filters.eq("bankPin", bankPin),
                Filters.eq("firstName", firstName.toLowerCase()),
                Filters.eq("lastName", lastName.toLowerCase()))).first();
        return document != null;
    }

    public boolean existingAccount(long bankId, long bankPin) {
        Document document = mongoCollection.find(Filters.and(Filters.eq("bankId", bankId), Filters.eq("bankPin", bankPin))).first();
        return document != null;
    }

    @Override
    public double updateBalance(long bankId, double balance) {
        mongoCollection.updateOne(Filters.eq("bankId", bankId), Updates.set("balance", balance));
        Document document = mongoCollection.find(Filters.eq("bankId", bankId)).first();
        if (document != null) {
            return document.getDouble("balance");
        }
        return -1;
    }

    public long generateBankPin() {
        Random random = new Random();
        return random.nextLong(1013, 9894);
    }

    public long generateBankId() {
        Random random = new Random();
        return random.nextLong(675000000, 675999999);
    }
}

