package persistencia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import modelo.MensajesPrivados;

public class DAOMensajesPrivados {
	
	public static ObjectId insert(MensajesPrivados msg) {
		Document doc=new Document();
		doc.append("idmensaje", msg.getIdmensaje());
		doc.append("fecha", msg.getFecha());
		doc.append("destinatario", msg.getDestinatario());
		doc.append("emisor", msg.getEmisor());
		doc.append("mensaje", msg.getMensaje());
		
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>mensajes=broker.getCollection("MensajesPrivados");
		mensajes.insertOne(doc);
		
		 ObjectId id = (ObjectId)doc.get( "_id" );
		
		 return id;
	}
	

	
	public static void update (MensajesPrivados msg) throws Exception {
		
		// Montamos la fecha actual para saber cuando se hizo el mensaje.
		 Calendar fecha = new GregorianCalendar();
		 String fechaMensaje = "";
	     int year = fecha.get(Calendar.YEAR);
	     // Se le suma uno, porque calendar.month devuelve de 0-11
	     int month = fecha.get(Calendar.MONTH)+1;
	     int day = fecha.get(Calendar.DAY_OF_MONTH);
	     int hour = fecha.get(Calendar.HOUR_OF_DAY);
	     int minute = fecha.get(Calendar.MINUTE);
	     String monthS = (month<10)?"0"+month:""+month;
	     String dayS = (day<10)?"0"+day:""+day;
	     fechaMensaje = dayS+"/"+monthS+"/"+year+" "+hour+":"+minute;
		
		Document filter = new Document("_id", new ObjectId(msg.getIdmensaje()));
		Document newValue = new Document();
		newValue.append("mensaje", msg.getMensaje());
		newValue.append("fecha", fechaMensaje);
		Document updateOperationDocument = new Document("$set", newValue);
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>mensajes=broker.getCollection("MensajesPrivados");
		mensajes.updateOne(filter, updateOperationDocument);
		
	}

	public static MensajesPrivados select(String idmensaje) throws Exception {
		MensajesPrivados result = null;
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> mensajes=broker.getCollection("MensajesPrivados");
		Document criterio=new Document();
		criterio.append("id", idmensaje);
		
		FindIterable<Document> resultado=mensajes.find(criterio);
		Document mensaje=resultado.first();
		
		if (mensaje!=null) {
			result = new MensajesPrivados(mensaje.getString("idmensaje"), mensaje.getString("fecha"), 
					mensaje.getString("emaildestinatario"), mensaje.getString("emailemisor"), mensaje.getString("mensaje"));
			
		}
		
		return result;
	}
	
	public static ArrayList selectAll () {
		
		ArrayList<MensajesPrivados> result = new ArrayList<MensajesPrivados>();
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> mensajes=broker.getCollection("MensajesPrivados");
		FindIterable<Document> it = mensajes.find();
		MongoCursor<Document> cursor = it.iterator();
	
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			ObjectId id = (ObjectId)doc.get( "_id" );
			MensajesPrivados msg = new MensajesPrivados(id.toString(), doc.getString("fecha"), doc.getString("destinatario"), doc.getString("emisor"), doc.getString("mensaje"));
			result.add(msg);
		}

		return result;
	}
	
	public static ArrayList selectMsgUser (String emailUser) {
		
		ArrayList<MensajesPrivados> result = new ArrayList<MensajesPrivados>();
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> mensajes=broker.getCollection("MensajesPrivados");
		FindIterable<Document> it = mensajes.find();
		MongoCursor<Document> cursor = it.iterator();
	
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			ObjectId id = (ObjectId)doc.get( "_id" );
			MensajesPrivados msg = new MensajesPrivados(id.toString(), doc.getString("fecha"), doc.getString("destinatario"), doc.getString("emisor"), doc.getString("mensaje"));
			if(doc.getString("emaildestinatario")==emailUser)
				result.add(msg);
		}

		return result;
	}
	
	public static void delete(String id) {
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>mensajes=broker.getCollection("MensajesPrivados");
		mensajes.deleteOne(new Document("_id", new ObjectId(id)));
		
	}

}
