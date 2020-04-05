# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM payara/server-full:5.201

COPY --chown=payara:payara target/payara-clientcert-test3-0.9-SNAPSHOT.war ${DEPLOY_DIR}
COPY --chown=payara:payara bin/pre-boot-commands.asadmin ${PREBOOT_COMMANDS}
COPY --chown=payara:payara bin/post-boot-commands.asadmin ${POSTBOOT_COMMANDS}

#RUN ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} start-domain ${DOMAIN_NAME} && \
#    ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} set configs.config.server-config.security-service.auth-realm.certificate.property.assign-groups=admins && \
#    ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} stop-domain ${DOMAIN_NAME}
