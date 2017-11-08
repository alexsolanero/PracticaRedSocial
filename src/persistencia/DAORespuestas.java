package persistencia;

import java.util.ArrayList;


import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import modelo.Respuesta;



public class DAORespuestas {

	public static ObjectId insert(Respuesta respuesta) {
		Document doc=new Document();
		doc.append("email", respuesta.getEmail());
		doc.append("fecha", respuesta.getFecha());
		doc.append("idPublicacion", respuesta.getIdPublicacion());
		doc.append("mensaje", respuesta.getMensaje());
		doc.append("nombre", respuesta.getNombre());
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>publicaciones=broker.getCollection("Respuestas");
		publicaciones.insertOne(doc);
		
		 ObjectId id = (ObjectId)doc.get( "_id" );
		
		 return id;
	}
	
	public static ArrayList<Respuesta> select(String idPublicacion) throws Exception {
		
		ArrayList<Respuesta> result = new ArrayList<Respuesta>();
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> publicaciones=broker.getCollection("Respuestas");
		Document criterio=new Document();
		criterio.append("idPublicacion", idPublicacion);
		
		FindIterable<Document> resultado=publicaciones.find(criterio);
		MongoCursor<Document> cursor = resultado.iterator();
		
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			Respuesta resp = new Respuesta(doc.getString("email"), doc.getString("fecha"), doc.getString("idPublicacion"), doc.getString("mensaje"), doc.getString("nombre"));
			result.add(resp);
		}
				
		return result;
	}
	
public static void delete(String id) {
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>respuestas=broker.getCollection("Respuestas");
		Document criterio=new Document();
		criterio.append("idPublicacion", id);
		respuestas.deleteMany(criterio);
		
	}
	
}
