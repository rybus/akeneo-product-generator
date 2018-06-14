package bitone.akeneo.product_generator.domain.processor;

import bitone.akeneo.product_generator.domain.RandomlyPicker;
import bitone.akeneo.product_generator.domain.exception.NoChildrenCategoryDefinedException;
import bitone.akeneo.product_generator.domain.exception.NoFamilyDefinedException;
import bitone.akeneo.product_generator.domain.generator.product_value.ValueGenerator;
import bitone.akeneo.product_generator.domain.generator.product_value.ValueGeneratorRegistry;
import bitone.akeneo.product_generator.domain.model.Attribute;
import bitone.akeneo.product_generator.domain.model.AttributeTypes;
import bitone.akeneo.product_generator.domain.model.Category;
import bitone.akeneo.product_generator.domain.model.CategoryRepository;
import bitone.akeneo.product_generator.domain.model.Channel;
import bitone.akeneo.product_generator.domain.model.ChannelRepository;
import bitone.akeneo.product_generator.domain.model.CurrencyRepository;
import bitone.akeneo.product_generator.domain.model.Family;
import bitone.akeneo.product_generator.domain.model.FamilyRepository;
import bitone.akeneo.product_generator.domain.model.Locale;
import bitone.akeneo.product_generator.domain.model.LocaleRepository;
import bitone.akeneo.product_generator.domain.model.Product;
import bitone.akeneo.product_generator.domain.model.ProductValue;
import java.util.ArrayList;
import java.util.HashSet;

public class ProductProcessor
{
    private ChannelRepository channelRepository;
    private LocaleRepository localeRepository;
    private CurrencyRepository currencyRepository;
    private FamilyRepository familyRepository;
    private CategoryRepository categoryRepository;
    private AttributeRepository attributeRepository;
    private ProductReader productReader;

    private int skuIndex = 0;
    private int familyIndex = 1;
    private int categoryIndex = 2;

    public ProductProcessor (
        ChannelRepository channelRepository,
        LocaleRepository localeRepository,
        CurrencyRepository currencyRepository,
        FamilyRepository familyRepository,
        CategoryRepository categoryRepository,
        AttributeRepository attributeRepository,
        ProductReader productReader
    ) {
        this.channelRepository = channelRepository;
        this.localeRepository = localeRepository;
        this.currencyRepository = currencyRepository;
        this.familyRepository = familyRepository;
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
        this.productReader = productReader;
    }

    public Product getNext() throws
        NoFamilyDefinedException,
        NoChildrenCategoryDefinedException {

        HashMap<String, RawValue> product = this.productReader.readProduct();

        String sku = product.get('sku');
        int id = generateUniqueId();
        Family family = getFamily( product.get('family'));
        Category[] categories = getCategories(product.get('categories').split(','));
        ProductValue[] values = getValues(product);

        return new Product(id, sku, true, family, values, categories);
    }

    private Family getFamily(String familyCode) throws NoFamilyDefinedException {
        Family family;

        if(familyCode != null && !familyCode.isEmpty()) {
            throw new NoFamilyDefinedException("No family provided.");
        }

        if (familyRepository.count() == 0) {
            throw new NoFamilyDefinedException("At least one family should exist.");
        }

        family  = familyRepository.get(familyCode)
        if (null = family) {
            throw new NoFamilyDefinedException("Family with code " + familyCode + ' does not exist.');
        } else {
            return family;
        }
    }

    private Category[] getCategories(String[] categoryCodes) throws NoChildrenCategoryDefinedException {

        Category[] categories = new HashSet<Category>();

        if (categoryRepository.countChildren() == 0) {
            throw new NoChildrenCategoryDefinedException("At least one children category should exist");
        }

        for (String categoryCodes : categoryCode) {
            Category category = categortyRepository.get(categoryCode);

            categories.add(category);
        }

        return (Category[]) categories.toArray(new Category[categories.size()]);
    }

    private ProductValue[] getValues(HashMap<String, RawValue> product)
    {
        RawValues[] rawValues = (RawValues[]) product.values().toArray(new RawLine[product.size()]);
        ArrayList<ProductValue> values = new ArrayList<ProductValue>();

        for (RawValues rawValue : rawValues) {
            attribute = attributeRepository.get(rawValue.getAttribute())
            if (attribute.getType().equals(AttributeTypes.IDENTIFIER)) {
                ProductValue identifierValue = new ProductValue(attribute, rawValue, null, null);
                values.add(identifierValue);
            } else {
              ProductValue value = new ProductValue(attribute, identifier, null, null);
               values.add(value);
            }
        }

        return (ProductValue[]) values.toArray(new ProductValue[values.size()]);
    }

    private int generateUniqueId() {
        productIdIndex++;

        return productIdIndex;
    }

    public void reset() {
        productIdentifierIndex = 0;
    }
}
