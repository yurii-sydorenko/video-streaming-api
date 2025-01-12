package com.videostreaming.service.impl;

import com.videostreaming.model.dto.VideoDTO;
import com.videostreaming.model.dto.VideoMetadataDTO;
import com.videostreaming.model.dto.VideoStatisticDTO;
import com.videostreaming.model.entity.Video;
import com.videostreaming.model.entity.VideoMetadata;
import com.videostreaming.model.entity.VideoStatistic;
import com.videostreaming.repository.VideoMetadataRepository;
import com.videostreaming.repository.VideoRepository;
import com.videostreaming.repository.VideoStatisticRepository;
import com.videostreaming.service.VideoService;
import com.videostreaming.service.VideoStorage;
import com.videostreaming.util.VideoMetadataFilter;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Service responsible for processing video related requests via a storage and repositories.
 * @author Yurii Sydorenko
 */
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private static final String VIDEO = "Video";

    private final VideoStorage videoStorage;
    private final VideoRepository videoRepository;
    private final VideoMetadataRepository videoMetadataRepository;
    private final VideoStatisticRepository videoStatisticRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public VideoDTO upload(MultipartFile file) {
        videoStorage.save(file);

        Video video = new Video();
        video.setFileName(file.getOriginalFilename());

        VideoMetadata videoMetadata = new VideoMetadata();
        videoMetadata.setVideo(video);
        video.setMetadata(videoMetadata);

        VideoStatistic videoStatistic = new VideoStatistic();
        videoStatistic.setVideo(video);
        video.setStatistic(videoStatistic);

        Video saved = videoRepository.save(video);

        return VideoDTO.builder()
                .id(saved.getId())
                .build();
    }

    @Override
    public VideoDTO get(Long videoId) {
        Video video = videoRepository.findByIdAndDeletedIsFalse(videoId)
                .orElseThrow(() -> new ObjectNotFoundException(videoId, VIDEO));

        return VideoDTO.builder()
                .id(video.getId())
                .video(videoStorage.get(video.getFileName()))
                .metadata(modelMapper.map(video.getMetadata(), VideoMetadataDTO.class))
                .build();
    }

    @Override
    public VideoStatisticDTO getStatistics(Long videoId) {
        return videoStatisticRepository.findByVideoIdAndVideoDeletedIsFalse(videoId)
                .map(statistic -> modelMapper.map(statistic, VideoStatisticDTO.class))
                .orElseThrow(() -> new ObjectNotFoundException(videoId, VIDEO));
    }

    @Override
    public List<VideoMetadataDTO> getAll(@Nullable Map<String, String> filters) {
        List<VideoMetadata> videosMetadata = Optional.ofNullable(filters)
                .filter(Predicate.not(Map::isEmpty))
                .map(f -> videoMetadataRepository.findAll(VideoMetadataFilter.matches(filters)))
                .orElseGet(videoMetadataRepository::findAll);

        return videosMetadata.stream()
                .filter(Predicate.not(metadata -> metadata.getVideo().isDeleted()))
                .map(metadata -> modelMapper.map(metadata, VideoMetadataDTO.class))
                .toList();
    }

    @Override
    public void edit(Long videoId, VideoMetadataDTO videoMetadataDTO) {
        VideoMetadata savedMetadata = videoMetadataRepository.findByVideoIdAndVideoDeletedIsFalse(videoId)
                .orElseThrow(() -> new ObjectNotFoundException(videoId, VIDEO));

        VideoMetadata newMetadata = modelMapper.map(videoMetadataDTO, VideoMetadata.class);
        newMetadata.setId(savedMetadata.getId());
        newMetadata.setVideo(savedMetadata.getVideo());

        videoMetadataRepository.save(newMetadata);
    }

    @Override
    public void softDelete(Long videoId) {
        videoRepository.softDeleteById(videoId);
    }

}
