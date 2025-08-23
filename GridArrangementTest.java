package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * Tests the equals() method of the GridArrangement class.
     */
    @Test
    public void testEqualsMethod() {
        GridArrangement arrangement1 = new GridArrangement(11, 22);
        GridArrangement arrangement2 = new GridArrangement(11, 22);
        assertEquals(arrangement1, arrangement2);
        assertEquals(arrangement2, arrangement1);

        arrangement1 = new GridArrangement(33, 22);
        assertNotEquals(arrangement1, arrangement2);
        arrangement2 = new GridArrangement(33, 22);
        assertEquals(arrangement1, arrangement2);

        arrangement1 = new GridArrangement(33, 44);
        assertNotEquals(arrangement1, arrangement2);
        arrangement2 = new GridArrangement(33, 44);
        assertEquals(arrangement1, arrangement2);
    }

    /**
     * Tests that GridArrangement is not cloneable.
     */
    @Test
    public void testCloningNotSupported() {
        GridArrangement arrangement = new GridArrangement(1, 2);
        assertFalse(arrangement instanceof Cloneable);
    }

    /**
     * Tests serialization and deserialization of GridArrangement.
     */
    @Test
    public void testSerialization() {
        GridArrangement original = new GridArrangement(33, 44);
        GridArrangement deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized);
    }

    /**
     * Tests arrangement with no constraints.
     */
    @Test
    public void testArrangeNoConstraints() {
        BlockContainer container = createTestContainer();
        Size2D size = container.arrange(null, RectangleConstraint.NONE);
        assertEquals(90.0, size.width, EPSILON);
        assertEquals(33.0, size.height, EPSILON);
    }

    /**
     * Tests arrangement with a fixed width and no height constraint.
     */
    @Test
    public void testArrangeFixedWidthNoHeightConstraint() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = new RectangleConstraint(100.0, null,
                LengthConstraintType.FIXED, 0.0, null,
                LengthConstraintType.NONE);
        Size2D size = container.arrange(null, constraint);
        assertEquals(100.0, size.width, EPSILON);
        assertEquals(33.0, size.height, EPSILON);
    }

    /**
     * Tests arrangement with a fixed height and no width constraint.
     */
    @Test
    public void testArrangeNoWidthConstraintFixedHeight() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE.toFixedHeight(100.0);
        Size2D size = container.arrange(null, constraint);
        assertEquals(90.0, size.width, EPSILON);
        assertEquals(100.0, size.height, EPSILON);
    }

    /**
     * Tests arrangement with a range for the width and a fixed height.
     */
    @Test
    public void testArrangeRangeWidthFixedHeight() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = new RectangleConstraint(new Range(40.0, 60.0), 100.0);
        Size2D size = container.arrange(null, constraint);
        assertEquals(60.0, size.width, EPSILON);
        assertEquals(100.0, size.height, EPSILON);
    }

    /**
     * Tests arrangement with a range for both width and height.
     */
    @Test
    public void testArrangeRangeWidthAndHeight() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = new RectangleConstraint(new Range(40.0, 60.0), new Range(50.0, 70.0));
        Size2D size = container.arrange(null, constraint);
        assertEquals(60.0, size.width, EPSILON);
        assertEquals(50.0, size.height, EPSILON);
    }

    /**
     * Tests arrangement with a range for the width and no height constraint.
     */
    @Test
    public void testArrangeRangeWidthNoHeightConstraint() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE.toRangeWidth(new Range(40.0, 60.0));
        Size2D size = container.arrange(null, constraint);
        assertEquals(60.0, size.width, EPSILON);
        assertEquals(33.0, size.height, EPSILON);
    }

    /**
     * Tests arrangement with a range for the height and no width constraint.
     */
    @Test
    public void testArrangeNoWidthConstraintRangeHeight() {
        BlockContainer container = createTestContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE.toRangeHeight(new Range(40.0, 60.0));
        Size2D size = container.arrange(null, constraint);
        assertEquals(90.0, size.width, EPSILON);
        assertEquals(40.0, size.height, EPSILON);
    }

    /**
     * Tests handling of null blocks in the layout with fixed width and height.
     */
    @Test
    public void testNullBlockFixedWidthAndHeight() {
        BlockContainer container = new BlockContainer(new GridArrangement(1, 1));
        container.add(null);
        Size2D size = container.arrange(null, new RectangleConstraint(20, 10));
        assertEquals(20.0, size.getWidth(), EPSILON);
        assertEquals(10.0, size.getHeight(), EPSILON);
    }

    /**
     * Tests handling of null blocks in the layout with fixed width and no height constraint.
     */
    @Test
    public void testNullBlockFixedWidthNoHeightConstraint() {
        BlockContainer container = new BlockContainer(new GridArrangement(1, 1));
        container.add(null);
        Size2D size = container.arrange(null, RectangleConstraint.NONE.toFixedWidth(10));
        assertEquals(10.0, size.getWidth(), EPSILON);
        assertEquals(0.0, size.getHeight(), EPSILON);
    }

    /**
     * Tests handling of null blocks in the layout with fixed width and range height.
     */
    @Test
    public void testNullBlockFixedWidthRangeHeight() {
        BlockContainer container = new BlockContainer(new GridArrangement(1, 1));
        container.add(null);
        Size2D size = container.arrange(null, new RectangleConstraint(30.0, new Range(5.0, 10.0)));
        assertEquals(30.0, size.getWidth(), EPSILON);
        assertEquals(5.0, size.getHeight(), EPSILON);
    }

    /**
     * Tests handling of null blocks in the layout with no constraints.
     */
    @Test
    public void testNullBlockNoConstraints() {
        BlockContainer container = new BlockContainer(new GridArrangement(1, 1));
        container.add(null);
        Size2D size = container.arrange(null, RectangleConstraint.NONE);
        assertEquals(0.0, size.getWidth(), EPSILON);
        assertEquals(0.0, size.getHeight(), EPSILON);
    }

    /**
     * Tests handling of less blocks than grid spaces with fixed width and height.
     */
    @Test
    public void testGridNotFullFixedWidthAndHeight() {
        Block block = new EmptyBlock(5, 5);
        BlockContainer container = new BlockContainer(new GridArrangement(2, 3));
        container.add(block);
        Size2D size = container.arrange(null, new RectangleConstraint(200, 100));
        assertEquals(200.0, size.getWidth(), EPSILON);
        assertEquals(100.0, size.getHeight(), EPSILON);
    }

    /**
     * Tests handling of less blocks than grid spaces with fixed width and no height constraint.
     */
    @Test
    public void testGridNotFullFixedWidthNoHeightConstraint() {
        Block block = new EmptyBlock(5, 5);
        BlockContainer container = new BlockContainer(new GridArrangement(2, 3));
        container.add(block);
        Size2D size = container.arrange(null, RectangleConstraint.NONE.toFixedWidth(30.0));
        assertEquals(30.0, size.getWidth(), EPSILON);
        assertEquals(10.0, size.getHeight(), EPSILON);
    }

    /**
     * Tests handling of less blocks than grid spaces with fixed width and range height.
     */
    @Test
    public void testGridNotFullFixedWidthRangeHeight() {
        Block block = new EmptyBlock(5, 5);
        BlockContainer container = new BlockContainer(new GridArrangement(2, 3));
        container.add(block);
        Size2D size = container.arrange(null, new RectangleConstraint(30.0, new Range(5.0, 10.0)));
        assertEquals(30.0, size.getWidth(), EPSILON);
        assertEquals(10.0, size.getHeight(), EPSILON);
    }

    /**
     * Tests handling of less blocks than grid spaces with no constraints.
     */
    @Test
    public void testGridNotFullNoConstraints() {
        Block block = new EmptyBlock(5, 5);
        BlockContainer container = new BlockContainer(new GridArrangement(2, 3));
        container.add(block);
        Size2D size = container.arrange(null, RectangleConstraint.NONE);
        assertEquals(15.0, size.getWidth(), EPSILON);
        assertEquals(10.0, size.getHeight(), EPSILON);
    }

    /**
     * Creates a test container with predefined blocks.
     *
     * @return a BlockContainer instance.
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
}