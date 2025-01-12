package com.videostreaming.aspect;

import com.videostreaming.repository.VideoStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect responsible for impressions/views incrementation.
 * It frees service/controller from additional calculation and do incrementation in the background.
 * @author Yurii Sydorenko
 */
@Aspect
@Component
@RequiredArgsConstructor
public class StatisticsAspect {

    private final VideoStatisticRepository videoStatisticRepository;

    @After(value = "execution(* com.videostreaming.controller.VideoController.loadVideo(..)) && args (videoId)", argNames = "videoId")
    public void updateImpressions(Long videoId) {
        videoStatisticRepository.incrementImpressions(videoId);
    }

    @After(value = "execution(* com.videostreaming.controller.VideoController.playVideo(..)) && args (videoId)", argNames = "videoId")
    public void updateViews(Long videoId) {
        videoStatisticRepository.incrementViews(videoId);
    }

}
