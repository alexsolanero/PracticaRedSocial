package persistencia;


import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import modelo.Like;


public class DAOLike {
  
  public static void insert(Like like) throws Exception {

    
    Document doc=new Document();
    doc.append("emailUsuario", like.getEmailUsuario());
    doc.append("idPublicacion", like.getIdPublicacion());    
    
    MongoBroker broker= MongoBroker.get();
    MongoCollection<Document>likes=broker.getCollection("Likes");
    likes.insertOne(doc);
  }
  

  
  public static void update (Like like) throws Exception {
    Document doc=new Document();
    doc.append("idPublicacion", like.getIdPublicacion());
    
    MongoBroker broker= MongoBroker.get();
    MongoCollection<Document>likes=broker.getCollection("Likes");
    likes.deleteOne(doc);
    
    Document doc2=new Document();
    doc2.append("emailUsuario", like.getEmailUsuario());
    doc2.append("idPublicacion", like.getIdPublicacion());
    
    likes.insertOne(doc2);
    
  }

  
  public static void delete(Like like) {
    Document doc=new Document();
    doc.append("idPublicacion", like.getIdPublicacion());
    doc.append("emailUsuario", like.getEmailUsuario());
    
    MongoBroker broker= MongoBroker.get();
    MongoCollection<Document>likes=broker.getCollection("Likes");
    likes.findOneAndDelete(doc);
    
  }
  
  public static void deleteAll(String idPublicacion) {
	    Document doc=new Document();
	    doc.append("idPublicacion", idPublicacion);
	    
	    MongoBroker broker= MongoBroker.get();
	    MongoCollection<Document>likes=broker.getCollection("Likes");
	    likes.deleteMany(doc);
	    
	  }
  
  public static ArrayList<Like> select(String idPublicacion) throws Exception {
		
		ArrayList<Like> result = new ArrayList<Like>();
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> publicaciones=broker.getCollection("Likes");
		Document criterio=new Document();
		criterio.append("idPublicacion", idPublicacion);
		
		FindIterable<Document> resultado=publicaciones.find(criterio);
		MongoCursor<Document> cursor = resultado.iterator();
		
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			Like like = new Like(doc.getString("emailUsuario"), doc.getString("idPublicacion"));
			result.add(like);
		}
				
		return result;
	}
  	
  public static Like checkLike(String idPublicacion,String emailUsuario) throws Exception {
		Like result = null;
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> likes=broker.getCollection("Likes");
		Document criterio=new Document();
		criterio.append("idPublicacion", idPublicacion);
		criterio.append("emailUsuario", emailUsuario);
		
		FindIterable<Document> resultado=likes.find(criterio);
		Document doc=resultado.first();
		
		if (doc!=null) {
			result = new Like (doc.getString("idPublicacion"), doc.getString("emailUsuario"));
		}
		
		return result;
	}

}