package org.jfree.chart.axis;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

/**
 * Readable tests for ModuloAxis.
 *
 * These tests aim to cover:
 * - Default display start/end values.
 * - setDisplayRange behavior (plain and wrapped values).
 * - autoAdjustRange effect on the axis range.
 * - Coordinate conversions with valid inputs and null handling.
 * - resizeRange validation.
 * - equals/clone contract.
 */
public class ModuloAxisTest {

    private static final double EPS = 1e-9;

    private static ModuloAxis newDegreesAxis0to360() {
        ModuloAxis axis = new ModuloAxis("degrees", new Range(0, 360));
        // Remove any margins that could complicate predictable mapping in tests.
        axis.setLowerMargin(0.0);
        axis.setUpperMargin(0.0);
        axis.setInverted(false);
        return axis;
    }

    @Test
    public void defaultDisplayRange_is270to90() {
        ModuloAxis axis = new ModuloAxis("any", new Range(0, 360));

        assertEquals(270.0, axis.getDisplayStart(), EPS);
        assertEquals(90.0, axis.getDisplayEnd(), EPS);
        // ValueAxis default is usually auto-range true:
        assertTrue(axis.isAutoRange());
    }

    @Test
    public void setDisplayRange_plainValues_noWrap() {
        ModuloAxis axis = newDegreesAxis0to360();

        axis.setDisplayRange(10.0, 20.0);

        assertEquals(10.0, axis.getDisplayStart(), EPS);
        assertEquals(20.0, axis.getDisplayEnd(), EPS);
    }

    @Test
    public void setDisplayRange_valuesOutsideFixedRange_areWrappedIntoFixedRange() {
        ModuloAxis axis = newDegreesAxis0to360();

        // 725 -> 725 % 360 == 5, -10 -> 350 within [0, 360]
        axis.setDisplayRange(725.0, -10.0);

        assertEquals(5.0, axis.getDisplayStart(), EPS);
        assertEquals(350.0, axis.getDisplayEnd(), EPS);
    }

    @Test
    public void autoAdjustRange_setsAxisRangeToFixedRange() {
        Range fixed = new Range(10.0, 20.0);
        ModuloAxis axis = new ModuloAxis("any", fixed);

        axis.autoAdjustRange();

        assertEquals("autoAdjustRange should set axis range to fixedRange",
                fixed, axis.getRange());
        assertTrue(axis.isAutoRange());
    }

    @Test
    public void valueToJava2D_withBottomEdge_mapsLinearlyWithinDisplayRange() {
        ModuloAxis axis = newDegreesAxis0to360();
        // Pick a simple non-wrapping display range for predictable mapping:
        axis.setDisplayRange(10.0, 20.0);

        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 40);

        // Expected: 10 -> x=0, 20 -> x=100, midpoint 15 -> x=50
        assertEquals(0.0, axis.valueToJava2D(10.0, area, RectangleEdge.BOTTOM), EPS);
        assertEquals(100.0, axis.valueToJava2D(20.0, area, RectangleEdge.BOTTOM), EPS);
        assertEquals(50.0, axis.valueToJava2D(15.0, area, RectangleEdge.BOTTOM), EPS);
    }

    @Test
    public void coordinateConversions_nullArea_throwsNPE() {
        ModuloAxis axis = newDegreesAxis0to360();

        assertThrows(NullPointerException.class,
                () -> axis.valueToJava2D(0.0, null, RectangleEdge.BOTTOM));
        assertThrows(NullPointerException.class,
                () -> axis.java2DToValue(0.0, null, RectangleEdge.BOTTOM));
        assertThrows(NullPointerException.class,
                () -> axis.lengthToJava2D(1.0, null, RectangleEdge.BOTTOM));
    }

    @Test
    public void resizeRange_nonPositivePercent_throwsIAE() {
        ModuloAxis axis = newDegreesAxis0to360();

        assertThrows(IllegalArgumentException.class, () -> axis.resizeRange(0.0));
        assertThrows(IllegalArgumentException.class, () -> axis.resizeRange(-1.0));
    }

    @Test
    public void equals_and_clone_contract() throws CloneNotSupportedException {
        ModuloAxis a1 = newDegreesAxis0to360();
        ModuloAxis a2 = (ModuloAxis) a1.clone();

        // equal to itself and to its clone; not same instance
        assertEquals(a1, a1);
        assertEquals(a1, a2);
        assertNotSame(a1, a2);

        // Changing the display range breaks equality
        a2.setDisplayRange(10.0, 20.0);
        assertNotEquals(a1, a2);
    }
}