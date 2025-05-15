package com.project.core.mysql.resources;


import com.project.core.mysql.entities.Acteur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;

@Path("/actors")
public class ActeurResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em = emf.createEntityManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActors() {
        var actors = em.createQuery("SELECT a FROM Acteur a", Acteur.class).getResultList();
        var response = new HashMap<String, Object>();
        if (actors.isEmpty()) {
            response.put("type", "error");
            response.put("message", "No actors found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
        response.put("type", "success");
        response.put("actors", actors);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActor(@PathParam("id") int id) {
        var actor = em.find(Acteur.class, id);
        var response = new HashMap<String, Object>();
        if (actor == null) {
            response.put("type", "error");
            response.put("message", "No actor found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
        response.put("type", "success");
        response.put("actor", actor);
        return Response.status(Response.Status.OK).entity(response).build();
    }
}
