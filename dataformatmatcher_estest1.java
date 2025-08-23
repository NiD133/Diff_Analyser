package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the DataFormatMatcher class.
 */
public class DataFormatMatcherTest {

    /**
     * Tests that the getters for the matched factory and match strength
     * return the values provided during the matcher's creation.
     */
    @Test
    public void shouldReturnMatchAndStrengthProvidedOnCreation() {
        // Arrange
        // An empty input stream and buffer are sufficient, as we are only testing
        // the state of the DataFormatMatcher, not the detection logic itself.
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] buffer = new byte[8];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(inputStream, buffer);

        JsonFactory expectedJsonFactory = new JsonFactory();
        MatchStrength expectedMatchStrength = MatchStrength.SOLID_MATCH;

        // Act
        // The createMatcher method acts as a factory for the DataFormatMatcher.
        DataFormatMatcher matcher = inputAccessor.createMatcher(expectedJsonFactory, expectedMatchStrength);

        // Assert
        // Verify that the matcher correctly stores and returns the provided factory and strength.
        JsonFactory actualJsonFactory = matcher.getMatch();
        MatchStrength actualMatchStrength = matcher.getMatchStrength();

        assertSame("The matched JsonFactory should be the same instance provided at creation.",
                expectedJsonFactory, actualJsonFactory);
        assertEquals("The match strength should be the one provided at creation.",
                expectedMatchStrength, actualMatchStrength);
    }
}