package bitone.akeneo.product_generator.domain.exception;

import java.lang.Exception;

public class AttributeNotFoundException extends Exception {
    public AttributeNotFoundException(String attributeCode) {
        super("Attribute " + attributeCode + " not found");
    }
}
