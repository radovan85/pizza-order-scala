FROM tomcat:10.1-jdk17

WORKDIR /app

COPY target/pizza-app.war /app/app.war

RUN mv /app/app.war /usr/local/tomcat/webapps/ROOT.war

ENV DB_URL=jdbc:postgresql://database:5432/pizza-db
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=1111

CMD ["catalina.sh", "run"]