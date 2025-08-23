package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

// This test class retains the original name and scaffolding for compatibility with the existing test suite.
public class GroupedRandomAccessSource_ESTestTest6 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that the length() method of GroupedRandomAccessSource correctly calculates
     * the total length by summing the lengths of all its constituent sources,
     * even when one of the sources reports a negative length. This is an important
     * edge case for the summation logic.
     */
    @Test(timeout = 4000)
    public void length_shouldReturnSumOfSourceLengths_whenOneSourceHasNegativeLength() throws IOException {
        // --- Arrange ---

        // 1. Define the lengths for our test sources.
        final int positiveSourceLength = 11;
        final long negativeSourceLength = -2024L;
        final int numberOfPositiveSources = 8;

        // 2. Create a base source with a positive length. This will be used to create
        //    the other sources.
        RandomAccessSource baseSource = new ArrayRandomAccessSource(new byte[positiveSourceLength]);

        // 3. Create a special source that is configured to report a negative length.
        //    A WindowRandomAccessSource allows this and is used to test the edge case.
        RandomAccessSource negativeLengthSource = new WindowRandomAccessSource(baseSource, 0, negativeSourceLength);
        
        // Precondition check: Ensure our negative-length source is configured correctly.
        assertEquals("Precondition failed: negativeLengthSource should report a negative length.",
                negativeSourceLength, negativeLengthSource.length());

        // 4. Assemble an array of sources, including multiple positive-length sources
        //    and the single negative-length source.
        RandomAccessSource[] sources = new RandomAccessSource[numberOfPositiveSources + 1];
        for (int i = 0; i < numberOfPositiveSources; i++) {
            // The original test used a mix of ArrayRandomAccessSource and GetBufferedRandomAccessSource,
            // but for testing length(), only the reported length matters, not the source type.
            // We use a simple ArrayRandomAccessSource for clarity.
            sources[i] = new ArrayRandomAccessSource(new byte[positiveSourceLength]);
        }
        sources[numberOfPositiveSources] = negativeLengthSource; // Add the negative source.

        // 5. Calculate the expected total length.
        long expectedTotalLength = (long) numberOfPositiveSources * positiveSourceLength + negativeSourceLength;
        // Calculation: (8 * 11) + (-2024) = 88 - 2024 = -1936

        // --- Act ---

        // Create the GroupedRandomAccessSource with our collection of sources.
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        long actualTotalLength = groupedSource.length();

        // --- Assert ---

        // Verify that the calculated length matches the expected sum.
        assertEquals("The total length should be the sum of all source lengths, including the negative one.",
                expectedTotalLength, actualTotalLength);
    }
}