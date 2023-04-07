package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.gson.Gson;
import pt.unl.fct.di.apdc.firstwebapp.util.AuthToken;
import pt.unl.fct.di.apdc.firstwebapp.util.LoginData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	/**
	 * Logger Object
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	
	private final Gson g = new Gson();

	public LoginResource() {
	} // nothing to be done here

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doLogin(LoginData data) {
		LOG.fine("Login attempt by user: " + data.username);
		
		if(data.username.equals("jleitao") && data.password.equals("password")) { 
			AuthToken at = new AuthToken(data.username);
			return Response.ok(g.toJson(at)).build();
	}
		return Response.status(Response.Status.FORBIDDEN).entity("Incorrect username or password.").build();
	}
	
	@GET
	@Path("/{username}")
	public Response checkUsernameAvailable(@PathParam("username") String username) {
		if(username.equals("jleitao")) {
			return Response.ok().entity(g.toJson(false)).build();
		} else {
			return Response.ok().entity(g.toJson(true)).build();
		}
	}
	
	//VERIFICAR
	/*
	@GET
	@Path("/v1")
	public Response resgisterPerson(@PathParam("username") String username) {

		Key userKey = datastore.newKeyFactory().setKind("Person").newKey("Carlos");
		
		Entity person = Entity.newBuilder(userKey).set("email", "cd@fct.unl.pt").build();
		
		datastore.put(person);
		
		return Response.ok().entity(g.toJson(true)).build();
	
	}*/

}
