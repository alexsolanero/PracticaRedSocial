package com.redsocial.persistencia;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.redsocial.auxiliares.Utilidades;
import com.redsocial.modelo.Publicacion;
import com.redsocial.modelo.Usuario;


/**
 * 
 * @author Usuario
 *
 */
@Component
public class DAOUsuario {
	
	private final static String nombre = "nombre";
	private final static String pwdd = "pwd";
	private final static String pwrd = "password";
	private final static String emaill = "email";
	private final static String Usuarioss = "Usuarios";
	private final static String idd = "_id";
	
	/**
	 * 
	 * @param email
	 * @param pwd
	 * @return Usuario si es correcto, null si sus credenciales no son correctas
	 */
	public static Usuario login(String email,String pwd) throws Exception {
		Usuario result = null;
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> usuarios=broker.getCollection("Usuarios");
		Document criterio=new Document();
		criterio.append("email", email);
		criterio.append("pwd", Utilidades.Encriptar(pwd));
		
		FindIterable<Document> resultado=usuarios.find(criterio);
		Document usuario=resultado.first();
		
		if (usuario!=null) {
			result = new Usuario (usuario.getString(nombre), usuario.getString(emaill), usuario.getString(pwdd));
		}
		
		return result;
	}
	
	/**
	 * @param usuario (email)
	 * @return Usuario asociado a ese email o null si el email no existe
	 */
	public static Usuario select(Usuario usuario) throws Exception {
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> usuarios=broker.getCollection("Usuarios");
		Document criterio=new Document();
		criterio.append(emaill, usuario.getemail());
		
		FindIterable<Document> resultado=usuarios.find(criterio);
		Document doc=resultado.first();
		Usuario result=null;
		if (doc!=null) {
			ObjectId id = (ObjectId)doc.get( idd );
			result = new Usuario (id.toString(),doc.getString(nombre), doc.getString(emaill), doc.getString(pwdd));
			result.setid(id.toString());
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * @param idUsuario
	 * @return El usuario asociado a ese id o null en caso de no existir
	 */
	public static Usuario selectWithID(String idUsuario) throws Exception {
		Usuario result = null;
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> usuarios=broker.getCollection("Usuarios");
		Document criterio=new Document();
		criterio.append("_id", new ObjectId(idUsuario));
		
		FindIterable<Document> resultado=usuarios.find(criterio);
		Document doc=resultado.first();
		
		if (doc!=null) {
			ObjectId id = (ObjectId)doc.get("_id");
			result = new Usuario(id.toString(), doc.getString("nombre"), doc.getString("email"), doc.getString("pwd"));
		}
		
		return result;
	}

	/**
	 * 
	 * @param id
	 * @method borra el usuario asociado a el id
	 */
	public static void delete(String id) {
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>usuarios=broker.getCollection(Usuarioss);
		usuarios.deleteOne(new Document(idd, new ObjectId(id)));
		
	}
	/**
	 * 
	 * @param id
	 * @method borra el usuario asociado a el elmail
	 */
	public static void deleteConEmail(String email) {
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>usuarios=broker.getCollection(Usuarioss);
		usuarios.deleteOne(new Document(emaill, email));
		
	}
	
	/**
	 * 
	 * @return una lista de usuarios completos
	 */
	public static ArrayList<Usuario> selectAll () {
		
		ArrayList<Usuario> result = new ArrayList<Usuario>();
		MongoBroker broker = MongoBroker.get();
		MongoCollection<Document> usuarios=broker.getCollection(Usuarioss);
		FindIterable<Document> it = usuarios.find();
		MongoCursor<Document> cursor = it.iterator();
	
		while (cursor.hasNext()) {
			Document doc = cursor.next();
			ObjectId id = (ObjectId)doc.get("_id");
			Usuario usu = new Usuario(id.toString(),doc.getString(nombre), doc.getString(emaill), doc.getString(pwdd));
			result.add(usu);
		}

		return result;
	}
	
	/**
	 * 
	 * @param usuario (nombre, email, pwd)
	 * @return el usuario si todo va bien, null en caso contrario
	 * @throws Exception 
	 */
	public static Usuario insert(Usuario usuario) throws Exception{
		Document doc=new Document();
		Usuario aux= select(usuario);
		if(aux!=null) return null;
		doc.append(nombre, usuario.getNombre());
		doc.append(emaill, usuario.getemail());
		doc.append("pwd", usuario.getPwd());

		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>usuarios=broker.getCollection(Usuarioss);
		usuarios.insertOne(doc);

		ObjectId id = (ObjectId)doc.get( idd );
		Usuario result = null;
		if (id!=null) {
			result = usuario;
			result.setid(id.toString());
		}

		return result;
	}
	
	/**
	 * 
	 * @param usuario (lo identificamos con el id)
	 * @method modifica el resto de campos del usuario con el id en cuestion
	 */
	public static void update (Usuario usuario) throws Exception {
		
		Document filter = new Document("_id", new ObjectId(usuario.getid()));
		Document newValue = new Document();
		newValue.append(nombre, usuario.getNombre());
		newValue.append(emaill, usuario.getemail());
		newValue.append("pwd", usuario.getPwd());
		Document updateOperationDocument = new Document("$set", newValue);
		
		MongoBroker broker= MongoBroker.get();
		MongoCollection<Document>usuarios=broker.getCollection("Usuarios");
		usuarios.updateOne(filter, updateOperationDocument);
		
	}

}


