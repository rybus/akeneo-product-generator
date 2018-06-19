package bitone.akeneo.product_generator.domain.exception;

import java.lang.Exception;

public class FamilyNotFoundException extends Exception {
    public FamilyNotFoundException(String attributeCode) {
        super("Family " + attributeCode + " not found");
    }
}
