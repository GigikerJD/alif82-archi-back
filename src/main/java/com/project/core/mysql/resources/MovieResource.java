package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Jouer;
import com.project.core.mysql.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/movies")
public class MovieResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em = emf.createEntityManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies() {
        var movies = em.createQuery("select m from Movie m").getResultList();
        Map<String, Object> response = new HashMap<>();
        if (movies.isEmpty()) {
            response.put("message", "No movies found");
            response.put("status", 404);
            return Response.ok(response).build();
        }
        response.put("message", "Found " + movies.size() + " movies");
        response.put("status", 200);
        return Response.ok(movies).build();
    }

    /*
    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("title") String title) {

    }

    @GET
    @Path("/city")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByCity(@QueryParam("name") String cityName) {

    }
    */
}