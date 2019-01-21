package org.ops4j.demo.jwt.authc;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.swarm.microprofile.jwtauth.deployment.auth.cdi.MPJWTProducer;

import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipalFactory;
import io.smallrye.jwt.auth.principal.ParseException;

/**
 * An identity store which verifies a Google OAuth identity token and extracts
 * user information for the current session.
 * <p>
 * This class does not interact with any persistent storage. The actual storage
 * is on the Google side. We only check that the token is not expired and that
 * the user belongs to the expected domain.
 * <p>
 * In addition, we populate a session-scoped bean with the user profile.
 *
 * @author hwellmann
 *
 */
@ApplicationScoped
public class IdTokenIdentityStore implements IdentityStore {

	private static Logger log = LoggerFactory.getLogger(IdTokenIdentityStore.class);

	@Inject
	private JWTAuthContextInfo authContextInfo;

	/**
	 * Validates the given credential.
	 *
	 * @param credential identity token credential
	 * @return if valid, result with user email as caller name and a default group
	 *         {@code USER}.
	 * @throws ParseException
	 */
	public CredentialValidationResult validate(IdTokenCredential credential) throws ParseException {
		JWTCallerPrincipalFactory factory = JWTCallerPrincipalFactory.instance();
		try {
			JWTCallerPrincipal callerPrincipal = factory.parse(credential.getToken(), authContextInfo);
			MPJWTProducer.setJWTPrincipal(callerPrincipal);

			log.debug("Authenticated user: {}", callerPrincipal.getName());

			return new CredentialValidationResult(callerPrincipal.getName(), callerPrincipal.getGroups());
		} catch (ParseException exc) {
			log.warn("Cannot parse token", exc);
			return CredentialValidationResult.INVALID_RESULT;
		}
	}
}