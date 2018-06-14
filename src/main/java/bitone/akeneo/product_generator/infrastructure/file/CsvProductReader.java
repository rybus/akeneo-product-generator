package bitone.akeneo.product_generator.infrastructure.file;

import bitone.akeneo.product_generator.domain.exception.RepositoryException;
import bitone.akeneo.product_generator.domain.model.ProductReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
import bitone.akeneo.product_generator.domain.model.RawValue;

public class CsvProductReader implements ProductReader {
    private String csvFile;
    private String[] headers;

    CSVParser csvParser = null;
    String line = "";
    String separator = ";";

    public void initialize(String csvFile) throws RepositoryException {
        String line = "";
        try {
            Reader reader = Files.newBufferedReader(Paths.get(this.csvFile));
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            if ((line = this.buffer.readLine()) != null) {
                headers = line.split(this.separator);
            }
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    public void close() throws RepositoryException {
        this.buffer.close();
    }

    public HashMap<String, RawValue> readLine() throws RepositoryException {
      try {
          if ((line = this.buffer.readLine()) != null) {
              return mapLineWithHeader(line.split(this.separator));
          }
          return null;
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    publi HashMap<String, RawValue> mapLineWithHeader(String[] rawLine)
    {
      HashMap<String, RawValues> values = new HashMap<String, RawValue>();
      for (int i = 0; i < headers.length. i++) {
        values.put(headers[i], new RawValue(headers[i], rawLine[i]));
      }

      return values;
    }
}
