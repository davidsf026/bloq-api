FROM registry.redhat.io/jboss-eap-7/eap74-openjdk11-openshift-rhel8:latest
ENV CONFIG_EXTERNAL_FILE_PATH=/tmp
USER 0
RUN mkdir -pv /var/log/jboss_log/SUBTIC-Bloq/ && \
    chown -R 185:0 /var/log/jboss_log/SUBTIC-Bloq/
USER 185
COPY target/*.war $JBOSS_HOME/standalone/deployments/
