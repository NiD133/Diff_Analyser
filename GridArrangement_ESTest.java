package org.jfree.chart.block;

import static org.junit.Assert.*;

import java.awt.Graphics2D;

import org.jfree.data.Range;
import org.junit.Test;

/**
 * Focused, readable tests for GridArrangement.
 *
 * Goals:
 * - Verify equals() semantics (reflexive, symmetric, different fields, other types).
 * - Verify arrange() behavior in a few representative constraint modes for an empty container.
 * - Ensure basic API calls (add, clear) do not throw for typical usage.
 *
 * Notes:
 * - These tests intentionally avoid deeply coupling to internal layout math. Instead, they check
 *   stable, high-level outcomes that are easy to maintain.
 */
public class GridArrangementTest {

    // ----------------------------------------------------------------------
    // equals() semantics
    // ----------------------------------------------------------------------

    @Test
    public void equals_isReflexiveAndSymmetric_whenRowsAndColsMatch() {
        GridArrangement a = new GridArrangement(3, 4);
        GridArrangement b = new GridArrangement(3, 4);

        // Reflexive
        assertTrue(a.equals(a));

        // Symmetric
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));

        // Not the same reference
        assertNotSame(a, b);
    }

    @Test
    public void equals_isFalse_whenRowsDifferOrColsDiffer() {
        GridArrangement base = new GridArrangement(2, 5);

        assertFalse(base.equals(new GridArrangement(3, 5))); // different rows
        assertFalse(base.equals(new GridArrangement(2, 6))); // different columns
        assertFalse(base.equals(new GridArrangement(3, 6))); // both differ
    }

    @Test
    public void equals_isFalse_whenOtherType() {
        GridArrangement ga = new GridArrangement(1, 1);
        assertFalse(ga.equals(new Object()));
    }

    // ----------------------------------------------------------------------
    // Basic API usage
    // ----------------------------------------------------------------------

    @Test
    public void add_allowsNullKey_andDoesNotThrow() {
        GridArrangement ga = new GridArrangement(2, 2);
        LabelBlock block = new LabelBlock("any");
        // Contract: container calls add(block, key). Here we just ensure method accepts inputs.
        ga.add(block, null);
    }

    @Test
    public void clear_doesNotThrow_andLeavesObjectUsable() {
        GridArrangement ga = new GridArrangement(2, 2);
        ga.clear();

        // Still usable after clear()
        BlockContainer container = new BlockContainer();
        Size2D size = ga.arrange(container, null, RectangleConstraint.NONE);
        assertEquals(0.0, size.getWidth(), 0.0001);
        assertEquals(0.0, size.getHeight(), 0.0001);
    }

    // ----------------------------------------------------------------------
    // arrange() behavior with an empty container
    // ----------------------------------------------------------------------

    @Test
    public void arrange_returnsFixedSize_whenWidthAndHeightAreFixed() {
        GridArrangement ga = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer();

        RectangleConstraint fixed = new RectangleConstraint(200.0, 100.0);
        Size2D size = ga.arrange(container, (Graphics2D) null, fixed);

        assertEquals(200.0, size.getWidth(), 0.0001);
        assertEquals(100.0, size.getHeight(), 0.0001);
    }

    @Test
    public void arrange_returnsZeroSize_whenNoConstraints_andContainerIsEmpty() {
        GridArrangement ga = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer();

        Size2D size = ga.arrange(container, (Graphics2D) null, RectangleConstraint.NONE);

        assertEquals(0.0, size.getWidth(), 0.0001);
        assertEquals(0.0, size.getHeight(), 0.0001);
    }

    @Test
    public void arrangeRR_usesRangeBounds_whenBothDimensionsAreRanges() {
        GridArrangement ga = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer();

        Range w = new Range(50.0, 50.0);
        Range h = new Range(50.0, 50.0);
        RectangleConstraint ranges = new RectangleConstraint(w, h);

        // arrangeRR is protected, but tests are in the same package so it is accessible.
        Size2D size = ga.arrangeRR(container, null, ranges);

        assertEquals(50.0, size.getWidth(), 0.0001);
        assertEquals(50.0, size.getHeight(), 0.0001);
    }

    @Test
    public void arrangeNN_emptyContainer_returnsZeroSize() {
        GridArrangement ga = new GridArrangement(2, 2);
        BlockContainer container = new BlockContainer();

        // arrangeNN is protected; tests are in the same package so it is accessible.
        Size2D size = ga.arrangeNN(container, null);

        assertEquals(0.0, size.getWidth(), 0.0001);
        assertEquals(0.0, size.getHeight(), 0.0001);
    }
}