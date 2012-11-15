package org.example.nitram509;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class IpCountryResolver {

    private List<Country> countries;

    public IpCountryResolver() {
        init();
    }

    void init() {
        countries = new ArrayList<Country>();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("GeoIPCountryWhois.csv");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            byte[] buf = new byte[4096];
            while ((read = is.read(buf)) >= 0) {
                baos.write(buf, 0, read);
            }
            is.close();
            byte[] arr = baos.toByteArray();
            String csv = new String(arr, 0, arr.length, "UTF-8");
            BufferedReader reader = new BufferedReader(new StringReader(csv));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length > 5) {
                    Country country = new Country();
                    country.ipstart = parseLong(extractOnlyNumbers(columns[2]));
                    country.code = filterQuotes(columns[4]);
                    country.name = filterQuotes(columns[5]);
                    countries.add(country);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("epic fail", e);
        }
    }

    private String filterQuotes(String str) {
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

    public Country resolve(String ip) {
        String[] parts = ip.split("[^\\d]");
        long target_ip = parseLong(parts[3], 10) +
                (parseLong(parts[2], 10) * 256) +
                (parseLong(parts[1], 10) * 65536) +
                (parseLong(parts[0], 10) * 16777216);

        int idxMin = 0;
        int idxMiddle = 0;
        int idxMax = countries.size() - 1;
        Country pickedCountry = null;
        while (idxMin <= idxMax) {
            idxMiddle = (idxMax + idxMin) >> 1;
            pickedCountry = countries.get(idxMiddle);
            // determine which subarray to search
            if (pickedCountry.ipstart < target_ip) {
                // change min index to search upper subarray
                idxMin = idxMiddle + 1;
            } else if (pickedCountry.ipstart > target_ip) {
                // change max index to search lower subarray
                idxMax = idxMiddle - 1;
            } else {
                // key found at index imid
                break;
            }
        }
        // return previous found country.
        return pickedCountry;
    }

}
