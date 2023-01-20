FROM maven:3.8.6-jdk-11

COPY requirements.txt .

RUN apt-get update
RUN apt-get install -y python3
RUN apt-get install -y python3-pip
RUN pip3 install pip --upgrade
RUN pip3 install -r requirements.txt
RUN curl --silent --show-error --fail "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
RUN  unzip awscliv2.zip
RUN  ./aws/install
RUN  apt install -y groff

ENTRYPOINT ["tail"]
CMD ["-f","/dev/null"]