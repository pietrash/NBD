package storage;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.internal.MongoClientImpl;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

public abstract class AbstractMongoRepository implements AutoCloseable {
    //    private final ConnectionString connectionString =
//            new ConnectionString("mongodb://localhost:27017/?replicaSet=replica_set_single");
    private final ConnectionString connectionString =
            new ConnectionString("mongodb://localhost:27017");
    private final MongoCredential mongoCredential = MongoCredential.createCredential("admin", "admin",
            "adminpassword".toCharArray());
    private final CodecRegistry pojoCodecRegistry =
            CodecRegistries.fromProviders(PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build());

    private MongoClientImpl mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoClientImpl getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(mongoCredential)
                .applyConnectionString(connectionString)
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        mongoClient = (MongoClientImpl) MongoClients.create(settings);
        mongoDatabase = mongoClient.getDatabase("rentDB");
    }
}
