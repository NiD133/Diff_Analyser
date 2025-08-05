package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import org.jfree.chart.block.*;
import org.jfree.data.Range;

public class GridArrangement_ESTest {

    @Test
    public void testEquals_DifferentRows() {
        GridArrangement grid1 = new GridArrangement(15, 15);
        GridArrangement grid2 = new GridArrangement(2084, 15);
        
        assertFalse("Grids with different row counts should not be equal", 
                   grid1.equals(grid2));
    }

    @Test
    public void testEquals_DifferentColumns() {
        GridArrangement grid1 = new GridArrangement(0, 0);
        GridArrangement grid2 = new GridArrangement(0, -1901);
        
        assertFalse("Grids with different column counts should not be equal", 
                   grid1.equals(grid2));
    }

    @Test
    public void testEquals_SameGridDimensions() {
        GridArrangement grid1 = new GridArrangement(0, 0);
        GridArrangement grid2 = new GridArrangement(0, 0);
        
        assertTrue("Grids with same dimensions should be equal", 
                  grid1.equals(grid2));
    }

    @Test
    public void testEquals_SameInstance() {
        GridArrangement grid = new GridArrangement(0, 0);
        
        assertTrue("Grid should equal itself", grid.equals(grid));
    }

    @Test
    public void testEquals_DifferentObjectType() {
        GridArrangement grid = new GridArrangement(30, 30);
        Object other = new Object();
        
        assertFalse("Grid should not equal different object type", 
                   grid.equals(other));
    }

    @Test
    public void testArrangeWithFixedConstraints() {
        GridArrangement grid = new GridArrangement(105, 105);
        BlockContainer container = createEmptyContainer();
        Range fixedRange = new Range(105, 105);
        RectangleConstraint constraint = new RectangleConstraint(fixedRange, fixedRange);
        
        Size2D result = grid.arrangeFN(container, null, constraint);
        
        assertEquals("Width should match constraint", 105.0, result.getWidth(), 0.01);
        assertEquals("Height should be zero for empty container", 0.0, result.getHeight(), 0.01);
    }

    @Test
    public void testArrangeWithColorBlock() {
        GridArrangement grid = new GridArrangement(59, 59);
        BlockContainer container = createEmptyContainer();
        
        // Add a color block to the container
        ColorBlock colorBlock = new ColorBlock(SystemColor.inactiveCaption, -1.0, 306.1);
        container.add(colorBlock, new Object());
        
        RectangleConstraint noConstraint = RectangleConstraint.NONE;
        Size2D result = grid.arrange(container, null, noConstraint);
        
        assertEquals("Width should be zero", 0.0, result.getWidth(), 0.01);
        assertTrue("Height should be positive", result.getHeight() > 0);
    }

    @Test
    public void testArrangeEmptyContainer() {
        GridArrangement grid = new GridArrangement(15, 15);
        BlockContainer container = createEmptyContainer();
        Range range = new Range(-3367.341901, 219.4320566834);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        
        Size2D result = grid.arrangeRR(container, null, constraint);
        
        assertEquals("Empty container should have zero dimensions", 
                    0.0, result.getWidth(), 0.01);
        assertEquals("Empty container should have zero dimensions", 
                    0.0, result.getHeight(), 0.01);
    }

    @Test
    public void testArrangeWithNegativeRange() {
        GridArrangement grid = new GridArrangement(-919, -919);
        BlockContainer container = createEmptyContainer();
        Range negativeRange = new Range(-919, -919);
        RectangleConstraint constraint = new RectangleConstraint(negativeRange, negativeRange);
        
        Size2D result = grid.arrangeRR(container, null, constraint);
        
        assertEquals("Should handle negative ranges", -919.0, result.getWidth(), 0.01);
        assertEquals("Should handle negative ranges", -919.0, result.getHeight(), 0.01);
    }

    @Test(expected = NullPointerException.class)
    public void testArrangeWithNullContainer() {
        GridArrangement grid = new GridArrangement(-653, -653);
        
        grid.arrangeNN(null, null);
    }

    @Test(expected = StackOverflowError.class)
    public void testArrangeWithSelfReferencingContainer() {
        GridArrangement grid = new GridArrangement(10, 10);
        BlockContainer container = createEmptyContainer();
        
        // Create circular reference
        container.add(container);
        
        RectangleConstraint noConstraint = RectangleConstraint.NONE;
        grid.arrange(container, null, noConstraint);
    }

    @Test
    public void testArrangeWithNullBlock() {
        GridArrangement grid = new GridArrangement(118, 118);
        BlockContainer container = createEmptyContainer();
        container.add(null); // Add null block
        
        RectangleConstraint noConstraint = RectangleConstraint.NONE;
        Size2D result = grid.arrangeFN(container, null, noConstraint);
        
        assertEquals("Should handle null blocks gracefully", 0.0, result.getWidth(), 0.01);
        assertEquals("Should handle null blocks gracefully", 0.0, result.getHeight(), 0.01);
    }

    @Test
    public void testClearMethod() {
        GridArrangement grid = new GridArrangement(-3031, -3031);
        
        // Should not throw exception
        grid.clear();
    }

    @Test
    public void testAddMethod() {
        GridArrangement grid = new GridArrangement(3306, 3306);
        LabelBlock label = new LabelBlock("test label");
        
        // Should not throw exception
        grid.add(label, "test key");
    }

    @Test
    public void testArrangeWithZeroGridDimensions() {
        GridArrangement grid = new GridArrangement(0, 0);
        BlockContainer container = createEmptyContainer();
        RectangleConstraint noConstraint = RectangleConstraint.NONE;
        
        Size2D result = grid.arrange(container, null, noConstraint);
        
        assertTrue("Result should have valid dimensions", 
                  !Double.isNaN(result.getWidth()) || !Double.isNaN(result.getHeight()));
    }

    @Test
    public void testArrangeWithLargeGridDimensions() {
        GridArrangement grid = new GridArrangement(2958465, 2958465);
        BlockContainer container = createEmptyContainer();
        RectangleConstraint noConstraint = RectangleConstraint.NONE;
        
        Size2D result = grid.arrangeFF(container, null, noConstraint);
        
        assertNotNull("Should return valid size", result);
    }

    // Helper method to create empty container
    private BlockContainer createEmptyContainer() {
        return new BlockContainer();
    }

    // Helper method to create container with arrangement
    private BlockContainer createContainerWithArrangement(Arrangement arrangement) {
        return new BlockContainer(arrangement);
    }

    // Helper method to create range constraint
    private RectangleConstraint createRangeConstraint(double value) {
        Range range = new Range(value, value);
        return new RectangleConstraint(range, range);
    }

    // Helper method to create fixed constraint
    private RectangleConstraint createFixedConstraint(double width, double height) {
        return new RectangleConstraint(width, height);
    }
}