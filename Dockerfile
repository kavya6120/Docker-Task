FROM tomcat:9

RUN rm -rf /usr/local/tomcat/webapps/*

COPY index.html /usr/local/tomcat/webapps/ROOT/index.html

EXPOSE 8080

CMD ["catalina.sh", "run"]
