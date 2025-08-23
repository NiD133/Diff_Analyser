package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the factory method {@link IOCase#forName(String)}.
 */
class IOCaseTest {

    @DisplayName("forName should return the correct IOCase for valid names")
    @ParameterizedTest(name = "IOCase.forName(\"{0}\") should return IOCase.{1}")
    @CsvSource({
        "Sensitive,   SENSITIVE",
        "Insensitive, INSENSITIVE",
        "System,      SYSTEM"
    })
    void forName_withValidName_shouldReturnCorrespondingEnum(final String name, final IOCase expected) {
        // When
        final IOCase result = IOCase.forName(name);

        // Then
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("forName should throw IllegalArgumentException for an unknown name")
    void forName_withUnknownName_shouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> IOCase.forName("UnknownName"));
    }

    @Test
    @DisplayName("forName should throw IllegalArgumentException for a null name")
    void forName_withNullName_shouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> IOCase.forName(null));
    }
}