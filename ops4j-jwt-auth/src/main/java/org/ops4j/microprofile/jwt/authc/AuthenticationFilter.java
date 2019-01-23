package org.ops4j.microprofile.jwt.authc;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.ops4j.microprofile.jwt.principal.JWTCallerPrincipal;

@Priority(Priorities.AUTHENTICATION)
@Provider
@RequestScoped
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private SecurityContext securityContext;

    @Inject
    private Principal principal;

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeaderVal = requestContext.getHeaderString("Authorization");
        if (authHeaderVal != null && authHeaderVal.startsWith("Bearer")) {
            try {
//                String bearerToken = authHeaderVal.substring(7);

//                AuthenticationStatus status = securityContext.authenticate(request, response,
//                        AuthenticationParameters.withParams().credential(new IdTokenCredential(bearerToken)));

                final javax.ws.rs.core.SecurityContext securityContext = requestContext.getSecurityContext();
                JWTSecurityContext jwtSecurityContext = new JWTSecurityContext(securityContext, (JWTCallerPrincipal) principal);
                requestContext.setSecurityContext(jwtSecurityContext);

            }
            catch (Exception ex) {
                ex.printStackTrace();
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
	}

}
