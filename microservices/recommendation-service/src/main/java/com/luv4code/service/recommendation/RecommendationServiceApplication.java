package com.luv4code.service.recommendation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;

@SpringBootApplication
@ComponentScan("com.luv4code")
@Slf4j
public class RecommendationServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(RecommendationServiceApplication.class, args);
        String mongoDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
        String mongoDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
        log.info("Connected to MongoDb: " + mongoDbHost + ":" + mongoDbPort);
    }

    @Autowired
    MongoOperations mongoTemplate;

    /*@EventListener(ContextRefreshedEvent.class)
    public void initIndicesAfterStartup() {

        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoTemplate.getConverter().getMappingContext();
        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);

        IndexOperations indexOps = mongoTemplate.indexOps(RecommendationEntity.class);
        resolver.resolveIndexFor(RecommendationEntity.class).forEach(e -> indexOps.ensureIndex(e));
    }*/
}

