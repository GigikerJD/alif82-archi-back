package com.project.core.mysql.resources;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("cities")
public class CityResource {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCities(){
        EntityManager em = emf.createEntityManager();
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
