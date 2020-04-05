package org.thespheres.payara.clientcert.test;

import fish.payara.security.annotations.CertificateAuthenticationMechanismDefinition;
import fish.payara.security.annotations.CertificateIdentityStoreDefinition;
import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 *
 * @author Juneau
 */
@DeclareRoles("admin")
@ApplicationPath("resources")
//@CertificateAuthenticationMechanismDefinition
//@CertificateIdentityStoreDefinition(value = "certificate", assignGroups = "admins")
public class JAXRSConfiguration extends Application {

}
