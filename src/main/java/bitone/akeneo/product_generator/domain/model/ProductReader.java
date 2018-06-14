package bitone.akeneo.product_generator.domain.model;

import bitone.akeneo.product_generator.domain.exception.RepositoryException;

public interface ProductRepository {

    public void initialize(String csvFile) throws RepositoryException {

    public Product readProduct() throws RepositoryException;

    public void close() throws RepositoryException;
}
