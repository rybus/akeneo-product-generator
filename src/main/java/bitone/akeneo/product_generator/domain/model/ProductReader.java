package bitone.akeneo.product_generator.domain.model;

import bitone.akeneo.product_generator.domain.exception.RepositoryException;
import java.util.HashMap;
import java.io.IOException;

public interface ProductReader {

    public void initialize(String csvFile) throws IOException;

    public HashMap<String, Record> readLine() throws IOException;
}
