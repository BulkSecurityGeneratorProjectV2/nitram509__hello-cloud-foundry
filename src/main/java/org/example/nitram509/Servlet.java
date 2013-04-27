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
import java.util.List;

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

}
