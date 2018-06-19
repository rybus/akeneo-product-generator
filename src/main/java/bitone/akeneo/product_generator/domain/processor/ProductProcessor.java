package bitone.akeneo.product_generator.domain.processor;

import bitone.akeneo.product_generator.domain.RandomlyPicker;
import bitone.akeneo.product_generator.domain.exception.NoChildrenCategoryDefinedException;
import bitone.akeneo.product_generator.domain.exception.NoFamilyDefinedException;
import bitone.akeneo.product_generator.domain.exception.RepositoryException;
import bitone.akeneo.product_generator.domain.exception.AttributeNotFoundException;
import bitone.akeneo.product_generator.domain.exception.FamilyNotFoundException;
import bitone.akeneo.product_generator.domain.generator.product_value.ValueGenerator;
import bitone.akeneo.product_generator.domain.generator.product_value.ValueGeneratorRegistry;
import bitone.akeneo.product_generator.domain.model.Attribute;
import bitone.akeneo.product_generator.domain.model.AttributeTypes;
import bitone.akeneo.product_generator.domain.model.Category;
import bitone.akeneo.product_generator.domain.model.CategoryRepository;
import bitone.akeneo.product_generator.domain.model.AttributeRepository;
import bitone.akeneo.product_generator.domain.model.Channel;
import bitone.akeneo.product_generator.domain.model.ChannelRepository;
import bitone.akeneo.product_generator.domain.model.CurrencyRepository;
import bitone.akeneo.product_generator.domain.model.Family;
import bitone.akeneo.product_generator.domain.model.FamilyRepository;
import bitone.akeneo.product_generator.domain.model.Locale;
import bitone.akeneo.product_generator.domain.model.LocaleRepository;
import bitone.akeneo.product_generator.domain.model.Product;
import bitone.akeneo.product_generator.domain.model.Record;
import bitone.akeneo.product_generator.domain.model.ProductValue;
import bitone.akeneo.product_generator.domain.model.ProductReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.io.IOException;

public class ProductProcessor
{
    private ChannelRepository channelRepository;
    private LocaleRepository localeRepository;
    private CurrencyRepository currencyRepository;
    private FamilyRepository familyRepository;
    private CategoryRepository categoryRepository;
    private AttributeRepository attributeRepository;
    private ProductReader productReader;

    private int productIdIndex = 0;

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
        NoChildrenCategoryDefinedException,
        IOException,
        AttributeNotFoundException,
        FamilyNotFoundException,
        RepositoryException {

        HashMap<String, Record> product = this.productReader.readLine();

        if (null == product) {
            return null;
        }
        String sku = product.get("sku").getValue();
        int id = generateUniqueId();
        Family family = getFamily(product.get("family").getValue());
        Category[] categories = getCategories(product.get("categories").getValue().split(","));
        ProductValue[] values = getValues(product);

        return new Product(id, sku, true, family, values, categories);
    }

    private Family getFamily(String familyCode) throws NoFamilyDefinedException, FamilyNotFoundException {
        Family family;

        if(familyCode == null || familyCode.isEmpty()) {
            throw new NoFamilyDefinedException("No family provided.");
        }

        if (familyRepository.count() == 0) {
            throw new NoFamilyDefinedException("At least one family should exist.");
        }

        family  = familyRepository.get(familyCode);
        if (null == family) {
            throw new FamilyNotFoundException(familyCode);
        } else {
            return family;
        }
    }

    private Category[] getCategories(String[] categoryCodes) throws NoChildrenCategoryDefinedException {

        HashSet<Category> categories = new HashSet<Category>();

        if (categoryRepository.countChildren() == 0) {
            throw new NoChildrenCategoryDefinedException("At least one children category should exist");
        }

        for (String categoryCode : categoryCodes) {
            Category category = categoryRepository.get(categoryCode);
            if (null == category) {
                throw new NoChildrenCategoryDefinedException("Category not found " + categoryCode);
            }
            categories.add(category);
        }

        return (Category[]) categories.toArray(new Category[categories.size()]);
    }

    private ProductValue[] getValues(HashMap<String, Record> product) throws AttributeNotFoundException
    {
        Record[] Records = (Record[]) product.values().toArray(new Record[product.size()]);
        ArrayList<ProductValue> values = new ArrayList<ProductValue>();

        for (Record Record : Records) {
          if (Record.getAttribute().equals("categories") || Record.getAttribute().equals("family")) {
              continue;
          }
          Attribute attribute = attributeRepository.get(Record.getAttribute());
          if (null == attribute) {
              throw new AttributeNotFoundException(Record.getAttribute());
          }
          ProductValue value = new ProductValue(attribute, Record.getValue(), null, null);
          values.add(value);
        }

        return (ProductValue[]) values.toArray(new ProductValue[values.size()]);
    }

    private int generateUniqueId() {
        productIdIndex++;

        return productIdIndex;
    }
}
