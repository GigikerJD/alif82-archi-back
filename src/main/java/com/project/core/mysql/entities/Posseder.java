package com.project.core.mysql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "posseder")
public class Posseder {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner")
    private Owner idOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movie")
    private Movie idMovie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Owner getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Owner idOwner) {
        this.idOwner = idOwner;
    }

    public Movie getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(Movie idMovie) {
        this.idMovie = idMovie;
    }

}