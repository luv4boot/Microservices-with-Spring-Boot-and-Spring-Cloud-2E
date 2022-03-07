package com.luv4code.service.review.services.mappers;

import com.luv4code.api.core.review.Review;
import com.luv4code.service.review.persistence.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    ReviewEntity apiToEntity(Review body);

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
    })
    Review entityToApi(ReviewEntity newEntity);

    List<Review> entityListToApiList(List<ReviewEntity> entityList);
    List<ReviewEntity> apiListToEntityList(List<Review> api);
}
