package org.apache.ibatis.datasource.jndi;

import java.util.Properties;
import org.junit.Test;

/**
 * Tests for the JndiDataSourceFactory class.
 */
public class JndiDataSourceFactoryTest {

    /**
     * The setProperties method iterates over the given properties, casting each key to a String.
     * This test verifies that a ClassCastException is thrown if a key is not a String,
     * ensuring the method fails fast with invalid input.
     */
    @Test(expected = ClassCastException.class)
    public void setPropertiesShouldThrowClassCastExceptionForNonStringKey() {
        // Arrange
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties invalidProperties = new Properties();

        // The Properties object allows non-String keys, but the factory's implementation
        // expects String keys. We use a generic Object to represent any non-String key.
        Object nonStringKey = new Object();
        invalidProperties.put(nonStringKey, "any-value");

        // Act & Assert
        // This call is expected to throw a ClassCastException because the internal
        // implementation attempts to cast the non-String key.
        factory.setProperties(invalidProperties);
    }
}