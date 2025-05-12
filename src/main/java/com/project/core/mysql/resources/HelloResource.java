package com.project.core.mysql.resources;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class HelloResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em = emf.createEntityManager();

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    /*
    @GET
    @Path("/movies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies() {
        var movies = em.createQuery("select m from Movie m").getResultList();
        if (movies.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No movies found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
        return Response.ok(movies).build();
    }*/
}