package com.videostreaming.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoMetadataDTO {

    private String title;

    private String synopsis;

    private String author;

    private Integer releaseYear;

    private String genre;

    private Long runningTime;

}
