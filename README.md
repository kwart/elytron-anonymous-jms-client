# Demo - Use JMS client anonymous access with Elytron

This demo shows how to use use the `ANONYMOUS` SASL authentication mechanism from Elytron as replacement for `unauthenticatedIdentity` login module option in WidlFly legacy security.

We want to send a message from JMS client without authentication, so we want to allow anonymous access and grant `"guest"` role (used in messaging-activemq configuration) to incoming clients.

[The client code](src/main/java/org/wildfly/security/elytron/demo/JmsClient.java) in this demo is based on [helloworld-jms](https://github.com/wildfly/quickstart/tree/11.x/helloworld-jms) WildFly quickstart.

## Prerequisities

### WildFly 11.x

Until there is a public release of WildFly 11.x (e.g. Alpha1), you have to build the server yourself from sources:

```
git clone https://github.com/wildfly/wildfly.git
cd wildfly
mvn clean source:jar install -DskipTests -Dcheckstyle.skip -Denforcer.skip
cp -r dist/target/wildfly-11.*-SNAPSHOT /tmp/wildfly
export JBOSS_HOME=/tmp/wildfly
``` 


## Configure the Application server

The configuration JBoss CLI script [demo.cli](demo.cli) contains commands to configure the server:

* enable Elytron across the server
* allow `ANONYMOUS` SASL mechanism for remoting connections
* add "guest" role mapper to the ApplicationDomain Elytron security domain
* add a test queue 

### Run the configuration script

*The script starts embedded server itself, so you should not start it manually.*

```bash
$JBOSS_HOME/bin/jboss-cli.sh --file=demo.cli
```

## Client configuration

Custom [wildfly-config.xml](src/main/resources/wildfly-config.xml) is used on classpath to allow all SASL mechanisms.

The important part is the authentication configuration which allows using of all SASL mechanisms available.

```xml
<configuration name="authn">
	<allow-all-sasl-mechanisms />
	<use-service-loader-providers />
</configuration>
```

## Run the demo

### Start the server (full profile)
```bash
$JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
```

### Run the JMS client
```
mvn clean package exec:java
```

If the client execution fails on a `NullPointerException`, check if the issue [JBEAP-8047](https://issues.jboss.org/browse/JBEAP-8047)/[ELY-896](https://issues.jboss.org/browse/ELY-896) is fixed already in your WildFly version.
