FROM ubuntu:18.04


# base tools
RUN apt-get update && \
    apt-get install -y \
    git \
    wget \
    curl \
    gnupg \
    unzip

# Install OpenJDK-8
RUN apt-get update &&\
    apt-get install -y openjdk-8-jdk &&\
    apt-get clean

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Install scala 2.12
RUN wget https://downloads.lightbend.com/scala/2.12.2/scala-2.12.2.deb && \
    dpkg -i scala-2.12.2.deb && \
    apt-get update && \
    apt-get install -y scala

# Install SBT
# https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
	apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
	apt update && apt install sbt -y

RUN adduser --disabled-password \
    --gecos '' zmiarzynska
RUN adduser zmiarzynska sudo
RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> \
    /etc/sudoers
USER zmiarzynska
WORKDIR /home/zmiarzynska/

RUN cd /home/zmiarzynska/
RUN mkdir target
COPY ebiznes-1.0.zip ./target
RUN unzip -o -d ./target /home/zmiarzynska/target/ebiznes-1.0.zip

CMD cd ./target/ebiznes-1.0/bin && ./ebiznes -Dplay.evolutions.db.default.autoApply=true -Dhttp.port=9000
EXPOSE 9000
