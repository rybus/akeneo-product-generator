package bitone.akeneo.product_generator.domain.model;

public class Record {
    private String attribute;
    private String value;

    public Record(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public String getAttribute()
    {
        return attribute;
    }

    public String getValue()
    {
        return value;
    }
}
