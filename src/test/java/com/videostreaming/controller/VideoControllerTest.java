package com.videostreaming.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.videostreaming.model.dto.VideoDTO;
import com.videostreaming.model.dto.VideoMetadataDTO;
import com.videostreaming.model.dto.VideoStatisticDTO;
import com.videostreaming.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    private static final Long TEST_VIDEO_ID = 1L;
    private static final String TEST_TITLE = "testTitle";

    @Mock
    private VideoService videoService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new VideoController(videoService)).build();
    }

    @Test
    void whenPublishVideoThenResponseIsReturnedSuccessfully() throws Exception {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(TEST_VIDEO_ID);

        when(videoService.upload(any()))
                .thenReturn(videoDTO);

        mockMvc.perform(post("/api/v1/streaming/videos"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    void whenEditVideoThenResponseIsReturnedSuccessfully() throws Exception {
        VideoMetadataDTO videoMetadataDTO = new VideoMetadataDTO();
        videoMetadataDTO.setTitle(TEST_TITLE);

        doNothing()
                .when(videoService).edit(TEST_VIDEO_ID, videoMetadataDTO);

        mockMvc.perform(put("/api/v1/streaming/videos/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(videoMetadataDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void whenLoadVideoThenResponseIsReturnedSuccessfully() throws Exception {
        VideoMetadataDTO videoMetadataDTO = new VideoMetadataDTO();
        videoMetadataDTO.setTitle(TEST_TITLE);

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(TEST_VIDEO_ID);
        videoDTO.setMetadata(videoMetadataDTO);

        when(videoService.get(TEST_VIDEO_ID))
                .thenReturn(videoDTO);

        mockMvc.perform(get("/api/v1/streaming/videos/1/load"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"metadata\":{\"title\":\"testTitle\"}}"));
    }

    @Test
    void whenPlayVideoThenResponseIsReturnedSuccessfully() throws Exception {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(TEST_VIDEO_ID);

        when(videoService.get(TEST_VIDEO_ID))
                .thenReturn(videoDTO);

        mockMvc.perform(get("/api/v1/streaming/videos/1/play"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetVideosWithoutFiltersThenResponseIsReturnedSuccessfully() throws Exception {
        VideoMetadataDTO videoMetadataDTO = new VideoMetadataDTO();
        videoMetadataDTO.setTitle(TEST_TITLE);

        when(videoService.getAll(Collections.emptyMap()))
                .thenReturn(Collections.singletonList(videoMetadataDTO));

        mockMvc.perform(get("/api/v1/streaming/videos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"title\":\"testTitle\"}]"));
    }

    @Test
    void whenGetVideosWithFiltersThenResponseIsReturnedSuccessfully() throws Exception {
        VideoMetadataDTO videoMetadataDTO = new VideoMetadataDTO();
        videoMetadataDTO.setTitle(TEST_TITLE);

        when(videoService.getAll(Map.of("title", TEST_TITLE)))
                .thenReturn(Collections.singletonList(videoMetadataDTO));

        mockMvc.perform(get("/api/v1/streaming/videos?title=testTitle"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"title\":\"testTitle\"}]"));
    }

    @Test
    void whenGetVideoStatisticsThenResponseIsReturnedSuccessfully() throws Exception {
        VideoStatisticDTO videoStatisticDTO = new VideoStatisticDTO();
        videoStatisticDTO.setImpressions(5L);
        videoStatisticDTO.setViews(2L);

        when(videoService.getStatistics(TEST_VIDEO_ID))
                .thenReturn(videoStatisticDTO);

        mockMvc.perform(get("/api/v1/streaming/videos/1/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"impressions\":5,\"views\":2}"));
    }

    @Test
    void whenDeleteVideoThenResponseIsReturnedSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/v1/streaming/videos/1"))
                .andExpect(status().isOk());

        verify(videoService).softDelete(TEST_VIDEO_ID);
    }


}
