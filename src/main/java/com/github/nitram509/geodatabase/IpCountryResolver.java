package com.github.nitram509.geodatabase;

import static java.lang.Long.parseLong;
import static com.github.nitram509.geodatabase.CountryDatabaseLoader.createUnknownCountry;

public class IpCountryResolver {

  private final Country[] countries;

  public IpCountryResolver() {
    CountryDatabaseLoader loader = new CountryDatabaseLoader();
    this.countries = loader.loadCountries();
  }

  public Country lookup(String ipString) {
    final String[] parts = ipString.split("[^\\d]");
    long ip = parseLong(parts[3], 10) +
            (parseLong(parts[2], 10) * 256) +
            (parseLong(parts[1], 10) * 65536) +
            (parseLong(parts[0], 10) * 16777216);
    return lookup(ip);
  }

  public Country lookup(long ip) {
    int idxMin = 0;
    int idxMiddle = 0;
    int idxMax = countries.length - 1;

    while (idxMin < idxMax) {
      idxMiddle = (idxMax + idxMin) >> 1;
      assert (idxMiddle < idxMax);
      if (countries[idxMiddle].ipstart < ip)
        idxMin = idxMiddle + 1;
      else
        idxMax = idxMiddle;
    }
    if ((idxMax == idxMin) && (countries[idxMin].ipstart == ip)) {
      return countries[idxMin];
    } else if ((idxMiddle > 0) && (countries[idxMiddle - 1].ipstart < ip) && (ip < countries[idxMiddle].ipstart)) {
      return countries[idxMiddle - 1];
    } else if ((idxMiddle < idxMax) && (countries[idxMiddle].ipstart < ip) && (ip < countries[idxMiddle + 1].ipstart)) {
      return countries[idxMiddle];
    }
    return createUnknownCountry(ip);
  }


}
