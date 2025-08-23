package org.apache.commons.cli;

import org.junit.Test;
import java.net.URL;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link PatternOptionBuilder} class.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the pattern character '/' correctly maps to the URL class.
     * According to the PatternOptionBuilder documentation, the '/' character
     * is used to specify that an option's argument is a URL.
     */
    @Test
    public void getValueType_shouldReturnURLClass_forSlashCharacter() {
        // Given the pattern character for a URL type
        final char urlPatternChar = '/';

        // When retrieving the value type associated with that character
        final Class<?> valueType = PatternOptionBuilder.getValueType(urlPatternChar);

        // Then the returned type should be the URL class
        assertEquals(URL.class, valueType);
    }
}