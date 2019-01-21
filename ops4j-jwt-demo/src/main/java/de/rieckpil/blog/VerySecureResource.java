package de.rieckpil.blog;

import java.security.Principal;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("secure")
@RequestScoped
public class VerySecureResource {

	private String message = "This is a secret text.";

	@Inject
	private JsonWebToken webToken;

	@Inject
	private Principal principal;

	@Inject
	private SecurityContext securityContext;

	@Context
	private javax.ws.rs.core.SecurityContext jaxrsContext;

	@GET
	@RolesAllowed("USER")
	public Response message() {

		String principalName = securityContext.getCallerPrincipal().getName();
		Principal userPrincipal = jaxrsContext.getUserPrincipal();

		System.out.println("JAX-RS principal: " + userPrincipal.getName());
		System.out.println(webToken.getIssuer());
		System.out.println(webToken.getRawToken());
		System.out.println(webToken.getTokenID());

		return Response.ok(principalName + " is allowed to read message: " + message).build();
	}
}
