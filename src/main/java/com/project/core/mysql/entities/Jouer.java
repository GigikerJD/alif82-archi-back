package com.project.core.mysql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "jouer")
public class Jouer {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_acteur")
    private Acteur idActeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movie")
    private Movie idMovie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Acteur getIdActeur() {
        return idActeur;
    }

    public void setIdActeur(Acteur idActeur) {
        this.idActeur = idActeur;
    }

    public Movie getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(Movie idMovie) {
        this.idMovie = idMovie;
    }

}