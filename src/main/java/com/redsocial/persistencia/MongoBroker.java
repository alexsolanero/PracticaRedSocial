package com.redsocial.persistencia;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 
 * @author Usuario
 *
 */
public class MongoBroker {

	private static MongoBroker yo;
	
	private MongoClient mongoClient;
	private MongoDatabase db;
	
	private MongoBroker(){
		this.mongoClient= new MongoClient(
				  new MongoClientURI( "mongodb://alba:pro2017@ds135196.mlab.com:35196/intravita" )
				);
        this.db= mongoClient.getDatabase("intravita");
	}
	
	public static MongoBroker get(){
		if(yo==null){
			yo=new MongoBroker();
		}
		return yo;
	}
	
	public MongoCollection<Document> getCollection(String collection){
		MongoCollection<Document> result=db.getCollection(collection, Document.class);
		if(result==null){
			db.createCollection(collection);
			result = db.getCollection(collection,Document.class);
		}
		return result;
	}

}
