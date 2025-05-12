package com.project.core.mysql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "jouer")
public class Jouer {
    @EmbeddedId
    private JouerId id;

    @MapsId("id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Actor id1;

    @MapsId("title")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title", nullable = false)
    private Movie title;

    public JouerId getId() {
        return id;
    }

    public void setId(JouerId id) {
        this.id = id;
    }

    public Actor getId1() {
        return id1;
    }

    public void setId1(Actor id1) {
        this.id1 = id1;
    }

    public Movie getTitle() {
        return title;
    }

    public void setTitle(Movie title) {
        this.title = title;
    }

}