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

### Local Environment

```bash
❯ curl http://localhost:8080/api/sum?num1=10.9&num2=2.888
{"num1":10.9,"num2":2.888,"sum":13.788}
```

### A Deployed Demo

https://feedback-analysis-calculator.herokuapp.com/api/sum?num1=1.2&num2=3.9

## Pipeline

### Project Pipeline: GitHub

Code is source-controlled via GitHub.

### CI Pipeline: Travis CI

This GitHub repository is connected to [Travis CI](https://travis-ci.com/).

Every time there is a commit pushed (or pull request) to GitHub, Travis will help run the tests and try to depoly the app to Heroku.

The configuration file: [.travis.yml](https://github.com/GRP-17/feedback-backend/blob/calculator/.travis.yml).

### Deploy Pipeline: Heroku

This GitHub repository is also connected to [Heroku](https://heroku.com). But it will wait for CI to pass before deploy.

As configured in [.travis.yml](https://github.com/GRP-17/feedback-backend/blob/calculator/.travis.yml):

```yaml
deploy:
  provider: heroku
  api_key:
    secure: kzVq4L5vd6Q/0shI2iDji+nTRRCmHu4MJIObeASNCiNWW8jlr6PSxid5V6HX8VcGeElABqxPa2yzub+au4PQjDOQAWyGC2QN5QgoZ9rQcrMaXPBkYcQBxkQfj32grPQyk9vxvXG3wX8p1pswFxjoUC5ONivvRihRpujgrV7ZvqYTdsF+hD7uTFymWxPL0uWukwVsa9KjHJdyUjgC3dVMI7eXt7x+RMFxci3DWGvOEakME6b9h3OX+417z6BEvgrtK+g6YhVEF/JP/L9Lu/VUvKoTolFDW2WeAW8sCS3+NrMfy2PpJAoogZVzi+M3P4l+F2uhbIGGigZAkDl0K5nlHaggxflCTDjxC4vtJQmpQY8jnZPvwbWIQDrPqPRmR9zlq7tp/oQWf/XfX4Sjiuodwg7uU5slfAPIv0S+e4GsW9iJrLyOJNkLuIEChztikGWsGM3HkBm+Cb9FCYh0UgZjNyjIvPRy/pZmkhcSKdUDkLXQjvY2JoPam41lYOz3kW/fIfZ0I0qTpPQuC7JQcRaRKMg5MjF+C1eCICBpNK+uofjo5vKsHGuVBUpDgsfri5qmokakglNC/TdiqpiODzdelzxnklkRpdmTNwDrTNulDmuoZ9+Kd0sqchhZnQvgjMctv9nvAWsYQCUTjW0+0KAu2OWD98N+1+iLdFNlaqP9x3k=
  app:
    master: feedback-analysis-grp-app
    calculator: feedback-analysis-calculator
```

- The `api_key` is generated with the [Travis encryption method](https://docs.travis-ci.com/user/encryption-keys/) for security.
  > The decrypted `api_key` is the key for a heroku account. It's generated with [Heroku CLI](https://devcenter.heroku.com/articles/authentication) using `heroku auth:token`.
  >
  > (In fact, here uses `heroku authorizations:create` to generate a long-term token, because the above one only provide a one-day period.)

- Deploy targets:

  - The `master` branch will be deployed to [feedback-analysis-grp-app](https://feedback-analysis-grp-app.herokuapp.com/)
  - The `calculator` branch will be deployed to [feedback-analysis-calculator](https://feedback-analysis-calculator.herokuapp.com/)

