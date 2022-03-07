package com.luv4code.service.product.services;

import com.luv4code.api.core.product.Product;
import com.luv4code.api.core.product.ProductService;
import com.luv4code.api.exceptions.InvalidInputException;
import com.luv4code.api.exceptions.NotFoundException;
import com.luv4code.service.product.persistence.ProductEntity;
import com.luv4code.service.product.persistence.ProductRepository;
import com.luv4code.service.product.services.mapper.ProductMapper;
import com.luv4code.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ServiceUtil serviceUtil;
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Product getProduct(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        ProductEntity entity = repository.findByProductId(productId).orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

        Product response = mapper.entityToApi(entity);
        response.setServiceAddress(serviceUtil.getServiceAddress());

        log.debug("getProduct: found productId: {}", response.getProductId());

        return response;
    }

    @Override
    public Product createProduct(Product product) {
        try {
            ProductEntity entity = mapper.apiToEntity(product);
            ProductEntity newEntity = repository.save(entity);

            log.debug("createProduct: entity created for productId: {}", product.getProductId());
            return mapper.entityToApi(newEntity);
        } catch (DuplicateKeyException ex) {
            throw new InvalidInputException("Duplicate Key, Product Id: " + product.getProductId());
        }
    }

    @Override
    public void deleteProduct(int productId) {
        log.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        repository.findByProductId(productId).ifPresent(e -> repository.delete(e));
    }
}
