package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    @Test
    public void valueOf_shouldReturnSensitiveEnum_whenGivenSensitiveString() {
        // The standard Enum.valueOf() method should correctly map
        // the string "SENSITIVE" to the IOCase.SENSITIVE constant.

        // Arrange
        final IOCase expected = IOCase.SENSITIVE;
        final String inputName = "SENSITIVE";

        // Act
        final IOCase actual = IOCase.valueOf(inputName);

        // Assert
        assertEquals(expected, actual);
    }
}