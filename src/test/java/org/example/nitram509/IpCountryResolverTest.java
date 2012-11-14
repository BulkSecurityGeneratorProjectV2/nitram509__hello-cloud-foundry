package org.example.nitram509;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: maki
 * Date: 14.11.12
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
public class IpCountryResolverTest {

    private IpCountryResolver resolver;

    @BeforeMethod
    public void setup() {
        resolver = new IpCountryResolver();
    }

    @Test
    public void test_ip_in_the_lower_range_and_leftmost_border() {
        // "1.0.64.0","1.0.127.255","16793600","16809983","JP","Japan"
        String ip = "1.0.64.0";
        Country actual = resolver.resolve(ip);
        Assert.assertEquals(actual.name, "Japan");
        Assert.assertEquals(actual.code, "JP");
    }

    @Test
    public void test_ip_in_the_lower_range_and_middle() {
        // "1.0.64.0","1.0.127.255","16793600","16809983","JP","Japan"
        String ip = "1.0.90.90";
        Country actual = resolver.resolve(ip);
        Assert.assertEquals(actual.name, "Japan");
        Assert.assertEquals(actual.code, "JP");
    }

    @Test
    public void test_ip_in_the_lower_range_and_rightmost_border() {
        // "1.0.64.0","1.0.127.255","16793600","16809983","JP","Japan"
        String ip = "1.0.64.0";
        Country actual = resolver.resolve(ip);
        Assert.assertEquals(actual.name, "Japan");
        Assert.assertEquals(actual.code, "JP");
    }


}
