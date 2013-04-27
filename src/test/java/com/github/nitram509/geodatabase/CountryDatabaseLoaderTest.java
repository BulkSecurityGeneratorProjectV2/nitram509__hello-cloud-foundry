package com.github.nitram509.geodatabase;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static org.fest.assertions.api.Assertions.assertThat;

public class CountryDatabaseLoaderTest {

  private CountryDatabaseLoader loader;

  @BeforeMethod
  public void setUp() {
    loader = new CountryDatabaseLoader();
  }

  @Test
  public void CSV_columns_are_correctly_extracted() {
    String[] parts = loader.extractColumnsFromCsv("\"test\",\"mit,komma\"");
    assertThat(parts[0]).isEqualTo("test");
    assertThat(parts[1]).isEqualTo("mit,komma");
  }
}
