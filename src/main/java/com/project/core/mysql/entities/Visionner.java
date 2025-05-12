package com.project.core.mysql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "visionner")
public class Visionner {
    @EmbeddedId
    private VisionnerId id;

    @MapsId("title")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title", nullable = false)
    private Movie title;

    @MapsId("id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Day id1;

    public VisionnerId getId() {
        return id;
    }

    public void setId(VisionnerId id) {
        this.id = id;
    }

    public Movie getTitle() {
        return title;
    }

    public void setTitle(Movie title) {
        this.title = title;
    }

    public Day getId1() {
        return id1;
    }

    public void setId1(Day id1) {
        this.id1 = id1;
    }

}