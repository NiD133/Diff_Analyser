package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the order-preserving behavior of {@link OrderedProperties} when loading from a file.
 */
@DisplayName("OrderedProperties file loading")
class OrderedPropertiesLoadTest {

    private static final String ASC_PROPERTIES_FILE = "test.properties";
    private static final String DESC_PROPERTIES_FILE = "test-reverse.properties";
    private static final String KEY_PREFIX = "key";
    private static final String VALUE_PREFIX = "value";
    private static final int START_INDEX = 1;
    private static final int END_INDEX = 11;

    private OrderedProperties orderedProperties;

    @BeforeEach
    void setUp() {
        orderedProperties = new OrderedProperties();
    }

    @Test
    @DisplayName("Should preserve insertion order when loading a standard properties file")
    void loadShouldPreserveOrderOfAscendingPropertiesFile() throws IOException {
        // Arrange: Define the expected order of keys as they appear in the file.
        final List<String> expectedKeys = IntStream.rangeClosed(START_INDEX, END_INDEX)
            .mapToObj(i -> KEY_PREFIX + i)
            .collect(Collectors.toList());

        // Act: Load the properties from the file.
        loadProperties(ASC_PROPERTIES_FILE);

        // Assert: Verify that all views of the properties reflect the expected order.
        assertPropertyOrderIsConsistent(expectedKeys);
    }

    @Test
    @DisplayName("Should preserve insertion order when loading a reverse-ordered properties file")
    void loadShouldPreserveOrderOfDescendingPropertiesFile() throws IOException {
        // Arrange: Define the expected order of keys (descending).
        final List<String> expectedKeys = IntStream.rangeClosed(START_INDEX, END_INDEX)
            .map(i -> END_INDEX - i + START_INDEX) // Reverses the range, e.g., 11, 10, ..., 1
            .mapToObj(i -> KEY_PREFIX + i)
            .collect(Collectors.toList());

        // Act: Load the properties from the file with reversed keys.
        loadProperties(DESC_PROPERTIES_FILE);

        // Assert: Verify that all views of the properties reflect the expected order.
        assertPropertyOrderIsConsistent(expectedKeys);
    }

    /**
     * Loads properties from a resource file located in the same package as this test class.
     *
     * @param resourceName The name of the properties file.
     * @throws IOException If an I/O error occurs.
     */
    private void loadProperties(final String resourceName) throws IOException {
        try (InputStream inputStream = OrderedPropertiesLoadTest.class.getResourceAsStream(resourceName)) {
            assertNotNull(inputStream, "Test resource not found: " + resourceName);
            // Properties files are specified to be ISO-8859-1 encoded.
            orderedProperties.load(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));
        }
    }

    /**
     * Asserts that the keys and values in the properties object match the expected order across all
     * relevant views (keySet, entrySet, etc.).
     *
     * @param expectedKeys The list of keys in their expected order.
     */
    private void assertPropertyOrderIsConsistent(final List<String> expectedKeys) {
        final List<String> expectedValues = expectedKeys.stream()
            .map(key -> VALUE_PREFIX + key.substring(KEY_PREFIX.length()))
            .collect(Collectors.toList());

        assertAll("All property views should be consistently ordered",
            () -> assertIterableEquals(expectedKeys, new ArrayList<>(orderedProperties.keySet()),
                "keySet() should be in insertion order."),

            () -> {
                final List<Object> actualKeysFromEntrySet = orderedProperties.entrySet().stream()
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
                assertIterableEquals(expectedKeys, actualKeysFromEntrySet,
                    "entrySet() keys should be in insertion order.");
            },

            () -> {
                final List<Object> actualValuesFromEntrySet = orderedProperties.entrySet().stream()
                    .map(entry -> entry.getValue())
                    .collect(Collectors.toList());
                assertIterableEquals(expectedValues, actualValuesFromEntrySet,
                    "entrySet() values should be in insertion order.");
            },

            () -> assertIterableEquals(expectedKeys, Collections.list(orderedProperties.keys()),
                "keys() enumeration should be in insertion order."),

            () -> assertIterableEquals(expectedKeys, Collections.list(orderedProperties.propertyNames()),
                "propertyNames() enumeration should be in insertion order.")
        );
    }
}