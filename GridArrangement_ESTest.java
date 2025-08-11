package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.HorizontalAlignment;
import org.jfree.chart.api.VerticalAlignment;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.CenterArrangement;
import org.jfree.chart.block.ColorBlock;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.block.FlowArrangement;
import org.jfree.chart.block.GridArrangement;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.data.Range;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class GridArrangementTest {

    @Test
    public void testGridArrangementEquality() {
        GridArrangement grid1 = new GridArrangement(15, 15);
        GridArrangement grid2 = new GridArrangement(2084, 15);
        
        assertNotNull(grid1);
        assertNotNull(grid2);
        assertFalse(grid1.equals(grid2));
        assertFalse(grid2.equals(grid1));
    }

    @Test
    public void testGridArrangementWithZeroDimensions() {
        GridArrangement grid1 = new GridArrangement(0, 0);
        GridArrangement grid2 = new GridArrangement(0, -1901);
        
        assertNotNull(grid1);
        assertNotNull(grid2);
        assertFalse(grid1.equals(grid2));
        assertFalse(grid2.equals(grid1));
    }

    @Test
    public void testRangeAndRectangleConstraint() {
        Range range = new Range(105, 105);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        
        assertEquals(0.0, range.getLength(), 0.01);
        assertEquals(LengthConstraintType.RANGE, constraint.getHeightConstraintType());
        assertEquals(LengthConstraintType.RANGE, constraint.getWidthConstraintType());
    }

    @Test
    public void testBlockContainerWithColorBlock() {
        BlockContainer container = new BlockContainer();
        SystemColor color = SystemColor.textInactiveText;
        ColorBlock colorBlock = new ColorBlock(color, 0.0, Double.NEGATIVE_INFINITY);
        
        container.add(colorBlock);
        assertFalse(container.isEmpty());
    }

    @Test
    public void testArrangeWithFixedWidthAndHeight() {
        GridArrangement grid = new GridArrangement(0, 4326);
        BlockContainer container = new BlockContainer(new FlowArrangement(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0.0, 1623.165750936));
        RectangleConstraint constraint = RectangleConstraint.NONE;
        
        Size2D size = grid.arrange(container, null, constraint);
        assertEquals(Double.NaN, size.getHeight(), 0.01);
        assertEquals(0.0, size.getWidth(), 0.01);
    }

    @Test
    public void testArrangeWithEmptyBlock() {
        GridArrangement grid = new GridArrangement(15, 15);
        BlockContainer container = new BlockContainer(new ColumnArrangement());
        EmptyBlock emptyBlock = new EmptyBlock(15, Double.MAX_VALUE);
        
        container.add(emptyBlock, grid);
        assertFalse(container.isEmpty());
    }

    @Test
    public void testArrangeWithNegativeDimensions() {
        GridArrangement grid = new GridArrangement(-919, -919);
        Range range = new Range(-919, -919);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        BlockContainer container = new BlockContainer(grid);
        
        Size2D size = grid.arrangeNR(container, null, constraint);
        assertEquals(-919.0, size.getWidth(), 0.01);
        assertEquals(-919.0, size.getHeight(), 0.01);
    }

    @Test
    public void testArrangeWithFixedWidthAndRangeHeight() {
        GridArrangement grid = new GridArrangement(65, 65);
        Range range = new Range(65, 65);
        RectangleConstraint constraint = new RectangleConstraint(-555.02928076, range);
        BlockContainer container = new BlockContainer();
        
        Size2D size = grid.arrangeFR(container, null, constraint);
        assertEquals(-555.02928076, size.getWidth(), 0.01);
        assertEquals(65.0, size.getHeight(), 0.01);
    }

    @Test
    public void testArrangeWithNegativeRange() {
        GridArrangement grid = new GridArrangement(0, -2116);
        Range range = new Range(0.0, 1317.5620888757064);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        BlockContainer container = new BlockContainer(grid);
        
        Size2D size = grid.arrangeFR(container, null, constraint);
        assertEquals(-0.0, size.getWidth(), 0.01);
        assertEquals(Double.NaN, size.getHeight(), 0.01);
    }

    @Test
    public void testArrangeWithFixedWidthAndRangeHeightWithNegativeValues() {
        GridArrangement grid = new GridArrangement(4, 3136);
        Range range = new Range(-1833.62112, -59.797977);
        RectangleConstraint constraint = new RectangleConstraint(3136, range, LengthConstraintType.FIXED, 3136, range, LengthConstraintType.FIXED);
        BlockContainer container = new BlockContainer(grid);
        
        Size2D size = grid.arrangeFR(container, null, constraint);
        assertEquals(3136.0, size.getWidth(), 0.01);
        assertEquals(-59.797977, size.getHeight(), 0.01);
    }

    @Test
    public void testArrangeWithFixedWidthAndRangeHeightWithZeroRange() {
        GridArrangement grid = new GridArrangement(1, 1);
        Range range = new Range(-2347.4, 0.0);
        RectangleConstraint constraint = new RectangleConstraint(-3036.4844585497153, range);
        BlockContainer container = new BlockContainer();
        
        Size2D size = grid.arrangeFF(container, null, constraint);
        assertEquals(-3036.4844585497153, size.getWidth(), 0.01);
        assertEquals(0.0, size.getHeight(), 0.01);
    }

    @Test
    public void testArrangeWithFixedWidthAndRangeHeightWithNegativeWidth() {
        GridArrangement grid = new GridArrangement(68, 68);
        BlockContainer container = new BlockContainer();
        RectangleConstraint constraint = new RectangleConstraint(-374.0, null);
        
        try {
            grid.arrangeFN(container, null, constraint);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Range(double, double): require lower (0.0) <= upper (-5.5)."));
        }
    }
}