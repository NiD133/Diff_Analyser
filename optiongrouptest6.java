package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link OptionGroup#toString()} method.
 */
class OptionGroupToStringTest {

    @Test
    @DisplayName("toString() should correctly format a group containing only long options")
    void toStringWithLongOptions() {
        // Arrange
        final OptionGroup group = new OptionGroup();
        group.addOption(new Option(null, "foo", false, "Foo"));
        group.addOption(new Option(null, "bar", false, "Bar"));

        // The order is determined by the insertion order of the options.
        final String expectedString = "[--foo Foo, --bar Bar]";

        // Act
        final String actualString = group.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("toString() should correctly format a group containing short options")
    void toStringWithShortOptions() {
        // Arrange
        final OptionGroup group = new OptionGroup();
        group.addOption(new Option("f", "foo", false, "Foo"));
        group.addOption(new Option("b", "bar", false, "Bar"));

        // The order is determined by insertion order, and the short option name is used.
        final String expectedString = "[-f Foo, -b Bar]";

        // Act
        final String actualString = group.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}