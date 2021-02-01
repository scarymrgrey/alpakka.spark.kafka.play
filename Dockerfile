FROM ubuntu:18.04
WORKDIR /opt
RUN apt-get update && apt-get install -y curl zip 

#RUN rm /bin/sh && ln -s /bin/bash /bin/sh
RUN apt-get -q -y install curl zip unzip
RUN curl -s https://get.sdkman.io | bash
RUN chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh"
RUN /bin/bash -c "source /root/.sdkman/bin/sdkman-init.sh; sdk version;sdk install java 11.0.10.hs-adpt;sdk install sbt"
#RUN source /root/.sdkman/bin/sdkman-init.sh; sdk version;sdk install java 11.0.10.hs-adpt;sdk install sbt

COPY ./services /services

RUN chmod a+x "/services/start_all.sh"

CMD /bin/bash -c "source /root/.sdkman/bin/sdkman-init.sh && /services/start_all.sh"
