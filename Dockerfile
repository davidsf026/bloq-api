FROM image-registry.openshift-image-registry.svc:5000/openshift/jboss-eap74-openjdk11-openshift
ENV CONFIG_EXTERNAL_FILE_PATH=/tmp
USER 0
RUN mkdir -pv /var/log/jboss_log/SUBTIC-Bloq/ && \
    chown -R 185:0 /var/log/jboss_log/SUBTIC-Bloq/
USER 185
COPY target/*.war $JBOSS_HOME/standalone/deployments/
