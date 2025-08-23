package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    /**
     * Tests that the select() method returns null when no converter in the set
     * can handle the requested type.
     */
    @Test
    public void selectShouldReturnNullWhenNoConverterSupportsTheGivenType() {
        // Arrange: Create a ConverterSet containing only the NullConverter,
        // which has a supported type of 'null'.
        Converter[] converters = { NullConverter.INSTANCE };
        ConverterSet converterSet = new ConverterSet(converters);

        // Act: Attempt to select a converter for a non-null type (String.class)
        // that is not supported by any converter in the set.
        Converter result = converterSet.select(String.class);

        // Assert: Verify that no converter was found.
        assertNull("Expected select() to return null for an unsupported type", result);
    }
}