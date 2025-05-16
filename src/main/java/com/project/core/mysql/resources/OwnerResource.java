package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Acteur;
import com.project.core.mysql.entities.Day;
import com.project.core.mysql.entities.Movie;
import com.project.core.mysql.entities.Owner;
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

    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwner(@PathParam("email") String email) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Owner owner = em.createQuery("select o from Owner o where o.email = :email", Owner.class)
                    .setParameter("email", email)
                    .getSingleResult();
            var response = new HashMap<String, Object>();
            if(owner == null) {
                response.put("type", "error");
                response.put("message", "Owner not found");
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            response.put("type", "success");
            response.put("owner", owner);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving owner: " + e.getMessage())
                    .build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @GET
    @Path("/{email}/movies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies(@PathParam("email") String email) {
        EntityManager em = null;
        Map<String, Object> response = new HashMap<>();
        try {
            em = emf.createEntityManager();

            // Vérifier si le propriétaire existe
            Owner owner = em.createQuery("select o from Owner o where o.email = :email", Owner.class)
                    .setParameter("email", email)
                    .getSingleResult();
            if (owner == null) {
                response.put("type", "error");
                response.put("message", "Owner not found with email: " + email);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }

            // Récupérer tous les films associés à ce propriétaire via la relation Posseder
            List<Movie> movies = em.createQuery(
                            "SELECT m FROM Movie m JOIN Posseder p ON m.title = p.idMovie.title " +
                                    "WHERE p.idOwner.email = :email", Movie.class)
                    .setParameter("email", email)
                    .getResultList();

            if (movies.isEmpty()) {
                response.put("type", "success");
                response.put("message", "No movies found for owner with email: " + email);
                response.put("movies", movies);
                return Response.ok(response).build();
            }

            // Créer une liste pour stocker les détails complets de chaque film
            List<Map<String, Object>> moviesWithDetails = new ArrayList<>();

            // Pour chaque film, récupérer les acteurs et les jours de projection
            for (Movie movie : movies) {
                Map<String, Object> movieDetails = new HashMap<>();
                movieDetails.put("movie", movie);

                // Récupérer les acteurs du film
                List<Acteur> actors = em.createQuery(
                                "SELECT a FROM Acteur a JOIN Jouer j ON a.id = j.idActeur.id " +
                                        "WHERE j.idMovie.title = :title", Acteur.class)
                        .setParameter("title", movie.getTitle())
                        .getResultList();
                movieDetails.put("actors", actors);

                // Récupérer les jours de projection
                List<String> showDays = em.createQuery(
                                "SELECT d.day FROM Day d JOIN Visionner v ON d.id = v.idDay.id " +
                                        "WHERE v.idMovie.title = :title", String.class)
                        .setParameter("title", movie.getTitle())
                        .getResultList();
                movieDetails.put("showDays", showDays);

                // Ajouter la ville du propriétaire
                movieDetails.put("city", owner.getCity());

                // Ajouter l'adresse du cinéma
                movieDetails.put("address", owner.getAdresse());

                // Ajouter le nom du cinéma
                movieDetails.put("movieTheatre", owner.getMovieTheatre());

                moviesWithDetails.add(movieDetails);
            }

            response.put("type", "success");
            response.put("message", "Found " + movies.size() + " movies for owner with email: " + email);
            response.put("moviesDetails", moviesWithDetails);
            response.put("owner", owner);
            return Response.ok(response).build();

        } catch (Exception e) {
            response.put("type", "error");
            response.put("message", "Error retrieving movies for owner: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @GET
    @Path("/{email}/movies/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("email") String email, @PathParam("title") String title) {
        EntityManager em = null;
        Map<String, Object> response = new HashMap<>();
        try{
            em = emf.createEntityManager();
            var owner = em.createQuery("select o from Owner o where o.email = :email", Owner.class)
                    .setParameter("email", email)
                    .getSingleResult();
            if (owner == null) {
                response.put("type", "error");
                response.put("message", "Owner not found with email: " + email);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
            response.put("city", owner.getCity());
            response.put("address", owner.getAdresse());
            response.put("movieTheatre", owner.getMovieTheatre());
            var movie = em.createQuery("select m from Movie m where m.title = :title", Movie.class)
                    .setParameter("title", title)
                    .getSingleResult();
            if(movie == null) {
                response.put("type", "error");
                response.put("message", "Movie not found with title: " + title);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
            List<Acteur> actors = em.createQuery(
                            "SELECT a FROM Acteur a JOIN Jouer j ON a.id = j.idActeur.id " +
                                    "WHERE j.idMovie.title = :title", Acteur.class)
                    .setParameter("title", movie.getTitle())
                    .getResultList();
            List<String> showDays = em.createQuery(
                            "SELECT d.day FROM Day d JOIN Visionner v ON d.id = v.idDay.id " +
                                    "WHERE v.idMovie.title = :title", String.class)
                    .setParameter("title", movie.getTitle())
                    .getResultList();
            response.put("type", "success");
            response.put("actors", actors);
            response.put("movie", movie);
            response.put("showDays", showDays);
            response.put("message", "Found " + actors.size() + " actors for owner with email: " + email);
            return Response.ok(response).build();
        }catch (Exception e){
            response.put("type", "error");
            response.put("message", "Error retrieving movie: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMovie() {
        return Response.ok().build();
    }
}