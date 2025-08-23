package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This test class is intentionally left with its original name to reflect the refactoring process.
 * The test cases within, however, have been improved for clarity.
 */
public class ClusteredXYBarRenderer_ESTestTest1 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Tests the behavior of findDomainBoundsWithOffset when the dataset contains a bin
     * with a lower bound of negative infinity. This is an important edge case for
     * floating-point calculations.
     */
    @Test
    public void findDomainBoundsWithOffset_withNegativeInfinityBin_returnsCorrectRange() {
        // Arrange: Create a renderer and a dataset with a single bin starting at
        // Double.NEGATIVE_INFINITY.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        // Use a descriptive key for the dataset series instead of an arbitrary integer.
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("Test Series");
        
        // This bin represents the edge case: an interval starting at negative infinity.
        SimpleHistogramBin binWithNegativeInfinity = new SimpleHistogramBin(
                Double.NEGATIVE_INFINITY, 0.0);
        dataset.addBin(binWithNegativeInfinity);

        // Act: Calculate the domain bounds using the method under test.
        Range resultRange = renderer.findDomainBoundsWithOffset(dataset);

        // Assert: Verify the behavior for this specific edge case.
        // The internal calculations with a negative-infinite lower bound are expected
        // to produce a range of [-Infinity, -Infinity].
        assertNotNull("The resulting range should not be null", resultRange);

        // A range of [-Infinity, -Infinity] has a length of (-Infinity - (-Infinity)),
        // which results in NaN according to IEEE 754 floating-point rules.
        assertEquals("Range length should be NaN for an infinite-to-infinite range",
                Double.NaN, resultRange.getLength(), 0.0);
        
        // The central value of a [-Infinity, -Infinity] range is also -Infinity.
        // Calculation: (lower/2 + upper/2) => (-inf/2 + -inf/2) => -inf
        assertEquals("Central value should be negative infinity",
                Double.NEGATIVE_INFINITY, resultRange.getCentralValue(), 0.0);
    }
}