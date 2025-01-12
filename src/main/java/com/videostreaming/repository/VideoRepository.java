package com.videostreaming.repository;

import com.videostreaming.model.entity.Video;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findByIdAndDeletedIsFalse(Long id);

    @Modifying
    @Transactional
    @Query("update Video v set v.deleted = true where v.id = :id")
    void softDeleteById(@Param(value = "id") Long id);

}
