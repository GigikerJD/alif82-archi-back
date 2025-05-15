package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Owner;
import com.project.core.mysql.models.Login;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/login")
public class LoginResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em = emf.createEntityManager();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login login) {
        var response = new HashMap<String, Object>();

        try {
            // Utilisation de paramètres nommés pour éviter les erreurs de syntaxe et les problèmes de sécurité
            Owner owner = em.createQuery("SELECT o FROM Owner o WHERE o.email = :email", Owner.class)
                    .setParameter("email", login.getEmail())
                    .getSingleResult();

            // Comparaison des mots de passe (en texte brut pour l'exemple, utilisez le hachage en production)
            if (!owner.getPassword().equals(login.getPassword())) {
                response.put("type", "error");
                response.put("message", "Votre identifiant ou mot de passe est incorrect");
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
            }

            response.put("type", "success");
            response.put("message", "Vous êtes connecté");
            return Response.ok(response).build();
        } catch (NoResultException e) {
            response.put("type", "error");
            response.put("message", "Utilisateur inconnu");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        } catch (Exception e) {
            response.put("type", "error");
            response.put("message", "Une erreur est survenue lors de la tentative de connexion");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
