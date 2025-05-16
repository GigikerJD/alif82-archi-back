package com.project.core.mysql.resources;


import com.project.core.mysql.entities.Day;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;

@Path("/days")
public class DayResource {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private EntityManager em = emf.createEntityManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDays() {
        var days = em.createQuery("select d from Day d", Day.class).getResultList();
        var resp = new HashMap<String, Object>();
        resp.put("days", days);
        return Response.ok(resp).build();

    }
}
