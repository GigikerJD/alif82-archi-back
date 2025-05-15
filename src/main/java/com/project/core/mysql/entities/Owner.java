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
    @Size(max = 255)
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "password")
    private String password;

    @Size(max = 255)
    @Column(name = "adresse")
    private String adresse;

    @Size(max = 255)
    @Column(name = "city")
    private String city;

    @Size(max = 255)
    @Column(name = "lastname")
    private String lastname;

    @Size(max = 255)
    @Column(name = "firstname")
    private String firstname;

    @Column(name = "DOB")
    private LocalDate dob;

    @Size(max = 255)
    @Column(name = "movieTheatre")
    private String movieTheatre;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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