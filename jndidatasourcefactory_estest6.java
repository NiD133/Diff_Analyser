package org.apache.ibatis.datasource.jndi;

import org.junit.Test;
import java.util.Properties;

/**
 * Test suite for {@link JndiDataSourceFactory}.
 * This class replaces the auto-generated test to improve clarity and maintainability.
 */
public class JndiDataSourceFactoryTest {

    /**
     * Verifies that the factory can be configured with a property that has an empty value.
     * This is a valid use case, for example, when a configuration property is present but not assigned a value.
     */
    @Test
    public void shouldAcceptPropertyWithEmptyValue() {
        // Arrange: Create a JndiDataSourceFactory and a properties object.
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        Properties properties = new Properties();

        // Set a property with a valid key but an empty string value.
        // This is a more direct and readable way to achieve the same setup as the original test.
        properties.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, "");

        // Act: Call the method under test.
        // The purpose of this test is to ensure that this operation completes without
        // throwing an exception. The test will pass if this line executes successfully.
        factory.setProperties(properties);

        // Assert (Implicit): No exception was thrown, which is the expected outcome.
    }
}