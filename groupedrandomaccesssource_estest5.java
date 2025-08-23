package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * This class contains tests for the GroupedRandomAccessSource.
 * The original test class name is kept for context, but in a real-world scenario,
 * it would be renamed to GroupedRandomAccessSourceTest.
 */
public class GroupedRandomAccessSource_ESTestTest5 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that the length() method of a GroupedRandomAccessSource
     * correctly returns the sum of the lengths of its constituent sources.
     */
    @Test
    public void length_whenCreatedFromMultipleSources_shouldReturnSumOfTheirLengths() throws IOException {
        // Arrange: Define the properties of the component sources.
        final int individualSourceLength = 5;
        final int numberOfSources = 3;
        final long expectedTotalLength = (long) individualSourceLength * numberOfSources;

        // Create an array of sources to be grouped.
        RandomAccessSource[] componentSources = new RandomAccessSource[numberOfSources];
        for (int i = 0; i < numberOfSources; i++) {
            byte[] sourceData = new byte[individualSourceLength];
            componentSources[i] = new ArrayRandomAccessSource(sourceData);
        }

        // Act: Create the GroupedRandomAccessSource and get its length.
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(componentSources);
        long actualLength = groupedSource.length();

        // Assert: The calculated length should match the expected total length.
        assertEquals(expectedTotalLength, actualLength);
    }
}