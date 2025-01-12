package com.videostreaming.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity
@NoArgsConstructor
public class VideoMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "video_id")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Video video;

    @Column
    private String title;

    @Column
    private String synopsis;

    @Column
    private String author;

    @Column
    private Integer releaseYear;

    @Column
    private String genre;

    @Column
    private Long runningTime;

}
