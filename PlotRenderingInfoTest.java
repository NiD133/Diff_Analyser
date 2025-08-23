package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable, intention-revealing tests for PlotRenderingInfo.
 *
 * These tests verify:
 * - Equality considers plot area, data area, and subplot infos.
 * - Cloning performs a deep copy (mutations do not leak across instances).
 * - Serialization round-trips without loss of equality.
 */
class PlotRenderingInfoTest {

    // ------------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------------

    private static PlotRenderingInfo newInfo() {
        return new PlotRenderingInfo(new ChartRenderingInfo());
    }

    private static Rectangle2D rect(double x, double y, double w, double h) {
        return new Rectangle2D.Double(x, y, w, h);
    }

    private static void assertSymmetricEquals(Object a, Object b, String message) {
        assertEquals(a, b, message);
        assertEquals(b, a, "symmetry: " + message);
    }

    // ------------------------------------------------------------------------
    // equals()
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("equals(): considers plot area, data area and subplot infos")
    void equalsShouldConsiderAllRelevantFields() {
        PlotRenderingInfo info1 = newInfo();
        PlotRenderingInfo info2 = newInfo();

        // Initially identical
        assertSymmetricEquals(info1, info2, "new instances are equal");

        // Plot area differences break equality
        info1.setPlotArea(rect(2, 3, 4, 5));
        assertNotEquals(info1, info2, "plot area difference breaks equality");
        info2.setPlotArea(rect(2, 3, 4, 5));
        assertSymmetricEquals(info1, info2, "plot area match restores equality");

        // Data area differences break equality
        info1.setDataArea(rect(2, 4, 6, 8));
        assertNotEquals(info1, info2, "data area difference breaks equality");
        info2.setDataArea(rect(2, 4, 6, 8));
        assertSymmetricEquals(info1, info2, "data area match restores equality");

        // Subplot presence/absence impacts equality
        info1.addSubplotInfo(new PlotRenderingInfo(null));
        assertNotEquals(info1, info2, "subplot count difference breaks equality");
        info2.addSubplotInfo(new PlotRenderingInfo(null));
        assertSymmetricEquals(info1, info2, "subplot count match restores equality");

        // Subplot internal differences impact equality
        info1.getSubplotInfo(0).setDataArea(rect(1, 2, 3, 4));
        assertNotEquals(info1, info2, "subplot data area difference breaks equality");
        info2.getSubplotInfo(0).setDataArea(rect(1, 2, 3, 4));
        assertSymmetricEquals(info1, info2, "subplot data area match restores equality");
    }

    // ------------------------------------------------------------------------
    // clone()
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("clone(): produces deep copy for plot/data areas")
    void cloningShouldProduceDeepCopy() throws CloneNotSupportedException {
        PlotRenderingInfo original = newInfo();
        // Ensure plot area is non-null so we can test deep copy semantics
        original.setPlotArea(new Rectangle2D.Double());

        PlotRenderingInfo clone = CloneUtils.clone(original);

        assertAll("basic clone properties",
                () -> assertNotSame(original, clone, "clone should be a different instance"),
                () -> assertSame(original.getClass(), clone.getClass(), "clone should be same type"),
                () -> assertEquals(original, clone, "clone should be equal initially")
        );

        // Mutate plot area in original - clone should not change
        original.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        assertNotEquals(original, clone, "mutating original plot area breaks equality");
        clone.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        assertEquals(original, clone, "matching clone plot area restores equality");

        // Mutate data area in original - clone should not change
        original.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(original, clone, "mutating original data area breaks equality");
        clone.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(original, clone, "matching clone data area restores equality");
    }

    // ------------------------------------------------------------------------
    // serialization
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("serialization: round-trip preserves equality")
    void serializationRoundTripShouldPreserveEquality() {
        PlotRenderingInfo original = newInfo();
        PlotRenderingInfo restored = TestUtils.serialised(original);
        assertEquals(original, restored, "serialized->deserialized instance should be equal");
    }
}