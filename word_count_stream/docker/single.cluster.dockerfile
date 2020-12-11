FROM centos:centos7.8.2003 as centos_ssh_image
ENV TZ=Asia/Shanghai
RUN set -x \
    && for i in $(ls /lib/systemd/system/sysinit.target.wants/); \
            do [ $i == systemd-tmpfiles-setup.service ] || rm -f $i; \
        done \
    && rm -f /lib/systemd/system/multi-user.target.wants/* \
    && rm -f /etc/systemd/system/*.wants/* \
    && rm -f /lib/systemd/system/local-fs.target.wants/* \
    && rm -f /lib/systemd/system/sockets.target.wants/*udev* \
    && rm -f /lib/systemd/system/sockets.target.wants/*initctl* \
    && rm -f /lib/systemd/system/basic.target.wants/* \
    && rm -f /lib/systemd/system/anaconda.target.wants/* \
    && rm -rf /etc/yum.repos.d/* \
    && curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-7.repo \
    && sed -i -e '/mirrors.cloud.aliyuncs.com/d' -e '/mirrors.aliyuncs.com/d' /etc/yum.repos.d/CentOS-Base.repo \
    && yum -y install openssh-server openssh-clients \
    && systemctl enable sshd
COPY keys/id_rsa keys/id_rsa.pub /root/.ssh/
RUN set -x \
    && mkdir -p /root/.ssh \
    && chmod 700 /root/.ssh \
    && touch $HOME/.ssh/authorized_keys \
    && chmod 600 $HOME/.ssh/authorized_keys $HOME/.ssh/id_rsa $HOME/.ssh/id_rsa.pub \
    && cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys \
    && yum install -y java-1.8.0-openjdk-devel
ENV JAVA_HOME=/usr/lib/jvm/java
#CMD ["/usr/sbin/init"]

FROM centos_ssh_image as kafka_installed
ARG KAFKA_ROOT_PATH=/opt/kafka
COPY kafka_2.13-2.6.0.tgz ${KAFKA_ROOT_PATH}/kafka_2.13-2.6.0.tgz
RUN set -x \
    && cd ${KAFKA_ROOT_PATH} \
    && tar zxf ${KAFKA_ROOT_PATH}/kafka_2.13-2.6.0.tgz \
    && rm ${KAFKA_ROOT_PATH}/kafka_2.13-2.6.0.tgz \
    && ln -s kafka_2.13-2.6.0 current

FROM kafka_installed as flink_installed
ARG FLINK_ROOT_PATH=/opt/flink
COPY flink-1.11.2-bin-scala_2.11.tgz ${FLINK_ROOT_PATH}/flink-1.11.2-bin-scala_2.11.tgz
RUN set -x \
    && cd ${FLINK_ROOT_PATH} \
    && tar zxf ${FLINK_ROOT_PATH}/flink-1.11.2-bin-scala_2.11.tgz \
    && rm ${FLINK_ROOT_PATH}/flink-1.11.2-bin-scala_2.11.tgz \
    && ln -s flink-1.11.2 current

FROM flink_installed
COPY word_count_stream-with-dependencies.jar /opt/word_count_stream/lib/word_count_stream-with-dependencies.jar
COPY data /opt/word_count_stream/data
CMD ["bash", "-c", "/opt/flink/current/bin/start-cluster.sh \
    && /opt/kafka/current/bin/zookeeper-server-start.sh -daemon /opt/kafka/current/config/zookeeper.properties \
    && sleep 5s \
    && /opt/kafka/current/bin/kafka-server-start.sh -daemon /opt/kafka/current/config/server.properties \
    && sleep 1d"]
