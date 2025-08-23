package org.apache.commons.collections4.properties;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import org.junit.Test;

/**
 * Unit tests for the {@link OrderedProperties} class.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that calling putAll() with an empty map on an empty OrderedProperties
     * instance does not change its state.
     */
    @Test
    public void putAllWithEmptyMapOnEmptyPropertiesShouldRemainEmpty() {
        // Arrange: Create an empty OrderedProperties instance.
        final OrderedProperties properties = new OrderedProperties();
        final Map<Object, Object> emptyMap = Collections.emptyMap();

        // Act: Call the method under test with an empty map.
        properties.putAll(emptyMap);

        // Assert: Verify that the properties object is still empty.
        assertTrue("Calling putAll with an empty map should not add any elements", properties.isEmpty());
    }
}