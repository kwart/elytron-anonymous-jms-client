# JBEAP-10506 Reproducer - programmatic naming client configuration

This reproducer shows problem with using `AuthenticationContext` API.

The client works when the configuration is specified in `wildfly-config.xml` on classpath. If the config file is removed and the same setting is configured by using `AuthenticationConfiguration`\ `AuthenticationContext` then client fails (as it does not use the correct SASL mechanism).

## How to run it
* edit [pom.xml](pom.xml) - set correct path to JBoss EAP maven repository and fix update JMS client bom version in property `version.org.jboss.eap` (if necessary)
* put the [standalone-full.xml](standalone-full.xml) configuration file to your server config directory (`standalone/configuration/")
* start the profile
```bash
bin/standalone.sh -c standalone-full.xml
```
* run this client project
```bash
mvn clean package exec:java
```

[The client code](src/main/java/org/wildfly/security/elytron/demo/JmsClient.java) makes just a simple JNDI lookup. 

## Expected output
```
Doing JNDI lookup using provider URL: http-remoting://127.0.0.1:8080
Found connection factory: true

Doing JNDI lookup using provider URL: remote://127.0.0.1:10567
Found connection factory: true
```

## Current ouptut
```
Doing JNDI lookup using provider URL: http-remoting://127.0.0.1:8080
javax.naming.CommunicationException: WFNAM00018: Failed to connect to remote host [Root exception is java.io.IOException: WFNAM00047: Failed to connect to any server]
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentityForNaming(RemoteNamingProvider.java:65)
	at org.wildfly.naming.client.remote.RemoteContext.lambda$lookupNative$0(RemoteContext.java:109)
	at org.wildfly.naming.client.NamingProvider.performExceptionAction(NamingProvider.java:99)
	at org.wildfly.naming.client.remote.RemoteContext.lookupNative(RemoteContext.java:108)
	at org.wildfly.naming.client.AbstractFederatingContext.lookup(AbstractFederatingContext.java:78)
	at org.wildfly.naming.client.AbstractFederatingContext.lookup(AbstractFederatingContext.java:64)
	at org.wildfly.naming.client.WildFlyRootContext.lookup(WildFlyRootContext.java:144)
	at javax.naming.InitialContext.lookup(InitialContext.java:417)
	at org.wildfly.security.elytron.demo.JmsClient.doLookup(JmsClient.java:48)
	at org.wildfly.security.elytron.demo.JmsClient.lambda$main$0(JmsClient.java:27)
	at org.wildfly.common.context.Contextual.run(Contextual.java:73)
	at org.wildfly.security.elytron.demo.JmsClient.main(JmsClient.java:26)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:297)
	at java.lang.Thread.run(Thread.java:745)
Caused by: java.io.IOException: WFNAM00047: Failed to connect to any server
	at org.wildfly.naming.client.remote.AggregateRemoteNamingProvider.getPeerIdentity(AggregateRemoteNamingProvider.java:75)
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentityForNaming(RemoteNamingProvider.java:63)
	... 17 more
	Suppressed: javax.security.sasl.SaslException: Authentication failed: none of the mechanisms presented by the server (ANONYMOUS) are supported
		at org.jboss.remoting3.remote.ClientConnectionOpenListener$Capabilities.handleEvent(ClientConnectionOpenListener.java:426)
		at org.jboss.remoting3.remote.ClientConnectionOpenListener$Capabilities.handleEvent(ClientConnectionOpenListener.java:241)
		at org.xnio.ChannelListeners.invokeChannelListener(ChannelListeners.java:92)
		at org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady(ReadReadyHandler.java:66)
		at org.xnio.nio.NioSocketConduit.handleReady(NioSocketConduit.java:89)
		at org.xnio.nio.WorkerThread.run(WorkerThread.java:567)
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
		... 18 more

Doing JNDI lookup using provider URL: remote://127.0.0.1:10567
javax.naming.CommunicationException: WFNAM00018: Failed to connect to remote host [Root exception is java.io.IOException: WFNAM00047: Failed to connect to any server]
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentityForNaming(RemoteNamingProvider.java:65)
	at org.wildfly.naming.client.remote.RemoteContext.lambda$lookupNative$0(RemoteContext.java:109)
	at org.wildfly.naming.client.NamingProvider.performExceptionAction(NamingProvider.java:99)
	at org.wildfly.naming.client.remote.RemoteContext.lookupNative(RemoteContext.java:108)
	at org.wildfly.naming.client.AbstractFederatingContext.lookup(AbstractFederatingContext.java:78)
	at org.wildfly.naming.client.AbstractFederatingContext.lookup(AbstractFederatingContext.java:64)
	at org.wildfly.naming.client.WildFlyRootContext.lookup(WildFlyRootContext.java:144)
	at javax.naming.InitialContext.lookup(InitialContext.java:417)
	at org.wildfly.security.elytron.demo.JmsClient.doLookup(JmsClient.java:48)
	at org.wildfly.security.elytron.demo.JmsClient.lambda$main$0(JmsClient.java:28)
	at org.wildfly.common.context.Contextual.run(Contextual.java:73)
	at org.wildfly.security.elytron.demo.JmsClient.main(JmsClient.java:26)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:297)
	at java.lang.Thread.run(Thread.java:745)
Caused by: java.io.IOException: WFNAM00047: Failed to connect to any server
	at org.wildfly.naming.client.remote.AggregateRemoteNamingProvider.getPeerIdentity(AggregateRemoteNamingProvider.java:75)
	at org.wildfly.naming.client.remote.RemoteNamingProvider.getPeerIdentityForNaming(RemoteNamingProvider.java:63)
	... 17 more
	Suppressed: javax.security.sasl.SaslException: Authentication failed: none of the mechanisms presented by the server (ANONYMOUS) are supported
		at org.jboss.remoting3.remote.ClientConnectionOpenListener$Capabilities.handleEvent(ClientConnectionOpenListener.java:426)
		at org.jboss.remoting3.remote.ClientConnectionOpenListener$Capabilities.handleEvent(ClientConnectionOpenListener.java:241)
		at org.xnio.ChannelListeners.invokeChannelListener(ChannelListeners.java:92)
		at org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady(ReadReadyHandler.java:66)
		at org.xnio.nio.NioSocketConduit.handleReady(NioSocketConduit.java:89)
		at org.xnio.nio.WorkerThread.run(WorkerThread.java:567)
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
		... 18 more
```
