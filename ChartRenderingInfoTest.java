package org.jfree.chart;

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ChartRenderingInfo}.
 *
 * The tests are organized to emphasize intent and isolate responsibilities:
 * - equals(): verifies differences in each significant field
 * - clone(): verifies deep copy and independence
 * - serialization: verifies state and internal references are preserved
 */
public class ChartRenderingInfoTest {

    // -------- equals() -------------------------------------------------------

    @Test
    public void equals_defaultInstancesAreEqual() {
        ChartRenderingInfo left = new ChartRenderingInfo();
        ChartRenderingInfo right = new ChartRenderingInfo();

        assertEquals(left, right);
    }

    @Test
    public void equals_detectsDifferenceInChartArea() {
        ChartRenderingInfo left = new ChartRenderingInfo();
        ChartRenderingInfo right = new ChartRenderingInfo();

        left.setChartArea(rectD(1, 2, 3, 4));
        assertNotEquals(left, right);

        right.setChartArea(rectD(1, 2, 3, 4));
        assertEquals(left, right);
    }

    @Test
    public void equals_detectsDifferenceInPlotDataArea() {
        ChartRenderingInfo left = new ChartRenderingInfo();
        ChartRenderingInfo right = new ChartRenderingInfo();

        left.getPlotInfo().setDataArea(rectI(1, 2, 3, 4));
        assertNotEquals(left, right);

        right.getPlotInfo().setDataArea(rectI(1, 2, 3, 4));
        assertEquals(left, right);
    }

    @Test
    public void equals_detectsDifferenceInEntityCollection() {
        ChartRenderingInfo left = new ChartRenderingInfo();
        ChartRenderingInfo right = new ChartRenderingInfo();

        StandardEntityCollection leftEntities = entityCollectionWithOneEntity(rectI(1, 2, 3, 4));
        left.setEntityCollection(leftEntities);
        assertNotEquals(left, right);

        StandardEntityCollection rightEntities = entityCollectionWithOneEntity(rectI(1, 2, 3, 4));
        right.setEntityCollection(rightEntities);
        assertEquals(left, right);
    }

    // -------- clone() --------------------------------------------------------

    @Test
    public void clone_createsDeepCopyAndMaintainsIndependence() throws CloneNotSupportedException {
        // Given
        ChartRenderingInfo original = new ChartRenderingInfo();

        // When
        ChartRenderingInfo clone = CloneUtils.clone(original);

        // Then
        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);

        // Mutate chart area in original only => should diverge
        original.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(original, clone);

        // Align clone's chart area => should be equal again
        clone.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(original, clone);

        // Mutate entity collection in original only => should diverge
        original.getEntityCollection().add(new ChartEntity(rectI(1, 2, 2, 1)));
        assertNotEquals(original, clone);

        // Align clone's entity collection => should be equal again
        clone.getEntityCollection().add(new ChartEntity(rectI(1, 2, 2, 1)));
        assertEquals(original, clone);

        // Sanity: entity collections are distinct objects (deep copy)
        EntityCollection originalEntities = original.getEntityCollection();
        EntityCollection cloneEntities = clone.getEntityCollection();
        if (originalEntities != null && cloneEntities != null) {
            assertNotSame(originalEntities, cloneEntities);
        }

        // Sanity: plot info is also a distinct object (deep copy)
        PlotRenderingInfo originalPlot = original.getPlotInfo();
        PlotRenderingInfo clonePlot = clone.getPlotInfo();
        assertNotSame(originalPlot, clonePlot);
    }

    // -------- serialization --------------------------------------------------

    @Test
    public void serialization_preservesChartArea() {
        // Given
        ChartRenderingInfo before = new ChartRenderingInfo();
        before.setChartArea(rectD(1, 2, 3, 4));

        // When
        ChartRenderingInfo after = TestUtils.serialised(before);

        // Then
        assertEquals(before, after);
    }

    @Test
    public void serialization_preservesPlotOwnerReference() {
        // Given
        ChartRenderingInfo before = new ChartRenderingInfo();
        before.getPlotInfo().setDataArea(rectD(1, 2, 3, 4));

        // When
        ChartRenderingInfo after = TestUtils.serialised(before);

        // Then
        assertEquals(before, after);
        // After deserialization, PlotRenderingInfo should reference its owning ChartRenderingInfo
        assertSame(after, after.getPlotInfo().getOwner());
    }

    // -------- helpers --------------------------------------------------------

    private static Rectangle2D rectD(double x, double y, double w, double h) {
        return new Rectangle2D.Double(x, y, w, h);
    }

    private static Rectangle rectI(int x, int y, int w, int h) {
        return new Rectangle(x, y, w, h);
    }

    private static StandardEntityCollection entityCollectionWithOneEntity(Rectangle bounds) {
        StandardEntityCollection entities = new StandardEntityCollection();
        entities.add(new ChartEntity(bounds));
        return entities;
    }
}