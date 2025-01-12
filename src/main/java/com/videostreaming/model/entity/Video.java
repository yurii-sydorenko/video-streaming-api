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
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @JoinColumn(name = "metadata_id")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private VideoMetadata metadata;

    @JoinColumn(name = "statistic_id")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private VideoStatistic statistic;

    @Column
    private boolean deleted;

}
