package org.ops4j.microprofile.jwt.authc;

import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

/**
 * Google OAuth identity token containing a number of claims.
 * <p>
 * The raw format is JWT.
 * @see <a href="https://developers.google.com/identity/protocols/OpenIDConnect#obtainuserinfo">Obtain user information
 * from the ID token</a>
 * @author hwellmann
 *
 */
public class IdentityToken {

    @JsonbProperty("iss")
    private String issuer;

    @JsonbProperty("at_hash")
    private String accessTokenHash;

    @JsonbProperty("email_verified")
    private boolean emailVerified;

    @JsonbProperty("sub")
    private String subject;

    @JsonbProperty("azd")
    private String authorizedPresenter;

    private String email;

    @JsonbProperty("profile")
    private String profileUrl;

    @JsonbProperty("picture")
    private String pictureUrl;

    @JsonbProperty("name")
    private String fullName;

    @JsonbProperty("aud")
    private String audience;

    @JsonbProperty("iat")
    private long issuedAt;

    @JsonbProperty("exp")
    private long expiresAt;

    private String nonce;

    @JsonbProperty("hd")
    private String hostedDomain;

    private List<String> groups;

    @JsonbProperty("preferred_username")
    private String preferredUsername;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAccessTokenHash() {
        return accessTokenHash;
    }

    public void setAccessTokenHash(String accessTokenHash) {
        this.accessTokenHash = accessTokenHash;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthorizedPresenter() {
        return authorizedPresenter;
    }

    public void setAuthorizedPresenter(String authorizedPresenter) {
        this.authorizedPresenter = authorizedPresenter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profile) {
        this.profileUrl = profile;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String picture) {
        this.pictureUrl = picture;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(long issuedAt) {
        this.issuedAt = issuedAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getHostedDomain() {
        return hostedDomain;
    }

    public void setHostedDomain(String hostedDomain) {
        this.hostedDomain = hostedDomain;
    }

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public String getPreferredUsername() {
		return preferredUsername;
	}

	public void setPreferredUsername(String preferredUsername) {
		this.preferredUsername = preferredUsername;
	}
}
