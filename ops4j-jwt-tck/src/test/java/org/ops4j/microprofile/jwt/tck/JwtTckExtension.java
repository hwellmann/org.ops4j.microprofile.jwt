package org.ops4j.microprofile.jwt.tck;

import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class JwtTckExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        System.err.println("Registered JwtArchiveProcessor");
        extensionBuilder.service(ApplicationArchiveProcessor.class, JwtArchiveProcessor.class);
    }

}

