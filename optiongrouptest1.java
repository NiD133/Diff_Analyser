package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link OptionGroup#getNames()} method.
 */
@DisplayName("OptionGroup Test")
class OptionGroupTest {

    @Nested
    @DisplayName("getNames method")
    class GetNamesTest {

        @Test
        @DisplayName("should return an empty collection when the group is empty")
        void shouldReturnEmptyCollectionForEmptyGroup() {
            // Arrange
            final OptionGroup group = new OptionGroup();

            // Act
            final Collection<String> names = group.getNames();

            // Assert
            assertNotNull(names, "The names collection should not be null.");
            assertTrue(names.isEmpty(), "The names collection should be empty for a new group.");
        }

        @Test
        @DisplayName("should return all option names in insertion order")
        void shouldReturnAllOptionNamesInOrder() {
            // Arrange
            final OptionGroup group = new OptionGroup();
            group.addOption(new Option("a", "alpha", false, "First option"));
            group.addOption(new Option("b", "beta", false, "Second option"));

            final List<String> expectedNames = List.of("a", "b");

            // Act
            final Collection<String> actualNames = group.getNames();

            // Assert
            // The internal implementation uses a LinkedHashMap, so insertion order is preserved.
            // assertIterableEquals checks for content, size, and order in a single call.
            assertIterableEquals(expectedNames, actualNames);
        }
    }
}