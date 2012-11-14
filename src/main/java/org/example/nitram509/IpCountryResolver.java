package org.example.nitram509;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class IpCountryResolver {

    public void init() {
        List<Country> countries = new ArrayList<Country>();
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
                    country.startIp = parseLong(extractNumbers(columns[2]));
                    country.code = columns[4];
                    country.name = columns[5];
                    countries.add(country);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractNumbers(String column) {
        return column.replaceAll("[^\\d]", "");
    }

    Country resolve(String ip) {
        init();

        Country country = new Country();
        country.name = "nowhere";
        country.code = "n.n.";
        return country;
    }

}
