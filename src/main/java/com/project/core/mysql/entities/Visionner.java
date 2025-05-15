package com.project.core.mysql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "visionner")
public class Visionner {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movie")
    private Movie idMovie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_day")
    private Day idDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Movie getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(Movie idMovie) {
        this.idMovie = idMovie;
    }

    public Day getIdDay() {
        return idDay;
    }

    public void setIdDay(Day idDay) {
        this.idDay = idDay;
    }

}