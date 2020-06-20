FROM wso2/micro-integrator:1.1.0
COPY CompositeApps/*.car ${WSO2_SERVER_HOME}/repository/deployment/server/carbonapps/
#COPY Libs/*.jar ${WSO2_SERVER_HOME}/lib/



#[DO NOT REMOVE] Auto generated Docker commands for config-map parser
COPY  --chown=wso2carbon:wso2 CarbonHome/.metadata/metadata_config.properties ${WSO2_SERVER_HOME}/.metadata/metadata_config.properties
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/datasources/master-datasources.xml ${WSO2_SERVER_HOME}/conf/datasources/master-datasources.xml
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/user-mgt.xml ${WSO2_SERVER_HOME}/conf/user-mgt.xml
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/axis2/axis2.xml ${WSO2_SERVER_HOME}/conf/axis2/axis2.xml
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/axis2/axis2_blocking_client.xml ${WSO2_SERVER_HOME}/conf/axis2/axis2_blocking_client.xml
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/carbon.xml ${WSO2_SERVER_HOME}/conf/carbon.xml
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/security/cipher-text.properties ${WSO2_SERVER_HOME}/conf/security/cipher-text.properties
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/passthru-http.properties ${WSO2_SERVER_HOME}/conf/passthru-http.properties
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/deployment.toml ${WSO2_SERVER_HOME}/conf/deployment.toml
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/jndi.properties ${WSO2_SERVER_HOME}/conf/jndi.properties
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/carbon.properties ${WSO2_SERVER_HOME}/conf/carbon.properties
COPY  --chown=wso2carbon:wso2 CarbonHome/conf/synapse.properties ${WSO2_SERVER_HOME}/conf/synapse.properties
RUN mkdir ${WSO2_SERVER_HOME}/repository/resources/conf/.metadata
COPY  --chown=wso2carbon:wso2 CarbonHome/.metadata/metadata_config.properties ${WSO2_SERVER_HOME}/repository/resources/conf/.metadata
#[DO NOT REMOVE] End of auto generated Docker commands for config-map parser
