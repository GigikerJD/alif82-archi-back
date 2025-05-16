package com.project.core.mysql.resources;

import com.project.core.mysql.entities.*;
import com.project.core.mysql.models.MovieForm;
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

            // Créer une liste pour stocker les films avec leurs informations de ville
            List<Map<String, Object>> moviesWithCities = new ArrayList<>();

            // Pour chaque film, récupérer les villes associées
            for (Movie movie : movies) {
                Map<String, Object> movieInfo = new HashMap<>();
                movieInfo.put("movie", movie);

                // Récupérer les villes associées à ce film via la relation Posseder
                List<String> cities = em.createQuery(
                                "SELECT DISTINCT o.city FROM Owner o JOIN Posseder p ON o.email = p.idOwner.email " +
                                        "WHERE p.idMovie.title = :title", String.class)
                        .setParameter("title", movie.getTitle())
                        .getResultList();

                movieInfo.put("cities", cities);
                moviesWithCities.add(movieInfo);
            }

            response.put("type", "success");
            response.put("message", "Found " + movies.size() + " movies");
            response.put("data", moviesWithCities);
            return Response.ok(response).build();
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

            // Récupérer les acteurs
            List<Acteur> actors = em.createQuery(
                            "SELECT a FROM Acteur a JOIN Jouer j ON a.id = j.idActeur.id " +
                                    "WHERE j.idMovie.title = :title", Acteur.class)
                    .setParameter("title", title)
                    .getResultList();

            // Récupérer les jours de projection
            List<String> showDays = em.createQuery(
                            "SELECT d.day FROM Day d JOIN Visionner v ON d.id = v.idDay.id " +
                                    "WHERE v.idMovie.title = :title", String.class)
                    .setParameter("title", movie.getTitle())
                    .getResultList();

            // Récupérer les informations des cinémas (adresse, ville, nom)
            List<Map<String, Object>> theaters = em.createQuery(
                            "SELECT o.adresse, o.city, o.movieTheatre FROM Owner o JOIN Posseder p ON o.email = p.idOwner.email " +
                                    "WHERE p.idMovie.title = :title", Object[].class)
                    .setParameter("title", movie.getTitle())
                    .getResultStream()
                    .map(result -> {
                        Map<String, Object> theater = new HashMap<>();
                        theater.put("address", result[0]);
                        theater.put("city", result[1]);
                        theater.put("name", result[2]);
                        return theater;
                    })
                    .toList();

            response.put("movie", movie);
            response.put("actors", actors);
            response.put("showDays", showDays);
            response.put("theaters", theaters);
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMovie(MovieForm movieForm) {
        EntityManager em = null;
        Map<String, Object> response = new HashMap<>();

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            // Validation des données requises
            if (movieForm.getTitle() == null || movieForm.getTitle().trim().isEmpty()) {
                response.put("type", "error");
                response.put("message", "Movie title is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            if (movieForm.getEmailOwner() == null || movieForm.getEmailOwner().trim().isEmpty()) {
                response.put("type", "error");
                response.put("message", "Owner email is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            // Vérifier si le film existe déjà
            Movie existingMovie = em.find(Movie.class, movieForm.getTitle());
            if (existingMovie != null) {
                response.put("type", "error");
                response.put("message", "Movie with this title already exists");
                return Response.status(Response.Status.CONFLICT).entity(response).build();
            }

            // Vérifier si le propriétaire existe
            Owner owner = em.find(Owner.class, movieForm.getEmailOwner());
            if (owner == null) {
                response.put("type", "error");
                response.put("message", "Owner not found with email: " + movieForm.getEmailOwner());
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }

            // Créer le film
            Movie movie = new Movie();
            movie.setTitle(movieForm.getTitle());
            movie.setDuration(movieForm.getDuration());
            movie.setDirector(movieForm.getDirector());
            movie.setLanguage(movieForm.getLanguage());
            movie.setSubtitles(movieForm.getSubtitles());
            movie.setStartingDate(movieForm.getStartingDate());
            movie.setEndDate(movieForm.getEndDate());
            movie.setMinimumAge(movieForm.getMinimumAge());
            movie.setShowDay(movieForm.getShowDay());

            em.persist(movie);

            // Créer la relation Posseder (Owner possède le Movie)
            Posseder posseder = new Posseder();
            // Générer un ID pour la relation Posseder
            Integer maxId = em.createQuery("SELECT MAX(p.id) FROM Posseder p", Integer.class).getSingleResult();
            posseder.setId(maxId != null ? maxId + 1 : 1);
            posseder.setIdOwner(owner);
            posseder.setIdMovie(movie);
            em.persist(posseder);

            // Créer les relations avec les acteurs
            if (movieForm.getActeurs() != null && !movieForm.getActeurs().isEmpty()) {
                Integer maxJouerId = em.createQuery("SELECT MAX(j.id) FROM Jouer j", Integer.class).getSingleResult();
                int currentJouerId = maxJouerId != null ? maxJouerId + 1 : 1;

                for (Acteur acteur : movieForm.getActeurs()) {
                    // Vérifier si l'acteur existe
                    Acteur existingActeur = em.find(Acteur.class, acteur.getId());
                    if (existingActeur != null) {
                        Jouer jouer = new Jouer();
                        jouer.setId(currentJouerId++);
                        jouer.setIdActeur(existingActeur);
                        jouer.setIdMovie(movie);
                        em.persist(jouer);
                    }
                }
            }

            // Créer les relations avec les jours de projection
            if (movieForm.getDays() != null && !movieForm.getDays().isEmpty()) {
                Integer maxVisionnerId = em.createQuery("SELECT MAX(v.id) FROM Visionner v", Integer.class).getSingleResult();
                int currentVisionnerId = maxVisionnerId != null ? maxVisionnerId + 1 : 1;

                for (Day day : movieForm.getDays()) {
                    // Vérifier si le jour existe
                    Day existingDay = em.find(Day.class, day.getId());
                    if (existingDay != null) {
                        Visionner visionner = new Visionner();
                        visionner.setId(currentVisionnerId++);
                        visionner.setIdMovie(movie);
                        visionner.setIdDay(existingDay);
                        em.persist(visionner);
                    }
                }
            }

            em.getTransaction().commit();

            response.put("type", "success");
            response.put("message", "Movie created successfully");
            response.put("movie", movie);
            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            response.put("type", "error");
            response.put("message", "Error creating movie: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
