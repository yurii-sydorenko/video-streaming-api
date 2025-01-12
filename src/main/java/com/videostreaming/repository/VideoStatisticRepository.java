package com.videostreaming.repository;

import com.videostreaming.model.entity.VideoStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface VideoStatisticRepository extends JpaRepository<VideoStatistic, Long> {

    Optional<VideoStatistic> findByVideoIdAndVideoDeletedIsFalse(Long videoId);

    @Modifying
    @Transactional
    @Query("update VideoStatistic vs set vs.impressions = (vs.impressions + 1) where vs.video.id = :videoId")
    void incrementImpressions(@Param("videoId") Long videoId);

    @Modifying
    @Transactional
    @Query("update VideoStatistic vs set vs.views = (vs.views + 1) where vs.video.id = :videoId")
    void incrementViews(@Param("videoId") Long videoId);

}
