/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ------------------------
 * GridArrangementTest.java
 * ------------------------
 * (C) Copyright 2005-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double EPSILON = 0.000000001;
    
    // Test data constants for better maintainability
    private static final int ROWS_1 = 11;
    private static final int COLS_1 = 22;
    private static final int ROWS_2 = 33;
    private static final int COLS_2 = 44;

    /**
     * Verify that equals() method correctly compares GridArrangement instances
     * based on their rows and columns values.
     */
    @Test
    public void testEquals() {
        // Test equal instances
        GridArrangement arrangement1 = new GridArrangement(ROWS_1, COLS_1);
        GridArrangement arrangement2 = new GridArrangement(ROWS_1, COLS_1);
        assertEquals(arrangement1, arrangement2);
        assertEquals(arrangement2, arrangement1);

        // Test different rows
        GridArrangement differentRows = new GridArrangement(ROWS_2, COLS_1);
        assertNotEquals(arrangement1, differentRows);
        
        GridArrangement matchingRows = new GridArrangement(ROWS_2, COLS_1);
        assertEquals(differentRows, matchingRows);

        // Test different columns
        GridArrangement differentCols = new GridArrangement(ROWS_2, COLS_2);
        assertNotEquals(differentRows, differentCols);
        
        GridArrangement matchingCols = new GridArrangement(ROWS_2, COLS_2);
        assertEquals(differentCols, matchingCols);
    }

    /**
     * Verify that GridArrangement is immutable and doesn't implement Cloneable.
     */
    @Test
    public void testCloning() {
        GridArrangement arrangement = new GridArrangement(1, 2);
        assertFalse(arrangement instanceof Cloneable, 
                   "GridArrangement should not implement Cloneable since it's immutable");
    }

    /**
     * Verify that GridArrangement can be serialized and deserialized correctly.
     */
    @Test
    public void testSerialization() {
        GridArrangement original = new GridArrangement(ROWS_2, COLS_2);
        GridArrangement deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, 
                    "Serialized and deserialized instances should be equal");
    }

    // ========== Layout Arrangement Tests ==========
    // Test naming convention: test[WidthConstraint][HeightConstraint]
    // N = None, F = Fixed, R = Range

    /**
     * Test arrangement with no width or height constraints.
     * Expected: Natural size based on content.
     */
    @Test
    public void testArrangement_NoConstraints() {
        BlockContainer container = createTestContainer();
        Size2D actualSize = container.arrange(null, RectangleConstraint.NONE);
        
        assertSize(90.0, 33.0, actualSize, "No constraints should use natural size");
    }

    /**
     * Test arrangement with fixed width and no height constraint.
     * Expected: Uses specified width, natural height.
     */
    @Test
    public void testArrangement_FixedWidth_NoHeightConstraint() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = new RectangleConstraint(
            100.0, null, LengthConstraintType.FIXED, 
            0.0, null, LengthConstraintType.NONE
        );
        
        Size2D actualSize = container.arrange(null, constraint);
        assertSize(100.0, 33.0, actualSize, "Fixed width should be respected, height natural");
    }

    /**
     * Test arrangement with no width constraint and fixed height.
     * Expected: Natural width, uses specified height.
     */
    @Test
    public void testArrangement_NoWidthConstraint_FixedHeight() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE.toFixedHeight(100.0);
        
        Size2D actualSize = container.arrange(null, constraint);
        assertSize(90.0, 100.0, actualSize, "Width should be natural, fixed height respected");
    }

    /**
     * Test arrangement with width range and fixed height.
     * Expected: Uses maximum width from range, specified height.
     */
    @Test
    public void testArrangement_WidthRange_FixedHeight() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = new RectangleConstraint(
            new Range(40.0, 60.0), 100.0
        );
        
        Size2D actualSize = container.arrange(null, constraint);
        assertSize(60.0, 100.0, actualSize, "Should use max width from range and fixed height");
    }

    /**
     * Test arrangement with width and height ranges.
     * Expected: Uses maximum width and minimum height from ranges.
     */
    @Test
    public void testArrangement_WidthRange_HeightRange() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = new RectangleConstraint(
            new Range(40.0, 60.0), new Range(50.0, 70.0)
        );
        
        Size2D actualSize = container.arrange(null, constraint);
        assertSize(60.0, 50.0, actualSize, "Should use max width and min height from ranges");
    }

    /**
     * Test arrangement with width range and no height constraint.
     * Expected: Uses maximum width from range, natural height.
     */
    @Test
    public void testArrangement_WidthRange_NoHeightConstraint() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE.toRangeWidth(
            new Range(40.0, 60.0)
        );
        
        Size2D actualSize = container.arrange(null, constraint);
        assertSize(60.0, 33.0, actualSize, "Should use max width from range and natural height");
    }

    /**
     * Test arrangement with no width constraint and height range.
     * Expected: Natural width, uses minimum height from range.
     */
    @Test
    public void testArrangement_NoWidthConstraint_HeightRange() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE.toRangeHeight(
            new Range(40.0, 60.0)
        );
        
        Size2D actualSize = container.arrange(null, constraint);
        assertSize(90.0, 40.0, actualSize, "Should use natural width and min height from range");
    }

    // ========== Edge Case Tests ==========

    /**
     * Test that null blocks in the grid are handled gracefully with fixed constraints.
     */
    @Test
    public void testNullBlock_FixedWidthAndHeight() {
        BlockContainer container = createContainerWithNullBlock();
        Size2D actualSize = container.arrange(null, new RectangleConstraint(20, 10));
        
        assertSize(20.0, 10.0, actualSize, "Fixed constraints should be respected even with null blocks");
    }

    /**
     * Test that null blocks in the grid are handled gracefully with fixed width only.
     */
    @Test
    public void testNullBlock_FixedWidth_NoHeightConstraint() {
        BlockContainer container = createContainerWithNullBlock();
        Size2D actualSize = container.arrange(null, RectangleConstraint.NONE.toFixedWidth(10));
        
        assertSize(10.0, 0.0, actualSize, "Null block should result in zero height with fixed width");
    }

    /**
     * Test that null blocks in the grid are handled gracefully with fixed width and height range.
     */
    @Test
    public void testNullBlock_FixedWidth_HeightRange() {
        BlockContainer container = createContainerWithNullBlock();
        Size2D actualSize = container.arrange(null, new RectangleConstraint(30.0, new Range(5.0, 10.0)));
        
        assertSize(30.0, 5.0, actualSize, "Should use fixed width and minimum height from range");
    }

    /**
     * Test that null blocks in the grid are handled gracefully with no constraints.
     */
    @Test
    public void testNullBlock_NoConstraints() {
        BlockContainer container = createContainerWithNullBlock();
        Size2D actualSize = container.arrange(null, RectangleConstraint.NONE);
        
        assertSize(0.0, 0.0, actualSize, "Null block with no constraints should result in zero size");
    }

    /**
     * Test that grids with fewer blocks than grid spaces are handled correctly with fixed constraints.
     */
    @Test
    public void testPartiallyFilledGrid_FixedWidthAndHeight() {
        BlockContainer container = createPartiallyFilledGrid();
        Size2D actualSize = container.arrange(null, new RectangleConstraint(200, 100));
        
        assertSize(200.0, 100.0, actualSize, "Fixed constraints should be respected for partially filled grid");
    }

    /**
     * Test that grids with fewer blocks than grid spaces are handled correctly with fixed width only.
     */
    @Test
    public void testPartiallyFilledGrid_FixedWidth_NoHeightConstraint() {
        BlockContainer container = createPartiallyFilledGrid();
        Size2D actualSize = container.arrange(null, RectangleConstraint.NONE.toFixedWidth(30.0));
        
        assertSize(30.0, 10.0, actualSize, "Should use fixed width and calculate height based on content");
    }

    /**
     * Test that grids with fewer blocks than grid spaces are handled correctly with fixed width and height range.
     */
    @Test
    public void testPartiallyFilledGrid_FixedWidth_HeightRange() {
        BlockContainer container = createPartiallyFilledGrid();
        Size2D actualSize = container.arrange(null, new RectangleConstraint(30.0, new Range(5.0, 10.0)));
        
        assertSize(30.0, 10.0, actualSize, "Should use fixed width and maximum height from range");
    }

    /**
     * Test that grids with fewer blocks than grid spaces are handled correctly with no constraints.
     */
    @Test
    public void testPartiallyFilledGrid_NoConstraints() {
        BlockContainer container = createPartiallyFilledGrid();
        Size2D actualSize = container.arrange(null, RectangleConstraint.NONE);
        
        assertSize(15.0, 10.0, actualSize, "Should calculate natural size for partially filled grid");
    }

    // ========== Helper Methods ==========

    /**
     * Creates a test container with three blocks arranged in a 1x3 grid.
     * Block dimensions: 10x11, 20x22, 30x33
     * Expected total size: 60x33 (sum of widths x max height)
     */
    private BlockContainer createTestContainer() {
        Block block1 = new EmptyBlock(10, 11);
        Block block2 = new EmptyBlock(20, 22);
        Block block3 = new EmptyBlock(30, 33);
        
        BlockContainer container = new BlockContainer(new GridArrangement(1, 3));
        container.add(block1);
        container.add(block2);
        container.add(block3);
        
        return container;
    }

    /**
     * Creates a container with a single null block in a 1x1 grid.
     */
    private BlockContainer createContainerWithNullBlock() {
        BlockContainer container = new BlockContainer(new GridArrangement(1, 1));
        container.add(null);
        return container;
    }

    /**
     * Creates a 2x3 grid with only one block (5x5), leaving other positions empty.
     */
    private BlockContainer createPartiallyFilledGrid() {
        Block block = new EmptyBlock(5, 5);
        BlockContainer container = new BlockContainer(new GridArrangement(2, 3));
        container.add(block);
        return container;
    }

    /**
     * Asserts that the actual size matches the expected dimensions within epsilon tolerance.
     */
    private void assertSize(double expectedWidth, double expectedHeight, Size2D actualSize, String message) {
        assertEquals(expectedWidth, actualSize.width, EPSILON, 
                    message + " - Width mismatch");
        assertEquals(expectedHeight, actualSize.height, EPSILON, 
                    message + " - Height mismatch");
    }
}