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
    @Size(max = 100)
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "duration")
    private Integer duration;

    @Size(max = 50)
    @Column(name = "langage", length = 50)
    private String langage;

    @Column(name = "subtitles")
    private Boolean subtitles;

    @Column(name = "minimumAge")
    private Integer minimumAge;

    @Size(max = 100)
    @Column(name = "director", length = 100)
    private String director;

    @Column(name = "startingDate")
    private LocalDate startingDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Size(max = 100)
    @Column(name = "city", length = 100)
    private String city;

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

    public String getLangage() {
        return langage;
    }

    public void setLangage(String langage) {
        this.langage = langage;
    }

    public Boolean getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(Boolean subtitles) {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalTime getShowDay() {
        return showDay;
    }

    public void setShowDay(LocalTime showDay) {
        this.showDay = showDay;
    }

}