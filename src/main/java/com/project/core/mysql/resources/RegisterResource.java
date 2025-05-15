package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Owner;
import com.project.core.mysql.models.Register;
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

@Path("/register")
public class RegisterResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Register register) {
        EntityManager em = null;
        Map<String, Object> response = new HashMap<>();
        try {
            em = emf.createEntityManager();
            Owner knownOwner = em.find(Owner.class, register.getEmail());

            if (knownOwner != null) {
                response.put("type", "error");
                response.put("message", "Email déjà enregistré");
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
            }
            
            response.put("type", "success");
            response.put("message", "Vous êtes inscrit");
            return Response.ok().entity(response).build();
        } catch (Exception e) {
            // Log the exception details here
            response.put("type", "error");
            response.put("message", "Erreur lors de l'inscription: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
