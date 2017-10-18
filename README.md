# Fast Fake Products Generator for Akeneo PIM 2.

## Overview
This tool generates products tables data file that can be re-imported wih very fast MySQL native tools.

## How to use it?

### Pre-requisites
You need a PIM installation with all structural entities already configured (attributes, families, categories, attribute options, etc...), but without product.

### Generating data files
```bash
$ java -jar akeneo-product-generator-full-0.1.0.jar <database URL> <output directory> <number of products>
```
 - example:
```bash
$ java -jar akeneo-product-generator-full-0.1.0.jar "jdbc:mysql://localhost/pim_ce_20?user=akeneo_pim&password=akeneo_pim" /var/tmp 1000000
```
It will generate 1 million products based on the structure from the local MySQL on the `pim_ce_20` database.

### Loading the generated TSV files
```sql
mysql> LOAD DATA LOCAL INFILE '/var/tmp/products.tsv' INTO TABLE pim_catalog_product;
mysql> LOAD DATA LOCAL INFILE '/var/tmp/products-categories.tsv' INTO TABLE pim_catalog_category_product;
mysql> LOAD DATA LOCAL INFILE '/var/tmp/products-unique-data.tsv' INTO TABLE pim_catalog_product_unique_data;
```
## Performances
Some metrics for 1 million products:
 - Generating data files: 64s
 - Importing into the pim_catalog_product table: 2 min 30s
 - Importing into the pim_catalog_category_product table: 1 min 25s
 - Importing into pim_catalog_product_unique_data: 33s

 => full process from 0 product to 1 million products in DB: 5 min 30s

Note: Once done, you still need to index the data with the `pim:product:index --all` command.
