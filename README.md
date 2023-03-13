# CDI Extensions Demo
  
This is a demo project for Quarkus Insights #122.

It compares 2 approaches for integrating with the CDI container in Quarkus:

- Build Compatible Extensions, as defined by CDI Lite
- Quarkus extensions
       
Therefore, there are 2 demonstration projects, both implementing the same use case.

## Use Case

The demo consists of a hypothetical, CDI-based framework.
It is not very realistic, but covers many important concerns.

Users of the framework are supposed to provide beans that implement a simple interface:

```java
interface Processor {
    void doWork();
}
```

The framework provides a bean of type `ProcessorManager` which allows obtaining all `Processor` instances.
For this, the extension must:

1. participate in type discovery.

Further, the framework makes sure that all implementations of `Processor.doWork()` are intercepted with a `LoggingInterceptor`.
For this, the extension must:

2. alter annotations on application classes.

Further, the processor implementations may be annotated `@Important`, which changes the log level used by the interceptor.
For this, the extension must:

3. inspect beans that exist in the container,
4. create a synthetic bean and pass data from the extension to runtime.

Finally, the extension validates that at least one `Processor` implementation actually exists.
For this, the extension must:

5. perform custom validation of the deployment.

## Usage

This is _not_ a multi-module Maven project.
There's a parent POM only for centralizing some boilerplate.
All modules must be built independently.

First of all, install the parent POM and the framework core classes to the local Maven repository for later resolution:

```bash
mvn clean install

cd framework
mvn clean install
```

### `cdi-lite`

```bash
cd cdi-lite/integration
mvn clean install

cd ../user
mvn clean test
```

### `quarkus`

```bash
cd quarkus/integration
mvn clean install

cd ../user
mvn clean test
```
