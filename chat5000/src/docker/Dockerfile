FROM openjdk:8-slim
ADD chat5000.jar chat5000.jar
EXPOSE 6000
#RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#  && echo 'Asia/Shanghai' >/etc/timezone \
ENTRYPOINT ["java","-jar","chat5000.jar"]