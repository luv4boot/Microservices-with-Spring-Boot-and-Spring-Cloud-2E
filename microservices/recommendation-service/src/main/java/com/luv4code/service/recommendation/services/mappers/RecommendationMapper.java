package com.luv4code.service.recommendation.services.mappers;

import com.luv4code.api.core.recommendation.Recommendation;
import com.luv4code.service.recommendation.persistence.RecommendationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mappings({
            @Mapping(target = "rating", source = "api.rate"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    RecommendationEntity apiToEntity(Recommendation api);

    @Mappings({
            @Mapping(target = "rate", source = "entity.rating"),
            @Mapping(target = "serviceAddress", ignore = true)
    })
    Recommendation entityToApi(RecommendationEntity entity);

    List<Recommendation> entityListToApiList(List<RecommendationEntity> entityList);

    List<RecommendationEntity> apiListToEntityList(List<Recommendation> api);
}
