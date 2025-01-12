package com.videostreaming.service;

import com.videostreaming.model.dto.VideoDTO;
import com.videostreaming.model.dto.VideoMetadataDTO;
import com.videostreaming.model.dto.VideoStatisticDTO;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface VideoService {

    VideoDTO upload(MultipartFile file) throws IOException;

    VideoDTO get(Long videoId);

    VideoStatisticDTO getStatistics(Long videoId);

    List<VideoMetadataDTO> getAll(@Nullable Map<String, String> filters);

    void edit(Long videoId, VideoMetadataDTO videoMetadataDTO);

    void softDelete(Long videoId);
}
