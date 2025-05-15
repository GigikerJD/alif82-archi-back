package com.project.core.mysql.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "duration")
    private Integer duration;

    @Size(max = 255)
    @Column(name = "language")
    private String language;

    @Size(max = 255)
    @Column(name = "subtitles")
    private String subtitles;

    @Column(name = "minimumAge")
    private Integer minimumAge;

    @Size(max = 255)
    @Column(name = "director")
    private String director;

    @Column(name = "startingDate")
    private LocalDate startingDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "showDay")
    private LocalTime showDay;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public Integer getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(Integer minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getShowDay() {
        return showDay;
    }

    public void setShowDay(LocalTime showDay) {
        this.showDay = showDay;
    }

}