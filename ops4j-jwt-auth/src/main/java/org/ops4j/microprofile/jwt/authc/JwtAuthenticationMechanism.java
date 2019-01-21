package org.ops4j.microprofile.jwt.authc;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.CredentialValidationResult.Status;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

@RequestScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

	@Inject
	private IdentityStoreHandler identityStoreHandler;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext httpMessageContext) throws AuthenticationException {
		String authHeaderVal = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeaderVal == null) {
			return AuthenticationStatus.NOT_DONE;
		}
		if (authHeaderVal.startsWith("Bearer")) {
			String bearerToken = authHeaderVal.substring(7);

			IdTokenCredential credential = new IdTokenCredential(bearerToken);

			CredentialValidationResult validationResult = identityStoreHandler.validate(credential);
			if (validationResult.getStatus() == Status.VALID) {
				return httpMessageContext.notifyContainerAboutLogin(validationResult.getCallerPrincipal(),
						validationResult.getCallerGroups());
			}
		}
		return httpMessageContext.responseUnauthorized();
	}
}