package conexion;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.Driver;



public class Conexion {
	static String lineaConexionAtlas = "mongodb+srv://Fiiuns_10:ffunes12@cluster0.yllyof1.mongodb.net/test";
	  static CodecProvider pojoCodecProvider = null;
	  static CodecRegistry pojoCodecRegistry = null;
	  static MongoClient mclient = null;
	  static MongoCollection<Driver> driversCollection = null;
	  static MongoDatabase mdb = null;
	  public static final String db = "pilotos";
	  public Conexion() {
		  pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
	      pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
	      mclient = MongoClients.create(lineaConexionAtlas);
		  mdb = mclient.getDatabase(db).withCodecRegistry(pojoCodecRegistry);
		  driversCollection = mdb.getCollection("drivers", Driver.class);
		  
	  }
	  public  MongoCollection<Driver> getDriversCollection(){
		  return driversCollection;
		  
	  }
     
}
//