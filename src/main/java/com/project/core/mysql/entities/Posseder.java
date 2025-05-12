package com.project.core.mysql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "posseder")
public class Posseder {
    @EmbeddedId
    private PossederId id;

    @MapsId("email")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email", nullable = false)
    private Owner email;

    @MapsId("title")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title", nullable = false)
    private Movie title;

    public PossederId getId() {
        return id;
    }

    public void setId(PossederId id) {
        this.id = id;
    }

    public Owner getEmail() {
        return email;
    }

    public void setEmail(Owner email) {
        this.email = email;
    }

    public Movie getTitle() {
        return title;
    }

    public void setTitle(Movie title) {
        this.title = title;
    }

}