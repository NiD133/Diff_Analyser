package org.jsoup.helper;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the utility method {@link W3CDom#propertiesFromMap(Map)}.
 */
public class W3CDomTest {

    @Test
    public void propertiesFromMapWithEmptyMapReturnsEmptyProperties() {
        // Arrange: Create an empty map.
        Map<String, String> emptyMap = new HashMap<>();

        // Act: Convert the empty map to a Properties object.
        Properties properties = W3CDom.propertiesFromMap(emptyMap);

        // Assert: The resulting Properties object should also be empty.
        assertTrue("The resulting properties should be empty for an empty map input.", properties.isEmpty());
    }

    @Test
    public void propertiesFromMapConvertsAllEntries() {
        // Arrange: Create a map with some sample data.
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put("encoding", "UTF-8");
        sourceMap.put("method", "xml");
        sourceMap.put("indent", "yes");

        // Act: Convert the map to a Properties object.
        Properties properties = W3CDom.propertiesFromMap(sourceMap);

        // Assert: The Properties object should contain the same key-value pairs.
        assertEquals("The number of properties should match the number of map entries.", 3, properties.size());
        assertEquals("The 'encoding' property should be correctly transferred.", "UTF-8", properties.getProperty("encoding"));
        assertEquals("The 'method' property should be correctly transferred.", "xml", properties.getProperty("method"));
        assertEquals("The 'indent' property should be correctly transferred.", "yes", properties.getProperty("indent"));
    }
}