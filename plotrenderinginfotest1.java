package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link PlotRenderingInfo} class, focusing on the equals() method.
 */
@DisplayName("PlotRenderingInfo equals()")
class PlotRenderingInfoTest {

    private PlotRenderingInfo info1;
    private PlotRenderingInfo info2;

    @BeforeEach
    void setUp() {
        // Arrange: Create two identical PlotRenderingInfo objects for comparison.
        // The 'owner' (ChartRenderingInfo) is not considered in the equals() method.
        info1 = new PlotRenderingInfo(new ChartRenderingInfo());
        info2 = new PlotRenderingInfo(new ChartRenderingInfo());
    }

    @Test
    @DisplayName("Two new instances with default values should be equal")
    void newInstancesShouldBeEqual() {
        // Assert: Two newly created instances should be equal to each other.
        assertEquals(info1, info2);
        // Assert: The equals contract should be symmetric.
        assertEquals(info2, info1);
    }

    @Test
    @DisplayName("should be unequal if plot areas differ")
    void equalityShouldDependOnPlotArea() {
        // Act: Modify the plot area of the first instance.
        info1.setPlotArea(new Rectangle(1, 2, 3, 4));

        // Assert: The instances should no longer be equal.
        assertNotEquals(info1, info2);

        // Act: Apply the same plot area to the second instance.
        info2.setPlotArea(new Rectangle(1, 2, 3, 4));

        // Assert: The instances should be equal again.
        assertEquals(info1, info2);
    }

    @Test
    @DisplayName("should be unequal if data areas differ")
    void equalityShouldDependOnDataArea() {
        // Act: Modify the data area of the first instance.
        info1.setDataArea(new Rectangle(5, 6, 7, 8));

        // Assert: The instances should no longer be equal.
        assertNotEquals(info1, info2);

        // Act: Apply the same data area to the second instance.
        info2.setDataArea(new Rectangle(5, 6, 7, 8));

        // Assert: The instances should be equal again.
        assertEquals(info1, info2);
    }

    @Test
    @DisplayName("should be unequal if subplot lists differ")
    void equalityShouldDependOnSubplotList() {
        // Act: Add a subplot to the first instance, making the lists different.
        info1.addSubplotInfo(new PlotRenderingInfo(null));

        // Assert: The instances should no longer be equal.
        assertNotEquals(info1, info2);

        // Act: Add an equivalent subplot to the second instance.
        info2.addSubplotInfo(new PlotRenderingInfo(null));

        // Assert: The instances should be equal again.
        assertEquals(info1, info2);
    }

    @Test
    @DisplayName("should be unequal if subplot contents differ")
    void equalityShouldDependOnSubplotContent() {
        // Arrange: Add identical subplots to both instances so they are comparable.
        info1.addSubplotInfo(new PlotRenderingInfo(null));
        info2.addSubplotInfo(new PlotRenderingInfo(null));
        assertEquals(info1, info2, "Sanity check: instances should be equal before subplot modification");

        // Act: Modify the data area of a subplot in the first instance.
        info1.getSubplotInfo(0).setDataArea(new Rectangle(10, 20, 30, 40));

        // Assert: The instances should no longer be equal due to the deep comparison.
        assertNotEquals(info1, info2);

        // Act: Apply the same modification to the subplot in the second instance.
        info2.getSubplotInfo(0).setDataArea(new Rectangle(10, 20, 30, 40));

        // Assert: The instances should be equal again.
        assertEquals(info1, info2);
    }
}