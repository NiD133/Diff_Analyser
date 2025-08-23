package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the factory methods of {@link TransformedSortedMap}.
 *
 * <p>This class focuses on the specific behavior of the factory methods,
 * providing a clear, concrete example of the transformation logic, separate
 * from the generic Map contract tests.</p>
 */
public class TransformedSortedMapFactoryTest {

    @Test
    @DisplayName("transformedSortedMap() should transform existing and new values")
    void transformedSortedMap_whenDecorating_transformsExistingAndNewValues() {
        // Arrange: Create a base map where values are string representations of numbers.
        // The map is typed as <String, Object> to accommodate both the initial String values
        // and the transformed Integer values.
        final SortedMap<String, Object> originalMap = new TreeMap<>();
        originalMap.put("A", "1");
        originalMap.put("B", "2");
        originalMap.put("C", "3");

        // A transformer that converts String values to Integers.
        // It is typed as Transformer<Object, Object> to match the map's value type,
        // making the test fully type-safe.
        final Transformer<Object, Object> valueTransformer = input -> {
            if (input instanceof String) {
                return Integer.valueOf((String) input);
            }
            // For this test, we assume only strings are put as values.
            return input;
        };

        // Act: Create a transformed map using the factory that transforms existing elements.
        final SortedMap<String, Object> transformedMap =
            TransformedSortedMap.transformedSortedMap(originalMap, null, valueTransformer);

        // Assert: Verify that the initial String values were transformed to Integers.
        assertAll("Existing values should be transformed upon creation",
            () -> assertEquals(3, transformedMap.size()),
            () -> assertEquals(1, transformedMap.get("A")),
            () -> assertEquals(2, transformedMap.get("B")),
            () -> assertEquals(3, transformedMap.get("C"))
        );

        // Act: Add a new element with a String value.
        transformedMap.put("D", "4");

        // Assert: Verify that the newly added value was also transformed to an Integer.
        assertAll("New value should be transformed on insertion",
            () -> assertEquals(4, transformedMap.size()),
            () -> assertEquals(4, transformedMap.get("D"))
        );
    }
}