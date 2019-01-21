/*
 *
 *   Copyright 2018 Red Hat, Inc, and individual contributors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.ops4j.microprofile.jwt.principal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.keys.resolvers.VerificationKeyResolver;
import org.jose4j.lang.UnresolvableKeyException;
import org.ops4j.microprofile.jwt.KeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implements the MP-JWT 1.1 mp.jwt.verify.publickey.location config property resolution logic
 */
public class KeyLocationResolver implements VerificationKeyResolver {
    private static final Logger log = LoggerFactory.getLogger(KeyLocationResolver.class);
    private String location;
    private StringWriter contents;
    private byte[] base64Decode;

    public KeyLocationResolver(String location) {
        this.location = location;
    }
    @Override
    public Key resolveKey(JsonWebSignature jws, List<JsonWebStructure> nestingContext) throws UnresolvableKeyException {
        PublicKey key = null;
        String kid = jws.getHeaders().getStringHeaderValue("kid");
        try {
            loadContents();
        } catch (IOException e) {
            throw new UnresolvableKeyException("Failed to load key from: " + location, e);
        }
        // Determine what the contents are...
        key = tryAsJWKx(kid);
        if (key == null) {
            key = tryAsPEM();
        }
        if (key == null) {
            throw new UnresolvableKeyException("Failed to read location as any of JWK, JWKS, PEM; " + location);
        }
        return key;
    }

    private PublicKey tryAsPEM() {
        PublicKey publicKey = null;
        try {
            publicKey = KeyUtils.decodePublicKey(contents.toString());
        } catch (Exception e) {
            log.debug("Failed to read location as PEM", e);
        }
        return publicKey;
    }

    private PublicKey tryAsJWKx(String kid) {
        PublicKey publicKey = null;
        try {
            log.debug("Trying location as JWK(S)...");
            String json;
            if (base64Decode != null) {
                json = new String(base64Decode);
            } else {
                json = contents.toString();
            }
            JsonObject jwks = Json.createReader(new StringReader(json)).readObject();
            JsonArray keys = jwks.getJsonArray("keys");
            JsonObject jwk;
            if (keys != null) {
                jwk = keys.getJsonObject(0);
            } else {
                jwk = jwks;
            }
            String e = jwk.getString("e");
            String n = jwk.getString("n");

            byte[] ebytes = Base64.getUrlDecoder().decode(e);
            BigInteger publicExponent = new BigInteger(1, ebytes);
            byte[] nbytes = Base64.getUrlDecoder().decode(n);
            BigInteger modulus = new BigInteger(1, nbytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
            publicKey = kf.generatePublic(rsaPublicKeySpec);
        } catch (Exception e) {
            log.debug("Failed to read location as JWK(S)", e);
        }

        return publicKey;
    }

    private void loadContents() throws IOException {
        contents = new StringWriter();
        InputStream is;
        if (location.startsWith("classpath:") || location.indexOf(':') < 0) {
            String path;
            if (location.startsWith("classpath:")) {
                path = location.substring(10);
            } else {
                path = location;
            }
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            is = loader.getResourceAsStream(path);
        } else {
            URL locationURL = new URL(location);
            is = locationURL.openStream();
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("-----BEGIN") && !line.startsWith("-----END")) {
                    // Skip any pem file header/footer
                    contents.write(line);
                }
                line = reader.readLine();
            }
        }
        try {
            // Determine if this is base64
            base64Decode = Base64.getDecoder().decode(contents.toString());
        } catch (Exception e) {
            base64Decode = null;
            log.debug("contents does not appear to be base64 encoded");
        }
    }
}
