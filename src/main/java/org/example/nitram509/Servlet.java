/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.nitram509;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;

@Path("/")
public class Servlet {

    IpCountryResolver ipCountryResolver = new IpCountryResolver();

    @GET
    @Path("resolve")
    @Produces({"application/json", "application/xml"})
    public Country getResolve(@Context HttpServletRequest servletRequest, @Context HttpHeaders httpHeaders) {
        String clientIp = findClientIp(servletRequest, httpHeaders);
        Country country = ipCountryResolver.resolve(clientIp);
        country.remoteHost = clientIp;
        return country;
    }

    private String findClientIp(HttpServletRequest servletRequest, HttpHeaders httpHeaders) {
        String clientIp;
        if ((clientIp = getFirstElementOrNull(httpHeaders.getRequestHeader("x-cluster-client-ip"))) != null) return clientIp;
        if ((clientIp = getFirstElementOrNull(httpHeaders.getRequestHeader("x-forwarded-for"))) != null) return clientIp;
        return servletRequest.getRemoteHost();
    }

    private static String getFirstElementOrNull(List<String> stringList) {
        String result = null;
        if (stringList != null && stringList.size() > 0) {
            result = stringList.get(0);
        }
        return result;
    }

//    your IP:172.30.49.25
//    host=[locateme.cloudfoundry.com]
//    x-forwarded-for=[94.109.92.67, 172.30.8.253]
//    connection=[close]
//    user-agent=[Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0]
//    accept=[application/json, text/plain, */*]
//    x-requested-with=[XMLHttpRequest]
//    accept-language=[de-de,de;q=0.8,en-us;q=0.5,en;q=0.3]
//    referer=[http://locateme.cloudfoundry.com/]
//    x-cluster-client-ip=[94.109.92.67]
//    cookie=[s_nr=1352900824095; __utma=207604417.135143014.1352900297.1352900297.1352963971.2; __utmz=207604417.1352963971.2.2.utmcsr=docs.cloudfoundry.com|utmccn=(referral)|utmcmd=referral|utmcct=/getting-started.html; s_cc=true; ev6=client%2520ip%2520address; s_sq=%5B%5BB%5D%5D; __utmb=207604417.2.10.1352963971; __utmc=207604417]
//    accept-encoding=[gzip, deflate])
}
