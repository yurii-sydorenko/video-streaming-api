package com.videostreaming.service;

import com.videostreaming.model.dto.VideoDTO;
import com.videostreaming.model.dto.VideoMetadataDTO;
import com.videostreaming.model.dto.VideoStatisticDTO;
import com.videostreaming.model.entity.Video;
import com.videostreaming.model.entity.VideoMetadata;
import com.videostreaming.model.entity.VideoStatistic;
import com.videostreaming.repository.VideoMetadataRepository;
import com.videostreaming.repository.VideoRepository;
import com.videostreaming.repository.VideoStatisticRepository;
import com.videostreaming.service.impl.VideoServiceImpl;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    private static final Long TEST_VIDEO_ID = 1L;
    private static final Long TEST_METADATA_ID = 2L;
    private static final String TEST_FILE_NAME = "testFileName";
    private static final String TEST_VIDEO_TITLE = "testVideoTitle";
    private static final String TEST_NEW_VIDEO_TITLE = "testNewVideoTitle";
    private static final Long TEST_IMPRESSIONS = 10L;
    private static final Long TEST_VIEWS = 5L;

    @Mock
    private VideoStorage videoStorage;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private VideoMetadataRepository videoMetadataRepository;
    @Mock
    private VideoStatisticRepository videoStatisticRepository;

    private final FileSystemResource fileSystemResource = new FileSystemResource(TEST_FILE_NAME);

    @InjectMocks
    private VideoServiceImpl videoService;

    @Test
    void whenUploadAndStorageDoesNotThrowException_thenVideoShouldBeSaved() {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

        doNothing()
                .when(videoStorage).save(multipartFile);
        when(videoRepository.save(any()))
                .thenReturn(createVideo());

        VideoDTO videoDTO = videoService.upload(multipartFile);

        VideoDTO expected = new VideoDTO();
        expected.setId(TEST_VIDEO_ID);

        assertThat(videoDTO).isEqualTo(expected);
    }

    @Test
    void whenGetAndVideoIsNotFound_thenExceptionIsThrown() {
        when(videoRepository.findByIdAndDeletedIsFalse(TEST_VIDEO_ID))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> videoService.get(TEST_VIDEO_ID));
    }

    @Test
    void whenGetAndVideoIsFound_thenResponseIsMappedSuccessfully() {
        when(videoRepository.findByIdAndDeletedIsFalse(TEST_VIDEO_ID))
                .thenReturn(Optional.of(createVideo()));
        when(videoStorage.get(TEST_FILE_NAME))
                .thenReturn(fileSystemResource);

        VideoDTO videoDTO = videoService.get(TEST_VIDEO_ID);

        VideoMetadataDTO expectedMetadata = new VideoMetadataDTO();
        expectedMetadata.setTitle(TEST_VIDEO_TITLE);
        VideoDTO expected = new VideoDTO(TEST_VIDEO_ID, fileSystemResource, expectedMetadata);

        assertThat(videoDTO).isEqualTo(expected);
    }

    @Test
    void whenGetStatisticsAndVideoIsNotFound_thenExceptionIsThrown() {
        when(videoStatisticRepository.findByVideoIdAndVideoDeletedIsFalse(TEST_VIDEO_ID))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> videoService.getStatistics(TEST_VIDEO_ID));
    }


    @Test
    void whenGetStatisticsAndVideoIsFound_thenResponseIsMappedSuccessfully() {
        when(videoStatisticRepository.findByVideoIdAndVideoDeletedIsFalse(TEST_VIDEO_ID))
                .thenReturn(Optional.of(createVideoStatistic()));

        VideoStatisticDTO statistics = videoService.getStatistics(TEST_VIDEO_ID);

        VideoStatisticDTO expected = new VideoStatisticDTO();
        expected.setImpressions(TEST_IMPRESSIONS);
        expected.setViews(TEST_VIEWS);

        assertThat(statistics).isEqualTo(expected);
    }

    @Test
    void whenGetAllAndFiltersAreNotEmpty_thenFindAllWithSpecificationIsCalledAndResponseIsMappedSuccessfully() {
        VideoMetadataDTO videoMetadataDTO = new VideoMetadataDTO();
        videoMetadataDTO.setTitle(TEST_NEW_VIDEO_TITLE);

        when(videoMetadataRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(createVideoMetadata()));

        List<VideoMetadataDTO> videosMetadata = videoService.getAll(Map.of("title", TEST_VIDEO_TITLE));

        VideoMetadataDTO expected = new VideoMetadataDTO();
        expected.setTitle(TEST_VIDEO_TITLE);

        assertThat(videosMetadata).isEqualTo(Collections.singletonList(expected));
    }

    @Test
    void whenGetAllAndFiltersAreEmpty_thenFindAllIsCalledAndResponseIsMappedSuccessfully() {
        VideoMetadataDTO videoMetadataDTO = new VideoMetadataDTO();
        videoMetadataDTO.setTitle(TEST_NEW_VIDEO_TITLE);

        when(videoMetadataRepository.findAll())
                .thenReturn(List.of(createVideoMetadata()));

        List<VideoMetadataDTO> videosMetadata = videoService.getAll(Collections.emptyMap());

        VideoMetadataDTO expected = new VideoMetadataDTO();
        expected.setTitle(TEST_VIDEO_TITLE);

        assertThat(videosMetadata).isEqualTo(Collections.singletonList(expected));
    }

    @Test
    void whenEditAndVideoIsNotFound_thenExceptionIsThrown() {
        VideoMetadataDTO videoMetadataDTO = new VideoMetadataDTO();
        videoMetadataDTO.setTitle(TEST_NEW_VIDEO_TITLE);

        when(videoMetadataRepository.findByVideoIdAndVideoDeletedIsFalse(TEST_VIDEO_ID))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> videoService.edit(TEST_VIDEO_ID, videoMetadataDTO));
    }

    @Test
    void whenEditAndVideoIsFound_thenResponseIsMappedSuccessfully() {
        VideoMetadataDTO videoMetadataDTO = new VideoMetadataDTO();
        videoMetadataDTO.setTitle(TEST_NEW_VIDEO_TITLE);

        when(videoMetadataRepository.findByVideoIdAndVideoDeletedIsFalse(TEST_VIDEO_ID))
                .thenReturn(Optional.of(createVideoMetadata()));

        videoService.edit(TEST_VIDEO_ID, videoMetadataDTO);

        VideoMetadata expectedMetadata = new VideoMetadata();
        expectedMetadata.setId(TEST_METADATA_ID);
        expectedMetadata.setTitle(TEST_NEW_VIDEO_TITLE);
        Video expectedVideo = new Video();
        expectedVideo.setId(TEST_VIDEO_ID);
        expectedMetadata.setVideo(expectedVideo);

        verify(videoMetadataRepository).save(expectedMetadata);
    }

    private Video createVideo() {
        Video video = new Video();

        video.setId(TEST_VIDEO_ID);
        video.setFileName(TEST_FILE_NAME);
        video.setMetadata(createVideoMetadata());

        return video;
    }

    private VideoStatistic createVideoStatistic() {
        VideoStatistic videoStatistic = new VideoStatistic();

        videoStatistic.setImpressions(TEST_IMPRESSIONS);
        videoStatistic.setViews(TEST_VIEWS);

        return videoStatistic;
    }

    private VideoMetadata createVideoMetadata() {
        Video video = new Video();
        video.setId(TEST_VIDEO_ID);

        VideoMetadata videoMetadata = new VideoMetadata();

        videoMetadata.setId(TEST_METADATA_ID);
        videoMetadata.setTitle(TEST_VIDEO_TITLE);
        videoMetadata.setVideo(video);

        return videoMetadata;
    }

}
