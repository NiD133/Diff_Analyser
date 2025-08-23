package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;

/**
 * Test suite for the {@link ConverterSet} class.
 */
public class ConverterSetTest {

    /**
     * Tests that the copyInto method correctly copies all converters from the set
     * into a destination array.
     */
    @Test
    public void copyInto_shouldCopyAllConvertersIntoProvidedArray() {
        // Arrange: Create a set with a known collection of mock converters.
        // Using a mix of mocks and nulls provides a more robust test case.
        Converter converter1 = mock(Converter.class);
        Converter converter2 = mock(Converter.class);
        Converter[] sourceConverters = {converter1, null, converter2};
        
        ConverterSet converterSet = new ConverterSet(sourceConverters);
        
        // Create a destination array of the correct size to copy into.
        Converter[] destinationArray = new Converter[sourceConverters.length];

        // Act: Call the method under test.
        converterSet.copyInto(destinationArray);

        // Assert: Verify that the destination array now contains the exact same
        // converters in the same order as the source array.
        assertArrayEquals(sourceConverters, destinationArray);
    }
}