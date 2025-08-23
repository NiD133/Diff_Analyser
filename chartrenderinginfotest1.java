package org.jfree.chart;

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the equals() method in the {@link ChartRenderingInfo} class.
 */
@DisplayName("ChartRenderingInfo.equals()")
class ChartRenderingInfoTest {

    private ChartRenderingInfo info1;
    private ChartRenderingInfo info2;

    @BeforeEach
    void setUp() {
        // Arrange: Create two separate but identical ChartRenderingInfo instances before each test.
        info1 = new ChartRenderingInfo();
        info2 = new ChartRenderingInfo();
    }

    @Test
    @DisplayName("should return true for two default instances")
    void testEquals_withDefaultInstances() {
        // Assert: Two newly created instances should be equal.
        assertEquals(info1, info2, "Default instances should be equal.");
    }

    @Test
    @DisplayName("should distinguish objects based on the chartArea property")
    void testEquals_whenChartAreaDiffers() {
        // Assert: Establish a baseline of equality.
        assertEquals(info1, info2);

        // Act: Modify the chartArea of the first instance.
        info1.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));

        // Assert: The instances should no longer be equal.
        assertNotEquals(info1, info2, "Instances should not be equal after changing one chartArea.");

        // Act: Modify the chartArea of the second instance to match the first.
        info2.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));

        // Assert: The instances should be equal again.
        assertEquals(info1, info2, "Instances should be equal when chartAreas match.");
    }

    @Test
    @DisplayName("should distinguish objects based on the plotInfo property")
    void testEquals_whenPlotInfoDiffers() {
        // Assert: Establish a baseline of equality.
        assertEquals(info1, info2);

        // Act: Modify the plotInfo of the first instance.
        info1.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));

        // Assert: The instances should no longer be equal.
        assertNotEquals(info1, info2, "Instances should not be equal after changing one plotInfo.");

        // Act: Modify the plotInfo of the second instance to match the first.
        info2.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));

        // Assert: The instances should be equal again.
        assertEquals(info1, info2, "Instances should be equal when plotInfos match.");
    }

    @Test
    @DisplayName("should distinguish objects based on the entityCollection property")
    void testEquals_whenEntityCollectionDiffers() {
        // Assert: Establish a baseline of equality.
        assertEquals(info1, info2);

        // Arrange: Create an entity collection to be added.
        StandardEntityCollection entities1 = new StandardEntityCollection();
        entities1.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));

        // Act: Set the entity collection on the first instance.
        info1.setEntityCollection(entities1);

        // Assert: The instances should no longer be equal.
        assertNotEquals(info1, info2, "Instances should not be equal after changing one entityCollection.");

        // Arrange: Create a matching entity collection.
        StandardEntityCollection entities2 = new StandardEntityCollection();
        entities2.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));

        // Act: Set the matching entity collection on the second instance.
        info2.setEntityCollection(entities2);

        // Assert: The instances should be equal again. (This fixes a bug in the original test).
        assertEquals(info1, info2, "Instances should be equal when entityCollections match.");
    }
}