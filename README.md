# Demo - Use JMS client Kerberos access with Elytron

This demo shows how to use use the `GSSAPI` SASL authentication mechanism from Elytron.

We want to send a message from JMS client with existing Kerberos ticket, so we want to allow GSSAPI access and grant `"guest"` role (used in messaging-activemq configuration) to incoming clients.

[The client code](src/main/java/org/wildfly/security/elytron/demo/JmsClient.java) in this demo is based on [helloworld-jms](https://github.com/wildfly/quickstart/tree/11.x/helloworld-jms) WildFly quickstart.

## Prerequisities

### Run kerberos KDC

Build [sample Kerberos server](https://github.com/kwart/kerberos-using-apacheds), create keytab for later usage and run the KDC.

```bash
cd /tmp
git clone https://github.com/kwart/kerberos-using-apacheds.git
cd kerberos-using-apacheds
mvn clean package
java -classpath target/kerberos-using-apacheds.jar org.jboss.test.kerberos.CreateKeytab remote/localhost@JBOSS.ORG remotepwd remote-localhost-remotepwd.keytab
java -jar target/kerberos-using-apacheds.jar test.ldif
```

The last step - running the server - generates the `krb5.conf` config file in the current directory. 

### WildFly 11.x

Build the server yourself from sources:

```
git clone https://github.com/wildfly/wildfly.git
cd wildfly
mvn clean source:jar install -DskipTests -Dcheckstyle.skip -Denforcer.skip
cp -r dist/target/wildfly-11.*-SNAPSHOT /tmp/wildfly
export JBOSS_HOME=/tmp/wildfly
``` 


## Configure the Application server

The configuration JBoss CLI script [demo.cli](demo.cli) contains commands to configure the server:

* enable Elytron in messaging and remoting
* allow `GSSAPI` SASL mechanism for remoting connections
* configure Kerberos and LDAP based security domain
* add a test queue 

### Run the configuration script

*The script starts embedded server itself, so you should not start it manually.*

```bash
$JBOSS_HOME/bin/jboss-cli.sh --file=demo.cli
```

## Client configuration

Custom [wildfly-config.xml](src/main/resources/wildfly-config.xml) is used on classpath to allow all SASL mechanisms.

The important part is the authentication configuration which allows using of GSSAPI SASL mechanism.

```xml
<configuration name="kerberos">
    <allow-sasl-mechanisms names="GSSAPI" />
    <use-service-loader-providers />
 </configuration>
```

## Run the demo

### Start the server (full profile)
```bash
$JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
```

### Authenticate to Kerberos and run the demo JMS client

Use "**secret**" as a password for hnelson user.

```bash
KRB5_CONFIG=/tmp/kerberos-using-apacheds/krb5.conf kinit hnelson@JBOSS.ORG
```

Build the demo app and run it:

```bash
mvn clean package
java -Djava.security.krb5.conf=/tmp/kerberos-using-apacheds/krb5.conf -Djavax.security.auth.useSubjectCredsOnly=false -jar target/elytron-kerberos-jms-client.jar
```