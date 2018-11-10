# Calculator Demo

This branch is to implement a calculator demo, generated with [Spring Initializr](https://start.spring.io/).

## Basic Info

> Spring Boot version: `2.1.0.RELEASE`.
>
> Selected dependencies: [MyBatis](http://www.mybatis.org/mybatis-3/), [Web](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html), [Lombok](https://projectlombok.org/).
>
> Plugin: [Maven](https://maven.apache.org/).
>
> See more at [pom.xml](https://github.com/GRP-17/feedback-backend/blob/calculator/pom.xml).

## Requirements

### Lombok Installation

[Lombok](https://projectlombok.org/) is a is a java library that automatically plugs into your editor and build tools.

In IntelliJ: https://github.com/mplushnikov/lombok-intellij-plugin.

In Eclipse: https://projectlombok.org/setup/eclipse.

## Running

You can launch the app either by running that `main()` method inside your IDE, or type `./mvnw spring-boot:run` on the command line. (`mvnw.bat` for Windows users).

In IntelliJ, Click <kbd>Run</kbd>  -> <kbd>Run 'CalculatorApplication'</kbd>.

## Example Usage

```bash
❯ curl http://localhost:8080/api/sum?num1=10.9&num2=2.888
{"num1":10.9,"num2":2.888,"sum":13.788}
```

