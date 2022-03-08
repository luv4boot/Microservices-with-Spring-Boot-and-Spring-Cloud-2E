package com.luv4code.api.core.recommendation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecommendationService {


    Mono<Recommendation> createRecommendation(Recommendation body);

    @GetMapping(
            value = "/recommendation",
            produces = "application/json")
    Flux<Recommendation> getRecommendations(
            @RequestParam(value = "productId", required = true) int productId);

    Mono<Void> deleteRecommendations(int productId);

}
