package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Actor;
import com.project.core.mysql.entities.Jouer;
import com.project.core.mysql.entities.JouerId;
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
        if (movies.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No movies found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
        return Response.ok(movies).build();
    }

    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("title") String title) {
        var movie = em.find(Movie.class, title);
        if(movie == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Movie not found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }

        var jouers = em.createQuery("select j from Jouer j where j.title.title = :title", Jouer.class)
                .setParameter("title", title)
                .getResultList();

        var actors = new ArrayList<Actor>();
        for (Jouer jouer : jouers) {
            actors.add(jouer.getId1());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Movie found");
        response.put("actors", actors);
        response.put("movie", movie);
        return Response.ok(response).build();
    }

    @GET
    @Path("/city")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByCity(@QueryParam("name") String cityName) {
        var moviesByCity = em.createQuery("select m from Movie m where m.city = :cityName", Movie.class)
                .setParameter("cityName", cityName)
                .getResultList();

        if (moviesByCity.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No movies found for city: " + cityName);
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }

        return Response.ok(moviesByCity).build();
    }
}