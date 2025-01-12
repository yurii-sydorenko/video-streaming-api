package com.videostreaming.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.FileSystemResource;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {

    @JsonProperty
    private Long id;

    @JsonProperty
    private FileSystemResource video;

    @JsonProperty
    private VideoMetadataDTO metadata;

}
