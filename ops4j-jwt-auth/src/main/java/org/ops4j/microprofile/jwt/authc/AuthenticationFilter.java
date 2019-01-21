package org.ops4j.microprofile.jwt.authc;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

//@Priority(Priorities.AUTHENTICATION)
//@Provider
@RequestScoped
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private SecurityContext securityContext;

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeaderVal = requestContext.getHeaderString("Authorization");
        if (authHeaderVal != null && authHeaderVal.startsWith("Bearer")) {
            try {
                String bearerToken = authHeaderVal.substring(7);

                AuthenticationStatus status = securityContext.authenticate(request, response,
                        AuthenticationParameters.withParams().credential(new IdTokenCredential(bearerToken)));


            }
            catch (Exception ex) {
                ex.printStackTrace();
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
        else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
	}

}
