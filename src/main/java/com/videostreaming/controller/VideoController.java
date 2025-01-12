package com.videostreaming.controller;

import com.videostreaming.model.dto.VideoDTO;
import com.videostreaming.model.dto.VideoMetadataDTO;
import com.videostreaming.model.dto.VideoStatisticDTO;
import com.videostreaming.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/streaming/videos")
public class VideoController {

    private final VideoService videoService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoDTO> publishVideo(MultipartFile file) throws IOException {
        return ResponseEntity.ok(videoService.upload(file));
    }

    @PutMapping(value = "/{videoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> editVideo(@PathVariable("videoId") Long videoId, @RequestBody VideoMetadataDTO videoMetadataDTO) {
        videoService.edit(videoId, videoMetadataDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{videoId}/load", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoDTO> loadVideo(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.get(videoId));
    }

    @GetMapping(value = "/{videoId}/play", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileSystemResource> playVideo(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.get(videoId).getVideo());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VideoMetadataDTO>> getVideos(@RequestParam(required = false) Map<String, String> filters) {
        return ResponseEntity.ok(videoService.getAll(filters));
    }

    @GetMapping(value = "/{videoId}/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VideoStatisticDTO> getVideoStatistics(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.getStatistics(videoId));
    }

    @DeleteMapping(value = "/{videoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoService.softDelete(videoId);
        return ResponseEntity.ok().build();
    }

}
