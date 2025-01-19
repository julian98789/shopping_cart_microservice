FROM amazoncorretto:17.0.12

WORKDIR /app

COPY build/libs/shopping_cart-0.0.1-SNAPSHOT.jar /app/shopping_cart-0.0.1-SNAPSHOT.jar

EXPOSE 8083:8083

CMD ["java", "-jar", "shopping_cart-0.0.1-SNAPSHOT.jar"]