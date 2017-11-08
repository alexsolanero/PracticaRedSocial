package persistencia;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import modelo.Publicacion;

public class DAOPublicaciones {

    
	public static ObjectId insert(Publicacion publicacion) {
		Document doc=new Document();
		doc.append("name", publicacion.getNombre());
		doc.append("email", publicacion.getEmail());
		doc.append("fecha", publicacion.getFecha());
		doc.append("mensaje", publicacion.getMensaje());
		doc.append("imagen", publicacion.getImagen());
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>publicaciones=broker.getCollection("Publicaciones");
		publicaciones.insertOne(doc);
		
		 ObjectId id = (ObjectId)doc.get( "_id" );
		
		 return id;
	}
	

	
	public static void update (Publicacion publicacion) throws Exception {
		
		// Montamos la fecha actual para saber cuando se hizo la publicacion.
		 Calendar fecha = new GregorianCalendar();
		 String fechaPublicacion = "";
	     int year = fecha.get(Calendar.YEAR);
	     // Se le suma uno, porque calendar.month devuelve de 0-11
	     int month = fecha.get(Calendar.MONTH)+1;
	     int day = fecha.get(Calendar.DAY_OF_MONTH);
	     int hour = fecha.get(Calendar.HOUR_OF_DAY);
	     int minute = fecha.get(Calendar.MINUTE);
	     String monthS = (month<10)?"0"+month:""+month;
	     String dayS = (day<10)?"0"+day:""+day;
	     fechaPublicacion = dayS+"/"+monthS+"/"+year+" "+hour+":"+minute;
		
		Document filter = new Document("_id", new ObjectId(publicacion.getIdPublicacion()));
		Document newValue = new Document();
		newValue.append("mensaje", publicacion.getMensaje());
		newValue.append("fecha", fechaPublicacion);
		Document updateOperationDocument = new Document("$set", newValue);
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>publicaciones=broker.getCollection("Publicaciones");
		publicaciones.updateOne(filter, updateOperationDocument);
		
	}

	public static Publicacion select(String idPublicacion) throws Exception {
		Publicacion result = null;
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> publicaciones=broker.getCollection("Publicaciones");
		Document criterio=new Document();
		criterio.append("id", idPublicacion);
		
		FindIterable<Document> resultado=publicaciones.find(criterio);
		Document publicacion=resultado.first();
		
		if (publicacion!=null) {
			result = new Publicacion(publicacion.getString("idPublicacion"), publicacion.getString("email"), 
			publicacion.getString("nombre"), publicacion.getString("fecha"), publicacion.getString("imagen"), publicacion.getString("mensaje"));
		}
		
		return result;
	}
	
	public static ArrayList<Publicacion> selectAll () {
		
		ArrayList<Publicacion> result = new ArrayList<Publicacion>();
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> publicaciones=broker.getCollection("Publicaciones");
		FindIterable<Document> it = publicaciones.find().sort(new BasicDBObject("_id",-1));
		MongoCursor<Document> cursor = it.iterator();
	
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			ObjectId id = (ObjectId)doc.get( "_id" );
			Publicacion publi = new Publicacion(id.toString(), doc.getString("email"), doc.getString("name"), doc.getString("fecha"), doc.getString("imagen"), doc.getString("mensaje"));
			result.add(publi);
		}

		return result;
	}
	
	public static void delete(String id) {
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>publicaciones=broker.getCollection("Publicaciones");
		publicaciones.deleteOne(new Document("_id", new ObjectId(id)));
		
	}
	

}
