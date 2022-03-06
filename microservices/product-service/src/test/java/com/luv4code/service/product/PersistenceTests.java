package com.luv4code.service.product;

import com.luv4code.service.product.persistence.ProductEntity;
import com.luv4code.service.product.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class PersistenceTests extends MongoDbTestBase {

    @Autowired
    private ProductRepository productRepository;
    private ProductEntity savedEntity;

    @BeforeEach
    void setupDb() {
        productRepository.deleteAll();

        ProductEntity entity = new ProductEntity(1, "n", 1);
        savedEntity = productRepository.save(entity);

        assertEqualsProduct(entity, savedEntity);
    }

    @Test
    void create() {
        ProductEntity newEntity = new ProductEntity(2, "n", 2);
        productRepository.save(newEntity);

        ProductEntity foundEntity = productRepository.findById(newEntity.getId()).get();

        assertEqualsProduct(newEntity, foundEntity);
        assertEquals(2, productRepository.count());
    }

    @Test
    void update() {
        savedEntity.setName("n2");
        productRepository.save(savedEntity);

        ProductEntity foundEntity = productRepository.findById(savedEntity.getId()).get();
        assertEquals(1, (long) foundEntity.getVersion());
        assertEquals("n2", foundEntity.getName());
    }

    @Test
    void delete() {
        productRepository.delete(savedEntity);
        assertFalse(productRepository.existsById(savedEntity.getId()));
    }

    @Test
    void getByProductId() {
        Optional<ProductEntity> entity = productRepository.findByProductId(savedEntity.getProductId());

        assertTrue(entity.isPresent());
        assertEqualsProduct(savedEntity, entity.get());
    }

    //    @Test
    void duplicateError() {
        assertThrows(DuplicateKeyException.class, () -> {
            ProductEntity entity = new ProductEntity(2, "n1", 2);
            productRepository.save(entity);
        });
    }



    private void assertEqualsProduct(ProductEntity expectedEntity, ProductEntity actualEntity) {
        assertEquals(expectedEntity.getId(), actualEntity.getId());
        assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
        assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
        assertEquals(expectedEntity.getName(), actualEntity.getName());
        assertEquals(expectedEntity.getWeight(), actualEntity.getWeight());

    }
}
