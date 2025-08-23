package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    @Test
    public void select_shouldReturnCorrectConverter_forExactTypeMatch() {
        // Arrange: Create a set of various converters.
        Converter expectedConverter = LongConverter.INSTANCE;
        
        Converter[] converters = {
            expectedConverter,
            new ReadablePartialConverter(),
            new CalendarConverter(),
            new ReadableIntervalConverter(),
            new ReadableIntervalConverter() // Include a duplicate to test robustness
        };
        
        ConverterSet converterSet = new ConverterSet(converters);
        
        // Act: Select a converter for the Long type.
        Converter actualConverter = converterSet.select(Long.class);
        
        // Assert: Verify that the specific LongConverter instance was returned.
        assertSame("The selected converter should be the singleton LongConverter instance",
                     expectedConverter, actualConverter);
    }
}