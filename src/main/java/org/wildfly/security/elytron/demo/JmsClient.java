package org.wildfly.security.elytron.demo;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;

/**
 * Simple JMS client which doesn't configure authentication info (username/password).
 */
public class JmsClient {
    // Set up all the default values

    public static void main(String[] args) {
        AuthenticationContext.empty()
                .with(MatchRule.ALL, AuthenticationConfiguration.EMPTY.useDefaultProviders().allowSaslMechanisms("ANONYMOUS"))
                .run(() -> {
                    doLookup("http-remoting://127.0.0.1:8080");
                    doLookup("remote://127.0.0.1:10567");
                });
    }

    private static void doLookup(final String providerUrl) {
        System.out.println();
        System.out.println("Doing JNDI lookup using provider URL: " + providerUrl);
        Context namingContext = null;
        try {

            // Set up the namingContext for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            env.put(Context.PROVIDER_URL, providerUrl);
            env.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
            namingContext = new InitialContext(env);

            // Perform the JNDI lookups
            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("jms/RemoteConnectionFactory");
            System.out.println("Found connection factory: " + (connectionFactory != null));
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (namingContext != null) {
                try {
                    namingContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static {
        Logger.getLogger("org").setLevel(Level.WARNING);
    }
}
