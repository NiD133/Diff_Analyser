package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link OrderedProperties} to ensure that all iteration methods
 * respect the original insertion order of properties.
 */
public class OrderedPropertiesTest {

    private static final int TEST_PROPERTY_COUNT = 10;

    /**
     * Creates a standard set of properties for testing, with keys "key1", "key2", etc.
     * in a predictable insertion order.
     *
     * @return An {@link OrderedProperties} instance populated with test data.
     */
    private OrderedProperties createTestProperties() {
        final OrderedProperties properties = new OrderedProperties();
        for (int i = 1; i <= TEST_PROPERTY_COUNT; i++) {
            properties.put("key" + i, "value" + i);
        }
        return properties;
    }

    /**
     * Provides a stream of arguments for the parameterized test, covering the different
     * ways to retrieve an ordered view of keys.
     *
     * @return A stream of {@link Arguments} for {@link #keyViewsShouldIterateInInsertionOrder}.
     */
    static Stream<Arguments> orderedKeyViewsProvider() {
        return Stream.of(
            Arguments.of("keySet()", (Function<OrderedProperties, Iterable<?>>) OrderedProperties::keySet),
            Arguments.of("keys()", (Function<OrderedProperties, Iterable<?>>) p -> Collections.list(p.keys())),
            Arguments.of("propertyNames()", (Function<OrderedProperties, Iterable<?>>) p -> Collections.list(p.propertyNames()))
        );
    }

    @ParameterizedTest(name = "View from {0} should iterate in insertion order")
    @MethodSource("orderedKeyViewsProvider")
    void keyViewsShouldIterateInInsertionOrder(final String viewName, final Function<OrderedProperties, Iterable<?>> viewExtractor) {
        // Arrange
        final OrderedProperties properties = createTestProperties();
        final List<String> expectedKeys = IntStream.rangeClosed(1, TEST_PROPERTY_COUNT)
                .mapToObj(i -> "key" + i)
                .collect(Collectors.toList());

        // Act
        final Iterable<?> actualKeys = viewExtractor.apply(properties);

        // Assert
        assertIterableEquals(expectedKeys, actualKeys);
    }

    @Test
    void entrySetShouldIterateInInsertionOrder() {
        // Arrange
        final OrderedProperties properties = createTestProperties();
        final List<String> expectedKeys = IntStream.rangeClosed(1, TEST_PROPERTY_COUNT)
                .mapToObj(i -> "key" + i)
                .collect(Collectors.toList());
        final List<String> expectedValues = IntStream.rangeClosed(1, TEST_PROPERTY_COUNT)
                .mapToObj(i -> "value" + i)
                .collect(Collectors.toList());

        // Act
        final Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
        final List<Object> actualKeys = entrySet.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        final List<Object> actualValues = entrySet.stream().map(Map.Entry::getValue).collect(Collectors.toList());

        // Assert
        assertEquals(expectedKeys, actualKeys, "Keys from entrySet should be in insertion order");
        assertEquals(expectedValues, actualValues, "Values from entrySet should be in insertion order");
    }

    @Test
    void loadShouldPreserveOrderFromFile() throws IOException {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        // The test resource file 'test-reverse.properties' is expected to contain
        // keys from key11 down to key1.
        final List<String> expectedKeys = new ArrayList<>();
        for (int i = 11; i >= 1; i--) {
            expectedKeys.add("key" + i);
        }

        // Act
        try (FileReader reader = new FileReader("src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties")) {
            properties.load(reader);
        }

        // Assert
        final List<Object> actualKeys = new ArrayList<>(properties.keySet());
        assertEquals(expectedKeys, actualKeys, "Keys loaded from file should be in file order");
    }
}