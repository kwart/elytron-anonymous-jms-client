package org.wildfly.security.elytron.demo;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Simple JNDI lookup
 */
public class JmsClient {
    private static final Logger log = Logger.getLogger(JmsClient.class.getName());

    // Set up all the default values
    private static final String INITIAL_CONTEXT_FACTORY = "org.wildfly.naming.client.WildFlyInitialContextFactory";
    // private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";

    public static void main(String[] args) {

        Context namingContext = null;

        try {

            // Set up the namingContext for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
            namingContext = new InitialContext(env);

            // Perform the JNDI lookups
            log.info("Attempting to acquire connection factory");
            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("jms/RemoteConnectionFactory");
            log.info("Found connection factory in JNDI");
        } catch (NamingException e) {
            log.log(Level.SEVERE,"Naming problem occured",e);
        } finally {
            if (namingContext != null) {
                try {
                    namingContext.close();
                } catch (NamingException e) {
                    log.log(Level.SEVERE,"Naming problem occured",e);
                }
            }
        }
    }
}
