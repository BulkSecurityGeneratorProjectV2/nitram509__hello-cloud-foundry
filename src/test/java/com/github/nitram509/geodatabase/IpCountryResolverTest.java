package com.github.nitram509.geodatabase;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class IpCountryResolverTest {

  private IpCountryResolver resolver;

  @BeforeClass
  public void setup() {
    resolver = new IpCountryResolver();
  }

  @DataProvider
  Object[][] allRecordsFromCsv() throws IOException {
    List<String[]> recordsToTest = new ArrayList<String[]>();
    BufferedReader reader = null;
    try {
      InputStream csvStream = this.getClass().getResourceAsStream("/GeoIPCountryWhois.csv");
      reader = new BufferedReader(new InputStreamReader(csvStream));
      String record;
      while ((record = reader.readLine()) != null) {
        String[] dataParts = record.split(",");
        for (int i = 0; i < dataParts.length; i++) {
          dataParts[i] = dataParts[i].replace("\"", "");
        }
        recordsToTest.add(dataParts);
      }
    } catch (Exception e) {
      Assert.fail("Couldn't prepare data provider", e);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }

    Object[][] recordsToTestArray = recordsToTest.toArray(new Object[recordsToTest.size()][]);
    int first100RecordsIndex = 0;
    int middle100RecordsIndex = recordsToTestArray.length / 2 - 50;
    int last100RecordsIndex = recordsToTestArray.length - 100;
    Object[][] result = new Object[300][];
    System.arraycopy(recordsToTestArray, first100RecordsIndex, result, 000, 100);
    System.arraycopy(recordsToTestArray, middle100RecordsIndex, result, 100, 100);
    System.arraycopy(recordsToTestArray, last100RecordsIndex, result, 200, 100);
    return result;
  }

  @Test(dataProvider = "allRecordsFromCsv")
  public void test_couple_of_records_from_original_csv(String ipFrom, String ipTo, String int32From, String int32To, String countryCode, String countryName) {
    for (String ip : new String[]{ipFrom, ipTo}) {
      Country actual = resolver.lookup(ip);

      assertThat(actual.name).isEqualTo(countryName);
      assertThat(actual.code).isEqualTo(countryCode);
      assertThat(actual.ipstart).isEqualTo(Long.parseLong(int32From));
      assertThat(actual.ipend).isEqualTo(Long.parseLong(int32To));
    }
  }

  @Test
  public void an_unknown_low_ip_will_resolve_to_an_corresponding_country_object() {
    String ipAsString = "0.1.2.3";
    long ipAsLong = (1 << 16) + 2 * (1 << 8) + 3;

    Country actual = resolver.lookup(ipAsString);

    assertThat(actual.name).isEqualTo("UNKNOWN");
    assertThat(actual.code).isEqualTo("N/A");
    assertThat(actual.ipstart).isEqualTo(ipAsLong);
    assertThat(actual.ipend).isEqualTo(ipAsLong);
  }

  @Test
  public void an_unknown_high_ip_will_resolve_to_an_corresponding_country_object() {
    String ipAsString = "224.255.255.255";
    long ipAsLong = 224L * (1 << 24) + 255L * (1 << 16) + 255L * (1 << 8) + 255L;

    Country actual = resolver.lookup(ipAsString);

    assertThat(actual.name).isEqualTo("UNKNOWN");
    assertThat(actual.code).isEqualTo("N/A");
    assertThat(actual.ipstart).isEqualTo(ipAsLong);
    assertThat(actual.ipend).isEqualTo(ipAsLong);
  }

  @Test
  public void lookup_with_String_eqals_lookup_with_Long() {
    String ipAsString = "197.23.11.79";
    long ipAsLong = 197L * (1 << 24) + 23L * (1 << 16) + 11L * (1 << 8) + 79L;

    Country actualFromString = resolver.lookup(ipAsString);
    Country actualFromLong = resolver.lookup(ipAsLong);

    assertThat(actualFromString.name).isEqualTo(actualFromLong.name);
    assertThat(actualFromString.code).isEqualTo(actualFromLong.code);
    assertThat(actualFromString.ipstart).isEqualTo(actualFromLong.ipstart);
    assertThat(actualFromString.ipend).isEqualTo(actualFromLong.ipend);
  }

}
