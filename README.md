# JBEAP-8585 reproducer

This reproducer shows problem with `JBOSS-LOCAL-USER` SASL authentication mechanism in JMS client.

## Configure the Application server

The configuration JBoss CLI script [demo.cli](demo.cli) contains commands to configure the server:

* enable Elytron across the server

### Run the configuration script

*The script starts embedded server itself, so you should not start it manually.*

```bash
$JBOSS_HOME/bin/jboss-cli.sh --file=demo.cli
```

## Run the reproducer

### Start the server (full profile)
```bash
$JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
```

### Run the JMS client (default Elytron client configuration)
```bash
mvn clean package exec:java -Dmaven.repo.local=/path/to/jboss-eap-7.1.0.Alpha1-maven-repository/maven-repository
```

#### Result
```
INFO: WildFly Naming version 1.0.0.Beta6-redhat-1
Led 31, 2017 11:55:24 DOP. org.wildfly.security.elytron.demo.JmsClient main
INFO: Attempting to acquire connection factory
Led 31, 2017 11:55:24 DOP. org.jboss.remoting3.EndpointImpl <clinit>
INFO: JBoss Remoting version 5.0.0.Beta12-redhat-1
Led 31, 2017 11:55:24 DOP. org.xnio.Xnio <clinit>
INFO: XNIO version 3.4.3.Final-redhat-1
Led 31, 2017 11:55:24 DOP. org.xnio.nio.NioXnio <clinit>
INFO: XNIO NIO Implementation Version 3.4.3.Final-redhat-1
Led 31, 2017 11:55:24 DOP. org.wildfly.security.Version <clinit>
INFO: ELY00001: WildFly Elytron version 1.1.0.Beta21-redhat-1
Led 31, 2017 11:55:24 DOP. org.wildfly.security.elytron.demo.JmsClient main
SEVERE: Naming problem occured
javax.naming.CommunicationException: WFNAM00018: Failed to connect to remote host [Root exception is javax.security.sasl.SaslException: Authentication failed: none of the mechanisms presented by the server are supported]
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentityForNaming(RemoteNamingProvider.java:110)
	at org.wildfly.naming.client.remote.RemoteContext.lookupNative(RemoteContext.java:91)
	at org.wildfly.naming.client.AbstractFederatingContext.lookup(AbstractFederatingContext.java:78)
	at org.wildfly.naming.client.AbstractFederatingContext.lookup(AbstractFederatingContext.java:64)
	at org.wildfly.naming.client.WildFlyRootContext.lookup(WildFlyRootContext.java:123)
	at org.wildfly.naming.client.WildFlyRootContext.lookup(WildFlyRootContext.java:113)
	at javax.naming.InitialContext.lookup(InitialContext.java:417)
	at org.wildfly.security.elytron.demo.JmsClient.main(JmsClient.java:40)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:297)
	at java.lang.Thread.run(Thread.java:745)
Caused by: javax.security.sasl.SaslException: Authentication failed: none of the mechanisms presented by the server are supported
	at org.jboss.remoting3.remote.ClientConnectionOpenListener$Capabilities.handleEvent(ClientConnectionOpenListener.java:412)
	at org.jboss.remoting3.remote.ClientConnectionOpenListener$Capabilities.handleEvent(ClientConnectionOpenListener.java:239)
	at org.xnio.ChannelListeners.invokeChannelListener(ChannelListeners.java:92)
	at org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady(ReadReadyHandler.java:66)
	at org.xnio.nio.NioSocketConduit.handleReady(NioSocketConduit.java:89)
	at org.xnio.nio.WorkerThread.run(WorkerThread.java:567)
	at ...asynchronous invocation...(Unknown Source)
	at org.jboss.remoting3.EndpointImpl.connect(EndpointImpl.java:466)
	at org.jboss.remoting3.FutureConnection.connect(FutureConnection.java:113)
	at org.jboss.remoting3.FutureConnection.init(FutureConnection.java:75)
	at org.jboss.remoting3.FutureConnection.get(FutureConnection.java:151)
	at org.jboss.remoting3.EndpointImpl.getConnection(EndpointImpl.java:422)
	at org.jboss.remoting3.UncloseableEndpoint.getConnection(UncloseableEndpoint.java:57)
	at org.jboss.remoting3.Endpoint.getConnection(Endpoint.java:105)
	at org.wildfly.naming.client.remote.RemoteNamingProvider.lambda$new$0(RemoteNamingProvider.java:68)
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentity(RemoteNamingProvider.java:126)
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentityForNaming(RemoteNamingProvider.java:108)
	... 13 more
```

### Run the JMS client (custom Elytron client configuration)
```bash
-Dmaven.repo.local=/path/to/jboss-eap-7.1.0.Alpha1-maven-repository/maven-repository -Dwildfly.config.url=`pwd`/custom-config.xml
```

#### Result
```
Led 31, 2017 11:58:31 DOP. org.wildfly.naming.client.Version <clinit>
INFO: WildFly Naming version 1.0.0.Beta6-redhat-1
Led 31, 2017 11:58:31 DOP. org.wildfly.security.elytron.demo.JmsClient main
INFO: Attempting to acquire connection factory
Led 31, 2017 11:58:31 DOP. org.jboss.remoting3.EndpointImpl <clinit>
INFO: JBoss Remoting version 5.0.0.Beta12-redhat-1
Led 31, 2017 11:58:31 DOP. org.xnio.Xnio <clinit>
INFO: XNIO version 3.4.3.Final-redhat-1
Led 31, 2017 11:58:31 DOP. org.xnio.nio.NioXnio <clinit>
INFO: XNIO NIO Implementation Version 3.4.3.Final-redhat-1
Led 31, 2017 11:58:31 DOP. org.wildfly.security.Version <clinit>
INFO: ELY00001: WildFly Elytron version 1.1.0.Beta21-redhat-1
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Led 31, 2017 11:58:32 DOP. org.wildfly.security.elytron.demo.JmsClient main
INFO: Found connection factory in JNDI
```