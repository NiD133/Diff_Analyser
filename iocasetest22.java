package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the {@link IOCase#toString()} method.
 */
class IOCaseToStringTest {

    @ParameterizedTest(name = "[{index}] {0}.toString() should return \"{1}\"")
    @CsvSource({
        "SENSITIVE, Sensitive",
        "INSENSITIVE, Insensitive",
        "SYSTEM, System"
    })
    void toString_shouldReturnTheCorrectName(final IOCase ioCase, final String expectedName) {
        // Act
        final String actualName = ioCase.toString();

        // Assert
        assertEquals(expectedName, actualName);
    }
}