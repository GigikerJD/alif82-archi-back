package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Owner;
import com.project.core.mysql.models.Login;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/login")
public class LoginResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em = emf.createEntityManager();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login login) {
        var owner = em.find(Owner.class, "select * from owner where email = '" + login.getEmail() + "'");
        if (owner == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Owner wasn't found").build();
        }
        if(!login.getPassword().equals(owner)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong password").build();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Vous êtes connecté");
        data.put("email", login.getEmail());
        data.put("firstname", owner.getFirstname());
        data.put("lastname", owner.getLastname());
        return Response.ok(data).build();
    }
}
