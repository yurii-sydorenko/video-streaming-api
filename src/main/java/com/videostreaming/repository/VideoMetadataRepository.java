package com.videostreaming.repository;

import com.videostreaming.model.entity.VideoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, Long>, JpaSpecificationExecutor<VideoMetadata> {

    Optional<VideoMetadata> findByVideoIdAndVideoDeletedIsFalse(Long videoId);

}
