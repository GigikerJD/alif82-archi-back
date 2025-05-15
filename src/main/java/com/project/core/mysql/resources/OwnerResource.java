package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Owner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/owners")
public class OwnerResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwners() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            List<Owner> owners = em.createQuery("SELECT o FROM Owner o", Owner.class).getResultList();
            return Response.ok(owners).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving owners: " + e.getMessage())
                    .build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
