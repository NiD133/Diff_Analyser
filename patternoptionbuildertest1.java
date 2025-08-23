package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Calendar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
// Renamed from PatternOptionBuilderTestTest1 for clarity
class PatternOptionBuilderTest {

    @Test
    @DisplayName("The '+' pattern should create an object instance for a valid class name and return null for an invalid one.")
    void objectCreatorPattern_shouldCreateObjectForValidClassAndReturnNullForInvalid() throws Exception {
        // Arrange
        // The '+' character in the pattern indicates that the option argument is a class name
        // that should be instantiated via its default constructor.
        final String pattern = "c+d+";
        final Options options = PatternOptionBuilder.parsePattern(pattern);

        final String validClassName = "java.util.Calendar";
        final String invalidClassName = "System.DateTime"; // A non-existent class
        final String[] args = {"-c", validClassName, "-d", invalidClassName};

        final CommandLineParser parser = new PosixParser();

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        // 1. For the valid class name, an object instance should be created.
        final Object createdObject = line.getOptionObject("c");
        assertNotNull(createdObject, "Should create an object for a valid class name.");
        assertInstanceOf(Calendar.class, createdObject, "The created object should be an instance of Calendar.");

        // 2. For the invalid class name, object creation should fail, resulting in null.
        final Object nonExistentObject = line.getOptionObject("d");
        assertNull(nonExistentObject, "Should return null when the class name is invalid or cannot be found.");
    }
}