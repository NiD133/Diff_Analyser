package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the cloning of the {@link PlotRenderingInfo} class.
 */
@DisplayName("PlotRenderingInfo Cloning")
class PlotRenderingInfoTest {

    private PlotRenderingInfo original;
    private PlotRenderingInfo clone;

    @BeforeEach
    void setUp() throws CloneNotSupportedException {
        // Arrange: Create a standard PlotRenderingInfo instance to be used in tests.
        original = new PlotRenderingInfo(new ChartRenderingInfo());
        original.setPlotArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        original.setDataArea(new Rectangle2D.Double(5.0, 6.0, 7.0, 8.0));

        // Act: Create the clone that will be tested.
        clone = (PlotRenderingInfo) original.clone();
    }

    @Test
    @DisplayName("should create a new instance")
    void clone_shouldCreateNewInstance() {
        assertNotSame(original, clone, "A clone must be a different object instance from the original.");
    }

    @Test
    @DisplayName("should be equal to the original before any modifications")
    void clone_shouldBeEqualInitially() {
        assertEquals(original, clone, "A new clone should be equal in value to the original.");
    }

    @Nested
    @DisplayName("Independence Checks")
    class IndependenceChecks {

        @Test
        @DisplayName("modifying the original's plot area should not affect the clone")
        void modifyingOriginalPlotArea_shouldNotAffectClone() {
            // Arrange: Capture the clone's plot area before the original is modified.
            Rectangle2D clonePlotAreaBefore = (Rectangle2D) clone.getPlotArea().clone();

            // Act: Modify the plot area of the original object.
            original.getPlotArea().setRect(99.0, 98.0, 97.0, 96.0);

            // Assert: The clone's plot area remains unchanged.
            assertNotEquals(original.getPlotArea(), clone.getPlotArea(),
                "Original and clone plot areas should now be different.");
            assertEquals(clonePlotAreaBefore, clone.getPlotArea(),
                "Clone's plot area should not have changed.");
        }

        @Test
        @DisplayName("modifying the original's data area should not affect the clone")
        void modifyingOriginalDataArea_shouldNotAffectClone() {
            // Arrange: Capture the clone's data area before the original is modified.
            Rectangle2D cloneDataAreaBefore = (Rectangle2D) clone.getDataArea().clone();

            // Act: Modify the data area of the original object.
            original.getDataArea().setRect(55.0, 66.0, 77.0, 88.0);

            // Assert: The clone's data area remains unchanged.
            assertNotEquals(original.getDataArea(), clone.getDataArea(),
                "Original and clone data areas should now be different.");
            assertEquals(cloneDataAreaBefore, clone.getDataArea(),
                "Clone's data area should not have changed.");
        }
    }
}