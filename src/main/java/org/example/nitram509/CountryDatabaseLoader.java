package org.example.nitram509;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class CountryDatabaseLoader {

  public Country[] loadCountries() {
    try {
      byte[] bytes = loadDataFromFile();
      List<Country> countries = parseDataAndInitializeCountries(bytes);
      return countries.toArray(new Country[countries.size()]);
    } catch (IOException e) {
      throw new RuntimeException("epic fail", e);
    }
  }

  private byte[] loadDataFromFile() throws IOException {
    InputStream is = this.getClass().getResourceAsStream("/GeoIPCountryWhois.csv");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int read;
    byte[] buf = new byte[4096];
    while ((read = is.read(buf)) >= 0) {
      baos.write(buf, 0, read);
    }
    is.close();
    return baos.toByteArray();
  }

  private List<Country> parseDataAndInitializeCountries(byte[] bytes) throws IOException {
    List<Country> countries = new ArrayList<Country>();
    String csv = new String(bytes, 0, bytes.length, "UTF-8");
    BufferedReader reader = new BufferedReader(new StringReader(csv));
    String line;
    long lastRangeEnd = 0;
    while ((line = reader.readLine()) != null) {
      String[] columns = line.split(",");
      if (columns.length > 5) {
        Country country = new Country();
        country.ipstart = parseLong(extractOnlyNumbers(columns[2]));
        country.ipend = parseLong(extractOnlyNumbers(columns[3]));
        country.code = filterQuotes(columns[4]);
        country.name = filterQuotes(columns[5]);
        countries.add(country);
        lastRangeEnd = country.ipend;
      }
    }
    reader.close();
    countries.add(createUnknownCountry(lastRangeEnd + 1));
    return countries;
  }

  private static String filterQuotes(String str) {
    if (str.startsWith("\"")) {
      str = str.substring(1);
    }
    if (str.endsWith("\"")) {
      str = str.substring(0, str.length() - 1);
    }
    return str;
  }

  private static String extractOnlyNumbers(String column) {
    return column.replaceAll("[^\\d]", "");
  }

  static Country createUnknownCountry(long ip) {
    Country unknownCountry = new Country();
    unknownCountry.name = "UNKNOWN";
    unknownCountry.code = "N/A";
    unknownCountry.ipstart = ip;
    unknownCountry.ipend = ip;
    return unknownCountry;
  }

}
