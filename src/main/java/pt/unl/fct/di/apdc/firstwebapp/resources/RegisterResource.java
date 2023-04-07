package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import com.google.gson.Gson;
import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)

public class RegisterResource {

    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    /**
     * Logger Object
     */
    private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

    private final Gson g = new Gson();
    KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    public RegisterResource() {
    } // nothing to be done here

    @POST
    @Path("/v1")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doRegistrationV1(RegisterData data) {
        LOG.fine("Attempt to register user: " + data.username);

        //Checks input data
        if( !data.validResgistration() ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter").build();
        }
        // Creates an entity user from the data. The key is username
        Key userKey = userKeyFactory.newKey(data.username);
        Entity user = Entity.newBuilder(userKey)
                .set("user_pwd", DigestUtils.sha512Hex(data.password))
                .set("user_creation_time", Timestamp.now())
                .build();

        //bad idea, put overwrites previous information that may exist
        datastore.put(user);
        LOG.info("User registered " + data.username);

        return Response.ok("newUser").build();
    }

    @POST
    @Path("/v4")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response doRegistrationV4(RegisterData data) {
        LOG.fine("Attempt to register user: " + data.username);

        //Checks input data
        if( !data.validResgistration() ) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter").build();
        }
        Transaction txn = datastore.newTransaction();  //inicio da transacao
        try{
            Key userKey = userKeyFactory.newKey(data.username);
            Entity user = txn.get(userKey);
            if(user!= null){
                txn.rollback();
                return Response.status(Response.Status.BAD_REQUEST).entity("User already exists.").build();
            } else{
                // Creates an entity user from the data. The key is username
                user = Entity.newBuilder(userKey)
                        .set("user_name", data.username)
                        .set("user_pwd", DigestUtils.sha512Hex(data.password))
                        .set("user_email", data.email)
                        .set("user_creation_time", Timestamp.now())
                        .build();
                txn.add(user);
                LOG.info("User registered " + data.username);
                txn.commit();
                return Response.ok(data.username + " was added as newUser").build();
            }
        }finally {
            if(txn.isActive()){
                txn.rollback();
            }
        }
    }

}