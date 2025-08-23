package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A collection of tests for the findDomainBounds() method in the
 * {@link ClusteredXYBarRenderer} class.
 */
@DisplayName("ClusteredXYBarRenderer.findDomainBounds")
class ClusteredXYBarRendererFindDomainBoundsTest {

    private static final double EPSILON = 0.0000000001;
    private XYDataset<String> dataset;

    @BeforeEach
    void setUp() {
        this.dataset = createSampleDataset();
    }

    /**
     * Verifies that for a default renderer, the domain bounds correspond directly
     * to the minimum and maximum interval values in the dataset.
     */
    @Test
    @DisplayName("Should return the actual data interval range for a default renderer")
    void findDomainBounds_withDefaultRenderer_returnsCorrectRange() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        // For the sample dataset, the lowest x-start is 0.9 and the highest x-end is 13.1.

        // Act
        Range domainBounds = renderer.findDomainBounds(dataset);

        // Assert
        assertNotNull(domainBounds, "The returned range should not be null for a valid dataset.");
        assertEquals(0.9, domainBounds.getLowerBound(), EPSILON,
                "Lower bound should match the minimum start-x value in the dataset.");
        assertEquals(13.1, domainBounds.getUpperBound(), EPSILON,
                "Upper bound should match the maximum end-x value in the dataset.");
    }

    /**
     * Verifies that when 'centerBarAtStartValue' is true, the domain bounds are
     * calculated based on an offset interval.
     */
    @Test
    @DisplayName("Should return an offset range when centerBarAtStartValue is true")
    void findDomainBounds_whenCenterBarAtStartIsTrue_returnsOffsetRange() {
        // Arrange
        // When centerBarAtStartValue is true, the bar is centered on the interval's start value.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(0.0, true);

        // Act
        Range domainBounds = renderer.findDomainBounds(dataset);

        // Assert
        assertNotNull(domainBounds, "The returned range should not be null for a valid dataset.");

        // Explanation for expected lower bound:
        // The point with the lowest start-x is {x=1.0, x-start=0.9, x-end=1.1}.
        // The interval width is 1.1 - 0.9 = 0.2.
        // The new interval, centered on the start value (0.9), becomes:
        // [0.9 - (0.2 / 2), 0.9 + (0.2 / 2)] --> [0.8, 1.0].
        // The minimum of all such calculated lower bounds is 0.8.
        assertEquals(0.8, domainBounds.getLowerBound(), EPSILON,
                "Lower bound should be offset to center the bar on the start-x value.");

        // Explanation for expected upper bound:
        // The point with the highest x-value is {x=13.0, x-start=12.9, x-end=13.1}.
        // The interval width is 13.1 - 12.9 = 0.2.
        // The new interval, centered on the start value (12.9), becomes:
        // [12.9 - (0.2 / 2), 12.9 + (0.2 / 2)] --> [12.8, 13.0].
        // The maximum of all such calculated upper bounds is 13.0.
        assertEquals(13.0, domainBounds.getUpperBound(), EPSILON,
                "Upper bound should be offset to center the bar on the start-x value.");
    }

    /**
     * Verifies that findDomainBounds returns null when the dataset is null.
     */
    @Test
    @DisplayName("Should return null for a null dataset")
    void findDomainBounds_withNullDataset_returnsNull() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Act
        Range domainBounds = renderer.findDomainBounds(null);

        // Assert
        assertNull(domainBounds, "findDomainBounds(null) should return null.");
    }

    /**
     * Creates a sample dataset with two series for testing.
     *
     * @return A sample DefaultIntervalXYDataset.
     */
    private DefaultIntervalXYDataset<String> createSampleDataset() {
        DefaultIntervalXYDataset<String> dataset = new DefaultIntervalXYDataset<>();

        // Series 1
        double[] x1 = {1.0, 2.0, 3.0};
        double[] x1Start = {0.9, 1.9, 2.9};
        double[] x1End = {1.1, 2.1, 3.1};
        double[] y1 = {4.0, 5.0, 6.0};
        double[] y1Start = {1.09, 2.09, 3.09};
        double[] y1End = {1.11, 2.11, 3.11};
        double[][] data1 = {x1, x1Start, x1End, y1, y1Start, y1End};
        dataset.addSeries("S1", data1);

        // Series 2
        double[] x2 = {11.0, 12.0, 13.0};
        double[] x2Start = {10.9, 11.9, 12.9};
        double[] x2End = {11.1, 12.1, 13.1};
        double[] y2 = {14.0, 15.0, 16.0};
        double[] y2Start = {11.09, 12.09, 13.09};
        double[] y2End = {11.11, 12.11, 13.11};
        double[][] data2 = {x2, x2Start, x2End, y2, y2Start, y2End};
        dataset.addSeries("S2", data2);

        return dataset;
    }
}