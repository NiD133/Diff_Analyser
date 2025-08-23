package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the forEach method of OrderedProperties to ensure it respects insertion order.
 */
@DisplayName("OrderedProperties.forEach()")
class OrderedPropertiesForEachTest {

    private OrderedProperties orderedProperties;
    private final List<String> expectedKeys = new ArrayList<>();

    @BeforeEach
    void setUp() {
        orderedProperties = new OrderedProperties();
        // Properties are added in reverse alphabetical order.
        // We expect forEach to iterate in this same order.
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            final String key = String.valueOf(ch);
            final String value = "Value" + ch;
            expectedKeys.add(key);
            orderedProperties.put(key, value);
        }
    }

    @Test
    @DisplayName("should iterate through elements in their insertion order")
    void testForEachShouldIterateInInsertionOrder() {
        // Arrange: Create a list to store the entries from the forEach loop.
        final List<Entry<Object, Object>> actualEntries = new ArrayList<>();

        // Act: Execute the forEach method and collect the results.
        orderedProperties.forEach((key, value) -> actualEntries.add(new SimpleEntry<>(key, value)));

        // Assert: Verify that the collected entries match the expected insertion order.
        assertEquals(expectedKeys.size(), actualEntries.size(), "The number of iterated items should match the number of inserted items.");

        for (int i = 0; i < expectedKeys.size(); i++) {
            final String expectedKey = expectedKeys.get(i);
            final String expectedValue = "Value" + expectedKey;
            
            final Entry<Object, Object> actualEntry = actualEntries.get(i);

            assertEquals(expectedKey, actualEntry.getKey(), "The key at index " + i + " should match the insertion order.");
            assertEquals(expectedValue, actualEntry.getValue(), "The value at index " + i + " should match the insertion order.");
        }
    }
}