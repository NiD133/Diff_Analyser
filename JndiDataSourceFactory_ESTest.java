package org.apache.ibatis.datasource.jndi;

import org.apache.ibatis.datasource.DataSourceException;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;
import java.util.Properties;

import static org.apache.ibatis.datasource.jndi.JndiDataSourceFactory.*;
import static org.junit.Assert.*;

/**
 * Tests for {@link JndiDataSourceFactory}.
 *
 * This test suite focuses on how the factory handles various property configurations
 * and its behavior when JNDI lookups are triggered.
 */
public class JndiDataSourceFactoryTest {

    @Test
    public void getDataSourceShouldReturnNullByDefault() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();

        // Act
        DataSource dataSource = factory.getDataSource();

        // Assert
        assertNull("DataSource should be null before properties are set", dataSource);
    }

    @Test(expected = NullPointerException.class)
    public void setPropertiesShouldThrowNullPointerExceptionForNullProperties() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();

        // Act
        factory.setProperties(null);

        // Assert (handled by @Test(expected=...))
    }

    @Test(expected = ClassCastException.class)
    public void setPropertiesShouldThrowClassCastExceptionForNonStringPropertyKeys() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        // The implementation expects String keys, so we use a non-String key to trigger the exception.
        properties.put(new Object(), "someValue");

        // Act
        factory.setProperties(properties);

        // Assert (handled by @Test(expected=...))
    }

    @Test
    public void setPropertiesShouldSucceedWhenOnlyEnvPropertiesAreProvided() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(ENV_PREFIX + Context.INITIAL_CONTEXT_FACTORY, "some.fake.factory.InitialContextFactory");

        // Act
        factory.setProperties(properties);

        // Assert
        // The factory should not create a data source, as the 'data_source' property is missing.
        assertNull(factory.getDataSource());
    }
    
    @Test
    public void setPropertiesShouldSucceedWhenOnlyInitialContextIsProvided() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(INITIAL_CONTEXT, "some.fake.InitialContextFactory");

        // Act
        factory.setProperties(properties);

        // Assert
        // The factory should not create a data source, as the 'data_source' property is missing.
        assertNull(factory.getDataSource());
    }

    @Test
    public void setPropertiesShouldThrowExceptionWhenDataSourceIsSetButNoJndiProviderIsConfigured() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(DATA_SOURCE, "java:comp/env/jdbc/testdb");

        // Act & Assert
        try {
            factory.setProperties(properties);
            fail("Should have thrown a DataSourceException due to missing JNDI provider.");
        } catch (DataSourceException e) {
            // The factory should wrap the underlying NamingException.
            assertTrue("Cause should be a NamingException", e.getCause() instanceof NamingException);
            assertTrue("Specific cause should be NoInitialContextException", e.getCause() instanceof NoInitialContextException);
            assertTrue(e.getMessage().contains("NoInitialContextException"));
        }
    }

    @Test
    public void setPropertiesShouldThrowExceptionOnJndiInitializationFailure() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();
        // Providing both initial_context and data_source properties triggers the JNDI lookup.
        // In a standard test environment without a JNDI provider, creating the InitialContext will fail.
        properties.setProperty(INITIAL_CONTEXT, "some.context");
        properties.setProperty(DATA_SOURCE, "java:comp/env/jdbc/nonexistent");

        // Act & Assert
        try {
            factory.setProperties(properties);
            fail("Should have thrown a DataSourceException due to JNDI initialization failure.");
        } catch (DataSourceException e) {
            assertTrue("Cause should be a NamingException", e.getCause() instanceof NamingException);
            assertTrue("Specific cause should be NoInitialContextException", e.getCause() instanceof NoInitialContextException);
        }
    }
}