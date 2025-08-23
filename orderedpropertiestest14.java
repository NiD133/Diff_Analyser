package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OrderedProperties#toString()}.
 *
 * This test suite focuses on verifying that the string representation of
 * an {@link OrderedProperties} instance correctly reflects the insertion
 * order of its elements.
 */
public class OrderedPropertiesToStringTest {

    /**
     * Verifies that the toString() method generates a string where entries
     * appear in the order they were added.
     */
    @Test
    void toStringShouldPreserveInsertionOrder() {
        // Arrange: Create properties and add entries in a specific, non-alphabetical order (Z down to A).
        final OrderedProperties properties = new OrderedProperties();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            final String key = String.valueOf(ch);
            final String value = "Value" + ch;
            properties.put(key, value);
        }

        // Act
        final String actualToString = properties.toString();

        // Assert: The toString() output should match the insertion order.
        // We build the expected string programmatically to make the test's intent
        // clear and avoid a long, hard-to-read hardcoded string literal.
        final String expectedToString = IntStream.rangeClosed('A', 'Z')
                .mapToObj(i -> (char) i)
                .sorted(Collections.reverseOrder()) // Ensures the order is Z, Y, X...
                .map(ch -> ch + "=Value" + ch)
                .collect(Collectors.joining(", ", "{", "}"));

        assertEquals(expectedToString, actualToString);
    }
}