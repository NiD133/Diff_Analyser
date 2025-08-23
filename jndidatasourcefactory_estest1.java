package org.apache.ibatis.datasource.jndi;

import org.junit.Test;

import java.util.Properties;

/**
 * Tests for the {@link JndiDataSourceFactory} class.
 */
public class JndiDataSourceFactoryTest {

    /**
     * Verifies that calling setProperties with a null argument
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenPropertiesAreNull() {
        // Arrange
        JndiDataSourceFactory dataSourceFactory = new JndiDataSourceFactory();
        Properties nullProperties = null;

        // Act & Assert
        // This call is expected to throw a NullPointerException, which is
        // verified by the @Test(expected=...) annotation.
        dataSourceFactory.setProperties(nullProperties);
    }
}