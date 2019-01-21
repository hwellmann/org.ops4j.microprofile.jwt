package org.ops4j.microprofile.jwt.authc;

import javax.security.enterprise.credential.Credential;

/**
 * Credential containing a Google OAuth identity token.
 *
 * @author hwellmann
 *
 */
public class IdTokenCredential implements Credential {

    private String token;


    /**
     * Creates a credential with the given token.
     * @param token identity token
     */
    public IdTokenCredential(String token) {
        this.token = token;
    }

    /**
     * Gets the identity token.
     * @return token
     */
    public String getToken() {
        return token;
    }
}
