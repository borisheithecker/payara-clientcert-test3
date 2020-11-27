This repository is set up to demostrate a series possible bugs in Payara server 5.201 related to the configuration of certificate realms. 
Steps to reproduce: 

1. In Payara 5.2020.6 production domain, enable client authentication for http-listener-2. Disable HTTP/2 for http-listener-2 as HTTP/2 does not support client certificate authentication. 

2. Make sure that no group assignment is set for realm "certificate".

3. Create a pkcs12 file: ```keytool -importkeystore -srckeystore PAYARA_DOMAIN/config/keystore.jks -destkeystore export.p12 -deststoretype PKCS12 -srcalias s1as -deststorepass changeit```

4. Run ```asadmin set configs.config.server-config.security-service.auth-realm.certificate.property.assign-groups=admins``` to add a group to the realm "certificate". 

5. Deploy the application. 

6. Run ```curl -v -k --cert-type P12 --cert export.p12:changeit https://localhost:8181/admins/resources/javaee8```. 

As espected, it returns "OK from CN=localhost,OU=Payara,O=Payara Foundation,L=Great Malvern,ST=Worcestershire,C=UK as admin: true"

7. Run ```asadmin set configs.config.server-config.security-service.auth-realm.certificate.property.assign-groups=admins2``` to change the set of assigned groups. Ensure that "admins2" shows up in the Assigned groups field in the console.  

8. Repeat step 6. 

The same output is returned, which is an error, because the caller should not be assigned to the group admins any more. 

9. Run ```asadmin set configs.config.server-config.security-service.auth-realm.certificate.property.assign-groups=``` to remove any group assigments. Ensure that no groups shows up in the Assigned groups field in the console.  

10. Repeat step 6. Same result, same error. 

11. Repeat step 7 to re-assign to the group "admin2" after removing any groups. Run curl (step 6) again. 

This time a 403 is thrown as expected. 

Further steps to show a related incorrect behaviour when deploying in docker and using a post-boot-commands.asadmin file. 

12. Build the docker image: sudo docker build -t payara-clientcert-test3 .

13. Run the docker image: sudo docker run --rm -p 8181:8181 --name pt payara-clientcert-test3

14. Step into the container: sudo docker exec -it pt /bin/bash

15. In the container, create a pkcs12 file: ```keytool -importkeystore -srckeystore appserver/glassfish/domains/production/config/keystore.jks -destkeystore export.p12 -deststoretype PKCS12 -srcalias s1as -deststorepass changeit```

16. Exit the container

17. Copy the key off the container: sudo docker cp pt:/opt/payara/export.p12 .

18. Run: curl -v -k --cert-type P12 --cert export.p12:changeit https://localhost:8181/admins/resources/javaee8

A 403 error is returned, which is a failure, because the caller should be assigned to the right group. Stepping into the container and exploring domain.xml it can been that ```<property name="assign-groups" value="admins"></property>``` is properly set. However, the realm is obviously not reinitiated after the change in the configuration, i.e. ```CertificateRealm.init(Properties)``` is not called. 

19. Uncomment line 1 in post-boot-commands.asadmin and rebuild and rerun. Again, a 430 error is returned. The above (steps 9 and 10) do not work as a workaround. 

20. Comment out lines 1 and 2 and uncomment lines 3 and 4. This time it works.

If using a post-boot-commands.asadmin as in Docker, the default realm for client certificate authentication must be completely removed and re-added. Changing only a property has no effect. 

Using pre-boot-commands.asadmin gives the same results. 

