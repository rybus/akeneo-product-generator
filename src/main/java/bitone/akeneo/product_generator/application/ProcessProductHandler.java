package bitone.akeneo.product_generator.application;

import bitone.akeneo.product_generator.domain.generator.ProductGenerator;
import bitone.akeneo.product_generator.domain.model.ProductRepository;
import bitone.akeneo.product_generator.domain.model.Product;
import bitone.akeneo.product_generator.domain.exception.NoFamilyDefinedException;
import bitone.akeneo.product_generator.domain.exception.NoChildrenCategoryDefinedException;
import bitone.akeneo.product_generator.domain.exception.RepositoryException;

public class ProcessProductHandler {
    private ProductProcessor processor;
    private ProductRepository repository;

    public ProcessProductHamdler(ProductProcessor processor, ProductRepository repository) {
        this.processor = processor;
        this.repository = repository;
    }

    public void handle(GenerateProduct command) throws
        NoFamilyDefinedException,
        NoChildrenCategoryDefinedException,
        RepositoryException
    {
        Product product = processor.getNext();

        repository.add(product);
    }
}
