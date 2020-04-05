package org.thespheres.payara.clientcert.test.resources;

import java.security.Principal;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author
 */
//@RolesAllowed("admin")
@Path("javaee8")
public class JavaEE8Resource {

    @Context
    SecurityContext securityContext;

    @GET
    public Response ping() {
        final Principal p = securityContext.getUserPrincipal();
        final String name = p != null ? p.getName() : null;
        final boolean isAdmin = securityContext.isUserInRole("admin");
        return Response
                .ok("OK from " + name + " as admin: " + isAdmin)
                .build();
    }
}
