package bitone.akeneo.product_generator.infrastructure.file;

import bitone.akeneo.product_generator.domain.exception.RepositoryException;
import bitone.akeneo.product_generator.domain.model.ProductReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.util.HashMap;
import java.util.Iterator;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import bitone.akeneo.product_generator.domain.model.Record;

public class CsvProductReader implements ProductReader {
    private CSVRecord headers;
    private CSVRecord current;

    Iterator<CSVRecord> csvRecords = null;

    public void initialize(String csvFile) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(csvFile));
        csvRecords = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';')).iterator();
    }

    public HashMap<String, Record> readLine() throws IOException {
      CSVRecord record;

      if (csvRecords.hasNext()) {
        record = csvRecords.next();

        if (null == headers) {
            headers = record;

            return readLine();
        }

        return mapLineWithHeader(record);
      }

      return null;
    }

    public HashMap<String, Record> mapLineWithHeader(CSVRecord record)
    {
      HashMap<String, Record> values = new HashMap<String, Record>();
      for (int i = 0; i < headers.size(); i++) {
        values.put(headers.get(i), new Record(headers.get(i), record.get(i)));
      }

      return values;
    }
}
