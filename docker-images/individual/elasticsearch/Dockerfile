#
# beer store Dockerfile
#

# Pull base image.
FROM centos:7

# Install Java.
RUN \
  yum update -y && \
  yum install -y java-1.8.0-openjdk.x86_64

# Add binary source directory as a local working direstory
ADD . /data
COPY ./ElasticSearch_2_3.jar .
# Define working directory.
WORKDIR /data

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk
ENV CLASSPATH $JAVA_HOME/lib

EXPOSE 9200

RUN chmod +x /data/run_es.sh

# Define entrypoint to start with.
ENTRYPOINT ["/data/run_es.sh"]

