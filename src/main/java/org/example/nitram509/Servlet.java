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

@Path("/")
public class Servlet {

    IpCountryResolver ipCountryResolver = new IpCountryResolver();

    @GET
    @Path("resolve")
    @Produces({"application/json", "application/xml"})
    public Country getResolve(@Context HttpServletRequest servletRequest) {
        String remoteHost = servletRequest.getRemoteHost();
        Country country = ipCountryResolver.resolve(remoteHost);
        country.remoteHost = remoteHost;
        return country;
    }


}
