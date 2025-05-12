package com.project.core.mysql.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "owner")
public class Owner {
    @Id
    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 100)
    @Column(name = "adresse", length = 100)
    private String adresse;

    @Size(max = 100)
    @Column(name = "lastname", length = 100)
    private String lastname;

    @Size(max = 100)
    @Column(name = "firstname", length = 100)
    private String firstname;

    @Column(name = "DOB")
    private LocalDate dob;

    @Size(max = 100)
    @Column(name = "movieTheatre", length = 100)
    private String movieTheatre;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getMovieTheatre() {
        return movieTheatre;
    }

    public void setMovieTheatre(String movieTheatre) {
        this.movieTheatre = movieTheatre;
    }

}