package org.apache.ibatis.datasource.jndi;

import org.apache.ibatis.datasource.DataSourceException;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.fail;

/**
 * Test suite for JndiDataSourceFactory.
 */
public class JndiDataSourceFactoryTest {

    /**
     * Verifies that setProperties correctly parses properties prefixed with "env."
     * to configure the JNDI InitialContext.
     * <p>
     * The method should handle various key formats, including standard JNDI property keys,
     * keys that are just the prefix, and keys with special characters, without
     * throwing an exception.
     */
    @Test
    public void shouldCorrectlyParsePropertiesWithEnvPrefix() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();

        // Add a standard JNDI property with the "env." prefix.
        // This property will be passed to the InitialContext.
        properties.setProperty("env.java.naming.factory.initial", "com.example.TestInitialContextFactory");

        // Add edge-case properties inspired by the original auto-generated test.
        properties.setProperty("env.", "valueForEmptyKey"); // A key that is exactly the prefix.
        properties.setProperty("env.key$with_special-chars", "anotherValue"); // A key with special characters.

        // Add a non-prefixed property that should be ignored by the JNDI context setup.
        properties.setProperty("data_source", "myDataSource");

        // Act & Assert
        // The setProperties method attempts to create a JNDI InitialContext using the
        // "env." prefixed properties. If the prefix is not stripped correctly, this
        // call may fail with a NamingException (wrapped in a DataSourceException).
        // This test succeeds if the method executes without throwing an exception.
        try {
            factory.setProperties(properties);
            // Success: The properties were parsed without error.
        } catch (DataSourceException e) {
            fail("setProperties should not throw an exception for valid 'env.' prefixed properties. " +
                 "Error: " + e.getMessage());
        }
    }
}