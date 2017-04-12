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
mvn clean package exec:java -Dmaven.repo.local=/path/to/jboss-eap-7.1.0.XXX-maven-repository/maven-repository -Dversion.org.jboss.eap=7.1.0.Beta1-redhat-1
```

#### Result
```
Dub 12, 2017 12:48:55 ODP. org.wildfly.naming.client.Version <clinit>
INFO: WildFly Naming version 1.0.0.Beta13-redhat-1
Dub 12, 2017 12:48:55 ODP. org.wildfly.security.elytron.demo.JmsClient main
INFO: Attempting to acquire connection factory
Dub 12, 2017 12:48:55 ODP. org.xnio.Xnio <clinit>
INFO: XNIO version 3.5.0.Beta4-redhat-1
Dub 12, 2017 12:48:55 ODP. org.xnio.nio.NioXnio <clinit>
INFO: XNIO NIO Implementation Version 3.5.0.Beta4-redhat-1
Dub 12, 2017 12:48:55 ODP. org.jboss.remoting3.EndpointImpl <clinit>
INFO: JBoss Remoting version 5.0.0.Beta19-redhat-1
Dub 12, 2017 12:48:55 ODP. org.wildfly.security.Version <clinit>
INFO: ELY00001: WildFly Elytron version 1.1.0.Beta34-redhat-1
Dub 12, 2017 12:48:56 ODP. org.wildfly.security.elytron.demo.JmsClient main
SEVERE: Naming problem occured
javax.naming.CommunicationException: WFNAM00018: Failed to connect to remote host [Root exception is java.io.IOException: WFNAM00047: Failed to connect to any server]
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentityForNaming(RemoteNamingProvider.java:65)
	at org.wildfly.naming.client.remote.RemoteContext.lambda$lookupNative$0(RemoteContext.java:109)
	at org.wildfly.naming.client.NamingProvider.performExceptionAction(NamingProvider.java:99)
	at org.wildfly.naming.client.remote.RemoteContext.lookupNative(RemoteContext.java:108)
	at org.wildfly.naming.client.AbstractFederatingContext.lookup(AbstractFederatingContext.java:78)
	at org.wildfly.naming.client.AbstractFederatingContext.lookup(AbstractFederatingContext.java:64)
	at org.wildfly.naming.client.WildFlyRootContext.lookup(WildFlyRootContext.java:144)
	at javax.naming.InitialContext.lookup(InitialContext.java:417)
	at org.wildfly.security.elytron.demo.JmsClient.main(JmsClient.java:40)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:297)
	at java.lang.Thread.run(Thread.java:745)
Caused by: java.io.IOException: WFNAM00047: Failed to connect to any server
	at org.wildfly.naming.client.remote.AggregateRemoteNamingProvider.getPeerIdentity(AggregateRemoteNamingProvider.java:75)
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentityForNaming(RemoteNamingProvider.java:63)
	... 14 more
	Suppressed: javax.security.sasl.SaslException: Authentication failed: none of the mechanisms presented by the server (JBOSS-LOCAL-USER, DIGEST-MD5) are supported
		at org.jboss.remoting3.remote.ClientConnectionOpenListener$Capabilities.handleEvent(ClientConnectionOpenListener.java:426)
		at org.jboss.remoting3.remote.ClientConnectionOpenListener$Capabilities.handleEvent(ClientConnectionOpenListener.java:241)
		at org.xnio.ChannelListeners.invokeChannelListener(ChannelListeners.java:92)
		at org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady(ReadReadyHandler.java:66)
		at org.xnio.nio.NioSocketConduit.handleReady(NioSocketConduit.java:89)
		at org.xnio.nio.WorkerThread.run(WorkerThread.java:569)
		at ...asynchronous invocation...(Unknown Source)
		at org.jboss.remoting3.EndpointImpl.connect(EndpointImpl.java:465)
		at org.jboss.remoting3.FutureConnection.getConnection(FutureConnection.java:117)
		at org.jboss.remoting3.FutureConnection.init(FutureConnection.java:77)
		at org.jboss.remoting3.FutureConnection.get(FutureConnection.java:152)
		at org.jboss.remoting3.EndpointImpl.doGetConnection(EndpointImpl.java:407)
		at org.jboss.remoting3.EndpointImpl.getConnection(EndpointImpl.java:341)
		at org.jboss.remoting3.UncloseableEndpoint.getConnection(UncloseableEndpoint.java:55)
		at org.wildfly.naming.client.remote.SingleRemoteNamingProvider.lambda$new$0(SingleRemoteNamingProvider.java:63)
		at java.security.AccessController.doPrivileged(Native Method)
		at org.wildfly.naming.client.remote.SingleRemoteNamingProvider.getPeerIdentity(SingleRemoteNamingProvider.java:87)
		at org.wildfly.naming.client.remote.AggregateRemoteNamingProvider.getPeerIdentity(AggregateRemoteNamingProvider.java:70)
		... 15 more
```

### Run the JMS client (custom Elytron client configuration)

Use `wildfly.config.url` system property to use configuration with JBOSS-LOCAL-USER SASL mechanism enabled. 

```bash
-Dwildfly.config.url=custom-config.xml
```

#### Result
```
Dub 12, 2017 12:59:25 ODP. org.wildfly.naming.client.Version <clinit>
INFO: WildFly Naming version 1.0.0.Beta13-redhat-1
Dub 12, 2017 12:59:25 ODP. org.wildfly.security.elytron.demo.JmsClient main
INFO: Attempting to acquire connection factory
Dub 12, 2017 12:59:25 ODP. org.xnio.Xnio <clinit>
INFO: XNIO version 3.5.0.Beta4-redhat-1
Dub 12, 2017 12:59:25 ODP. org.xnio.nio.NioXnio <clinit>
INFO: XNIO NIO Implementation Version 3.5.0.Beta4-redhat-1
Dub 12, 2017 12:59:25 ODP. org.jboss.remoting3.EndpointImpl <clinit>
INFO: JBoss Remoting version 5.0.0.Beta19-redhat-1
Dub 12, 2017 12:59:25 ODP. org.wildfly.security.Version <clinit>
INFO: ELY00001: WildFly Elytron version 1.1.0.Beta34-redhat-1
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Dub 12, 2017 12:59:27 ODP. org.wildfly.security.elytron.demo.JmsClient main
INFO: Found connection factory in JNDI
```