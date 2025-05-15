package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Acteur;
import com.project.core.mysql.entities.Movie;
import com.project.core.mysql.entities.Owner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/movies")
public class MovieResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies() {
        EntityManager em = null;
        Map<String, Object> response = new HashMap<>();
        try {
            em = emf.createEntityManager();
            List<Movie> movies = em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();

            if (movies.isEmpty()) {
                response.put("type", "error");
                response.put("message", "No movies found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
            response.put("type", "success");
            response.put("message", "Found " + movies.size() + " movies");
            return Response.ok(movies).build();
        } catch (Exception e) {
            response.put("message", "Error retrieving movies: " + e.getMessage());
            response.put("type", "error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("title") String title) {
        EntityManager em = null;
        Map<String, Object> response = new HashMap<>();
        try {
            em = emf.createEntityManager();
            Movie movie = em.find(Movie.class, title);

            if (movie == null) {
                response.put("message", "Movie not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }

            List<Acteur> actors = em.createQuery(
                            "SELECT a FROM Acteur a JOIN Jouer j ON a.id = j.idActeur.id " +
                                    "WHERE j.idMovie.title = :title", Acteur.class)
                    .setParameter("title", title)
                    .getResultList();

            response.put("movie", movie);
            response.put("actors", actors);
            return Response.ok(response).build();
        } catch (Exception e) {
            response.put("message", "Error retrieving movie: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @GET
    @Path("/city")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByCity(@QueryParam("name") String cityName) {
        EntityManager em = null;
        Map<String, Object> response = new HashMap<>();
        try {
            em = emf.createEntityManager();
            if (cityName == null || cityName.isEmpty()) {
                response.put("message", "City name is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            List<Movie> movies = em.createQuery(
                            "SELECT DISTINCT m FROM Movie m JOIN Posseder p ON m.title = p.idMovie.title " +
                                    "JOIN Owner o ON p.idOwner.email = o.email " +
                                    "WHERE o.city LIKE :cityPattern", Movie.class)
                    .setParameter("cityPattern", "%" + cityName + "%")
                    .getResultList();

            if (movies.isEmpty()) {
                response.put("type", "error");
                response.put("message", "No movies found in " + cityName);
            } else {
                response.put("type", "success");
                response.put("movies", movies);
                response.put("count", movies.size());
                response.put("message", "Found " + movies.size() + " movies in " + cityName);
            }

            return Response.ok(response).build();
        } catch (Exception e) {
            response.put("message", "Error retrieving movies by city: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @GET
    @Path("cities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCities(){
        EntityManager em = null;
        Map<String, Object> response = new HashMap<>();
        try{
            em = emf.createEntityManager();
            var cities = em.createQuery("select distinct(city) from Owner", String.class).getResultList();
            if(cities.isEmpty()){
                response.put("type", "error");
                response.put("message", "No owners found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
            response.put("type", "success");
            response.put("cities", cities);
            return Response.ok(response).build();
        }catch (Exception e){
            response.put("message", "Error retrieving owners: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
