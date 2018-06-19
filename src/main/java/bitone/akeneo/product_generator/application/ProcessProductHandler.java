package bitone.akeneo.product_generator.application;

import bitone.akeneo.product_generator.domain.generator.ProductGenerator;
import bitone.akeneo.product_generator.domain.model.ProductRepository;
import bitone.akeneo.product_generator.domain.model.Product;
import bitone.akeneo.product_generator.domain.exception.NoFamilyDefinedException;
import bitone.akeneo.product_generator.domain.exception.NoChildrenCategoryDefinedException;
import bitone.akeneo.product_generator.domain.exception.RepositoryException;
import bitone.akeneo.product_generator.domain.exception.AttributeNotFoundException;
import bitone.akeneo.product_generator.domain.exception.FamilyNotFoundException;
import bitone.akeneo.product_generator.domain.processor.ProductProcessor;
import java.io.IOException;

public class ProcessProductHandler {
    private ProductProcessor processor;
    private ProductRepository repository;

    public ProcessProductHandler(ProductProcessor processor, ProductRepository repository) {
        this.processor = processor;
        this.repository = repository;
    }

    public void handle(ProcessProduct command) throws
        NoFamilyDefinedException,
        NoChildrenCategoryDefinedException,
        AttributeNotFoundException,
        FamilyNotFoundException,
        IOException,
        RepositoryException
    {

        Product product;
        while ((product = processor.getNext()) != null) {
            repository.add(product);
        }
    }
}
