package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link ConverterSet}.
 */
// The original test class name and hierarchy are preserved.
public class ConverterSet_ESTestTest18 extends ConverterSet_ESTest_scaffolding {

    /**
     * Tests that ConverterSet.select() returns null when no converter in the set
     * can handle the requested type.
     */
    @Test
    public void selectShouldReturnNullWhenNoConverterSupportsTheGivenType() {
        // Arrange: Create a ConverterSet with a few standard converters.
        // These converters handle types like Long, ReadableInterval, and ReadableDuration.
        Converter[] converters = new Converter[] {
            ReadableIntervalConverter.INSTANCE,
            LongConverter.INSTANCE,
            ReadableDurationConverter.INSTANCE
        };
        ConverterSet converterSet = new ConverterSet(converters);

        // Define a type that none of the converters in the set are expected to handle.
        // ConverterSet.Entry is an arbitrary but suitable choice for this purpose.
        Class<?> unsupportedType = ConverterSet.Entry.class;

        // Act: Attempt to select a converter for the unsupported type.
        Converter selectedConverter = converterSet.select(unsupportedType);

        // Assert: The result should be null, as no matching converter was found.
        assertNull("Expected no converter to be found for an unsupported type.", selectedConverter);
    }
}