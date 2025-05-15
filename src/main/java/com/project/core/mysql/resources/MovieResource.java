package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Acteur;
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

    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("title") String title) {
        Movie movie = em.find(Movie.class, title);

        if (movie == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Movie not found");
            errorResponse.put("status", 404);
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }

        // Get actors for this movie using the Jouer join table
        List<Acteur> actors = em.createQuery(
                        "SELECT a FROM Acteur a JOIN Jouer j ON a.id = j.idActeur.id " +
                                "WHERE j.idMovie.title = :title", Acteur.class)
                .setParameter("title", title)
                .getResultList();

        // Create a response with both movie details and actors
        Map<String, Object> response = new HashMap<>();
        response.put("movie", movie);
        response.put("actors", actors);
        response.put("status", 200);

        return Response.ok(response).build();
    }

    @GET
    @Path("/city")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByCity(@QueryParam("name") String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "City name is required");
            errorResponse.put("status", 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        // Find movies where the owner's movie theater is in the specified city
        // Assuming the address might contain the city name
        List<Movie> movies = em.createQuery(
                        "SELECT DISTINCT m FROM Movie m JOIN Posseder p ON m.title = p.idMovie.title " +
                                "JOIN Owner o ON p.idOwner.email = o.email " +
                                "WHERE o.adresse LIKE :cityPattern", Movie.class)
                .setParameter("cityPattern", "%" + cityName + "%")
                .getResultList();

        Map<String, Object> response = new HashMap<>();
        if (movies.isEmpty()) {
            response.put("message", "No movies found in " + cityName);
            response.put("status", 404);
        } else {
            response.put("movies", movies);
            response.put("count", movies.size());
            response.put("message", "Found " + movies.size() + " movies in " + cityName);
            response.put("status", 200);
        }

        return Response.ok(response).build();
    }
}