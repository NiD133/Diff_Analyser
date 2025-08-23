package org.apache.commons.collections4.properties;

import org.junit.Test;

/**
 * Tests for the {@link OrderedProperties#forEach(BiConsumer)} method.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that forEach() throws a NullPointerException when the provided action is null,
     * as specified by the java.util.Map interface contract.
     */
    @Test(expected = NullPointerException.class)
    public void forEachShouldThrowNullPointerExceptionWhenActionIsNull() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();

        // Act & Assert
        // This call is expected to throw a NullPointerException.
        properties.forEach(null);
    }
}