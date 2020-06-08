# set base Docker image to AdoptOpenJDK Ubuntu Docker image
FROM adoptopenjdk:11.0.6_10-jdk-hotspot-bionic

# set Docker image build arguments
# build arguments for user/group configurations
ARG USER=wso2carbon
ARG USER_ID=802
ARG USER_GROUP=wso2
ARG USER_GROUP_ID=802
ARG USER_HOME=/home/${USER}

# create the non-root user and group and set MOTD login message
RUN \
    groupadd --system -g ${USER_GROUP_ID} ${USER_GROUP} \
    && useradd --system --create-home --home-dir ${USER_HOME} --no-log-init -g ${USER_GROUP_ID} -u ${USER_ID} ${USER}

# copy jar of the Hospital Service
COPY --chown=wso2carbon:wso2 Hospital-Service-2.0.0.jar ${USER_HOME}/

# install required packages
RUN \
    apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
        libxml2-utils \
        netcat \
        unzip \
        wget \
    && rm -rf /var/lib/apt/lists/*

# set the user and work directory
USER ${USER_ID}
WORKDIR ${USER_HOME}

# set environment variables
ENV WORKING_DIRECTORY=${USER_HOME}

# expose ports
EXPOSE 9091

# initiate container and start WSO2 Carbon server
CMD java -jar /home/wso2carbon/Hospital-Service-2.0.0.jar