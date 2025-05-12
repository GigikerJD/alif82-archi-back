package com.project.core.mysql.resources;

import com.project.core.mysql.entities.Owner;
import com.project.core.mysql.models.Register;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/register")
public class RegisterResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em = emf.createEntityManager();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Register register) {
        if (register.getEmail() == null || register.getFirstname() == null ||
                register.getLastname() == null || register.getPassword() == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Missing required fields");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }


        Owner existingOwner = em.find(Owner.class, register.getEmail());
        if (existingOwner != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User already exists with this email");
            return Response.status(Response.Status.CONFLICT).entity(response).build();
        }

        try {
            Owner newOwner = new Owner();
            newOwner.setEmail(register.getEmail());
            newOwner.setFirstname(register.getFirstname());
            newOwner.setLastname(register.getLastname());

            if (register.getDOB() != null) {
                newOwner.setDob(register.getDOB());
            }

            em.getTransaction().begin();
            em.persist(newOwner);
            em.getTransaction().commit();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("email", register.getEmail());
            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}