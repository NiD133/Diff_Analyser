package org.jfree.chart;

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A collection of tests for the cloning behavior of the {@link ChartRenderingInfo} class.
 */
@DisplayName("ChartRenderingInfo Cloning")
class ChartRenderingInfoTest {

    private ChartRenderingInfo originalInfo;
    private ChartRenderingInfo clonedInfo;

    @BeforeEach
    void setUp() throws CloneNotSupportedException {
        originalInfo = new ChartRenderingInfo();
        clonedInfo = CloneUtils.clone(originalInfo);
    }

    @Test
    @DisplayName("should create a new, distinct object instance")
    void clone_createsNewInstance() {
        assertNotSame(originalInfo, clonedInfo, "A clone must be a separate object from the original.");
    }

    @Test
    @DisplayName("should be equal to the original right after cloning")
    void clone_isInitiallyEqualToOriginal() {
        assertEquals(originalInfo, clonedInfo, "A new clone should be logically equal to the original.");
    }

    @Test
    @DisplayName("should have an independent chartArea")
    void clone_hasIndependentChartArea() {
        // Arrange: Capture the initial state of the clone's chart area.
        Rectangle2D initialCloneChartArea = (Rectangle2D) clonedInfo.getChartArea().clone();

        // Act: Modify the chart area of the original object.
        originalInfo.getChartArea().setRect(10.0, 20.0, 30.0, 40.0);

        // Assert: The modification to the original should not affect the clone.
        assertNotEquals(originalInfo.getChartArea(), clonedInfo.getChartArea(),
                "The clone's chart area should differ from the modified original's.");
        assertEquals(initialCloneChartArea, clonedInfo.getChartArea(),
                "The clone's chart area should remain unchanged after the original was modified.");
    }

    @Test
    @DisplayName("should have an independent entity collection")
    void clone_hasIndependentEntityCollection() {
        // Arrange: The clone's entity collection is initially empty.
        assertTrue(clonedInfo.getEntityCollection().getEntities().isEmpty(),
                "The clone's entity collection should be empty initially.");

        // Act: Add an entity to the original object's collection.
        originalInfo.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 3, 4)));

        // Assert: The modification to the original should not affect the clone.
        assertFalse(originalInfo.getEntityCollection().getEntities().isEmpty(),
                "The original's entity collection should now contain an entity.");
        assertTrue(clonedInfo.getEntityCollection().getEntities().isEmpty(),
                "The clone's entity collection should remain empty after the original was modified.");
    }
}