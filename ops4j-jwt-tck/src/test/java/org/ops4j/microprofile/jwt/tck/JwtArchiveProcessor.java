package org.ops4j.microprofile.jwt.tck;

import java.io.File;

import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class JwtArchiveProcessor implements ApplicationArchiveProcessor {

	@Override
	public void process(Archive<?> applicationArchive, TestClass testClass) {
		if (!(applicationArchive instanceof WebArchive)) {
			return;
		}

		System.out.println("*** Enriching web archive");
		File[] libs = Maven.resolver()
				.resolve("io.smallrye:smallrye-jwt:1.1.0",
						"org.ops4j.microprofile.jwt:ops4j-jwt-auth:0.1.0-SNAPSHOT",
						"org.testng:testng:6.10",
						"org.eclipse.microprofile.jwt:microprofile-jwt-auth-api:1.1",
						"org.bitbucket.b_c:jose4j:0.6.4")
				.withoutTransitivity()
				.asFile();

		WebArchive war = applicationArchive.as(WebArchive.class);
		war.addAsLibraries(libs);
		war.addAsWebInfResource("jboss-web.xml");


		Node mpConfig = war.get("/META-INF/microprofile-config.properties");
		if (mpConfig == null) {
			war.addAsResource(new StringAsset("mp.jwt.verify.publickey.location = /publicKey.pem"), "/META-INF/microprofile-config.properties");
		}

		war.as(ZipExporter.class).exportTo(new File("/tmp/test.war"), true);
	}

}
