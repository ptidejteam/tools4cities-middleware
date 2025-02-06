# Tools4Cities Middleware

![Java](https://img.shields.io/badge/Java-orange)
![Apache Maven](https://github.com/ptidejteam/ptidej-Ptidej/actions/workflows/maven.yml/badge.svg)

Tools4Cities Middleware allows users to fetch, transform, and process data from various sources using Producers, Operations, and Consumers.

## What is it?

The purpose of the Tools4Cities Middleware is to enable users to perform operations on data from different sources via the use of the following abstractions:

- Producer: connects to data sources and fetches data
- Operation: describes transformations to be performed on producer outputs (data)
- Consumer: calls a series of producers, executes a series of operations on the producer's outputs, and then outputs the resulting data

![image](./docs/architecture.png)

## What do I need?

- Java 21
- Eclipse 2024-06 (4.32.0)
- Maven version 3.7.x (embedded in Eclipse)
- Postman (optional)

## How do I set it up?

- Open the terminal and check Java installation by typing "java --version". If Java is set up correctly, you should see the Java version printed on the terminal.
- Open Eclipse. On the left-side menu, select Import > Maven > Existing Maven Projects.
- Select project directory: tools4cities-middleware.
- Click "Finish" and wait for the project to load.
- After loading, right-click the "middleware" folder, and select Maven > Update Project.
- Right-click again and select: Run As > maven install
- Right-click again and select: Run As > maven test

Alternatively, if you have mvn set up in the command line, you can also run:

```bash
mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false
mvn clean
mvn validate
mvn install
```

## How do I use it?

- This middleware is a REST API which receives queries as input, and generates data as output.
- Queries are JSON files where you speficy which data you want, and which transformations you wish to apply on the data. In the folder /docs/examples, you can see examples of such queries.
- You can call the middleware routes using either Postman or via code (for example, using the requests package in [Python](https://www.geeksforgeeks.org/get-post-requests-using-python/)).
- For now the amount of Producers, Operations and parameters is quite limited, but we intend to expand it in the future and also document it better. Your suggestions are more than welcome!

## Who do I talk to?

Project manager: gabriel.cavalheiroullmann at concordia.ca

## Guidelines

- If you wish to integrate your changes into the middleware, please create a new branch from develop, make your changes, then open a PR requesting merge into develop.
- Tests shall be written to show the proper way Producers and Operations should be used. Consequently, the tests will also ensure these classes are working as intended by the developer.
