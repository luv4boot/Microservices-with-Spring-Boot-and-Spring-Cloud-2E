package com.luv4code.service.review.services;

import com.luv4code.api.core.review.Review;
import com.luv4code.api.core.review.ReviewService;
import com.luv4code.api.exceptions.InvalidInputException;
import com.luv4code.service.review.persistence.ReviewEntity;
import com.luv4code.service.review.persistence.ReviewRepository;
import com.luv4code.service.review.services.mappers.ReviewMapper;
import com.luv4code.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static java.util.logging.Level.FINE;

@RestController
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ServiceUtil serviceUtil;
    private final ReviewRepository repository;
    private final ReviewMapper mapper;
    private final Scheduler jdbcScheduler;

    @Autowired
    public ReviewServiceImpl(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
        this.repository = repository;
        this.mapper = mapper;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Override
    public Mono<Review> createReview(Review body) {
        if (body.getProductId() < 1)
            throw new InvalidInputException("Invalid ProductId: " + body.getProductId());
        return Mono.fromCallable(() -> internalCreateReview(body))
                .subscribeOn(jdbcScheduler);
    }

    private Review internalCreateReview(Review body) {
        try {
            ReviewEntity entity = mapper.apiToEntity(body);
            ReviewEntity newEntity = repository.save(entity);

            log.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
            return mapper.entityToApi(newEntity);

        } catch (DataIntegrityViolationException dive) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
        }
    }

    @Override
    public Flux<Review> getReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        log.info("Will get reviews for product with id={}", productId);

        return Mono.fromCallable(() -> internalGerReviews(productId))
                .flatMapMany(Flux::fromIterable)
                .log(log.getName(), FINE)
                .subscribeOn(jdbcScheduler);
    }

    private List<Review> internalGerReviews(int productId) {
        List<ReviewEntity> entityList = repository.findByProductId(productId);
        List<Review> list = mapper.entityListToApiList(entityList);
        list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

        log.debug("getReviews response size: {}", list.size());
        return list;
    }


    @Override
    public Mono<Void> deleteReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        return Mono.fromRunnable(() -> internalDeleteReviews(productId)).subscribeOn(jdbcScheduler).then();
    }

    private void internalDeleteReviews(int productId) {
        log.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }
}
