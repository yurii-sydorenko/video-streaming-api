package com.videostreaming.util;

import com.videostreaming.model.entity.VideoMetadata;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

/**
 * Class responsible for specification creation.
 * It allows to create filters on any entity column.
 * @author Yurii Sydorenko
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoMetadataFilter {

    public static Specification<VideoMetadata> matches(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = filters.entrySet().stream()
                    .map(filter -> criteriaBuilder.like(root.get(filter.getKey()), filter.getValue()))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.and(predicates);
        };
    }

}
