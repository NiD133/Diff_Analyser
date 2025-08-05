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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    @Nested
    class CoreFunctionalityTests {

        @Test
        void equals_ReturnsTrueForSameRowsAndColumns() {
            GridArrangement arrangement1 = new GridArrangement(11, 22);
            GridArrangement arrangement2 = new GridArrangement(11, 22);
            assertEquals(arrangement1, arrangement2);
        }

        @Test
        void equals_ReturnsFalseForDifferentRows() {
            GridArrangement arrangement1 = new GridArrangement(11, 22);
            GridArrangement arrangement2 = new GridArrangement(33, 22);
            assertNotEquals(arrangement1, arrangement2);
        }

        @Test
        void equals_ReturnsFalseForDifferentColumns() {
            GridArrangement arrangement1 = new GridArrangement(11, 22);
            GridArrangement arrangement2 = new GridArrangement(11, 44);
            assertNotEquals(arrangement1, arrangement2);
        }

        @Test
        void cloning_ShouldNotBeSupported() {
            GridArrangement arrangement = new GridArrangement(1, 2);
            assertFalse(arrangement instanceof Cloneable);
        }

        @Test
        void serialization_ShouldPreserveState() {
            GridArrangement original = new GridArrangement(33, 44);
            GridArrangement deserialized = TestUtils.serialised(original);
            assertEquals(original, deserialized);
        }
    }

    @Nested
    class FullGridArrangementTests {
        private BlockContainer container;

        void setupFullContainer() {
            Block block1 = new EmptyBlock(10, 11);
            Block block2 = new EmptyBlock(20, 22);
            Block block3 = new EmptyBlock(30, 33);
            container = new BlockContainer(new GridArrangement(1, 3));
            container.add(block1);
            container.add(block2);
            container.add(block3);
        }

        @Test
        void arrange_WithNoConstraints() {
            setupFullContainer();
            Size2D size = container.arrange(null, RectangleConstraint.NONE);
            assertEquals(90.0, size.width, EPSILON, "Width should match sum of block widths");
            assertEquals(33.0, size.height, EPSILON, "Height should match maximum block height");
        }

        @Test
        void arrange_WithFixedWidthAndUnconstrainedHeight() {
            setupFullContainer();
            RectangleConstraint constraint = new RectangleConstraint(
                100.0, null, LengthConstraintType.FIXED,
                0.0, null, LengthConstraintType.NONE
            );
            Size2D size = container.arrange(null, constraint);
            assertEquals(100.0, size.width, EPSILON, "Width should match fixed constraint");
            assertEquals(33.0, size.height, EPSILON, "Height should remain unchanged");
        }

        @Test
        void arrange_WithFixedHeightAndUnconstrainedWidth() {
            setupFullContainer();
            RectangleConstraint constraint = RectangleConstraint.NONE.toFixedHeight(100.0);
            Size2D size = container.arrange(null, constraint);
            assertEquals(90.0, size.width, EPSILON, "Width should remain unchanged");
            assertEquals(100.0, size.height, EPSILON, "Height should match fixed constraint");
        }

        @Test
        void arrange_WithWidthRangeAndFixedHeight() {
            setupFullContainer();
            RectangleConstraint constraint = new RectangleConstraint(
                new Range(40.0, 60.0), 100.0
            );
            Size2D size = container.arrange(null, constraint);
            assertEquals(60.0, size.width, EPSILON, "Width should use maximum range value");
            assertEquals(100.0, size.height, EPSILON, "Height should match fixed constraint");
        }

        @Test
        void arrange_WithWidthRangeAndHeightRange() {
            setupFullContainer();
            RectangleConstraint constraint = new RectangleConstraint(
                new Range(40.0, 60.0), new Range(50.0, 70.0)
            );
            Size2D size = container.arrange(null, constraint);
            assertEquals(60.0, size.width, EPSILON, "Width should use maximum range value");
            assertEquals(50.0, size.height, EPSILON, "Height should use minimum range value");
        }

        @Test
        void arrange_WithWidthRangeAndUnconstrainedHeight() {
            setupFullContainer();
            RectangleConstraint constraint = RectangleConstraint.NONE.toRangeWidth(new Range(40.0, 60.0));
            Size2D size = container.arrange(null, constraint);
            assertEquals(60.0, size.width, EPSILON, "Width should use maximum range value");
            assertEquals(33.0, size.height, EPSILON, "Height should remain unchanged");
        }

        @Test
        void arrange_WithHeightRangeAndUnconstrainedWidth() {
            setupFullContainer();
            RectangleConstraint constraint = RectangleConstraint.NONE.toRangeHeight(new Range(40.0, 60.0));
            Size2D size = container.arrange(null, constraint);
            assertEquals(90.0, size.width, EPSILON, "Width should remain unchanged");
            assertEquals(40.0, size.height, EPSILON, "Height should use minimum range value");
        }
    }

    @Nested
    class NullBlockHandlingTests {
        private BlockContainer createContainerWithNullBlock() {
            BlockContainer container = new BlockContainer(new GridArrangement(1, 1));
            container.add(null);
            return container;
        }

        @Test
        void arrange_WithFixedWidthAndHeight() {
            BlockContainer container = createContainerWithNullBlock();
            Size2D size = container.arrange(null, new RectangleConstraint(20, 10));
            assertEquals(20.0, size.getWidth(), EPSILON, "Width should match constraint");
            assertEquals(10.0, size.getHeight(), EPSILON, "Height should match constraint");
        }

        @Test
        void arrange_WithFixedWidth() {
            BlockContainer container = createContainerWithNullBlock();
            Size2D size = container.arrange(null, RectangleConstraint.NONE.toFixedWidth(10));
            assertEquals(10.0, size.getWidth(), EPSILON, "Width should match constraint");
            assertEquals(0.0, size.getHeight(), EPSILON, "Height should be zero for null block");
        }

        @Test
        void arrange_WithFixedWidthAndHeightRange() {
            BlockContainer container = createContainerWithNullBlock();
            RectangleConstraint constraint = new RectangleConstraint(
                30.0, new Range(5.0, 10.0)
            );
            Size2D size = container.arrange(null, constraint);
            assertEquals(30.0, size.getWidth(), EPSILON, "Width should match constraint");
            assertEquals(5.0, size.getHeight(), EPSILON, "Height should use minimum range value");
        }

        @Test
        void arrange_WithNoConstraints() {
            BlockContainer container = createContainerWithNullBlock();
            Size2D size = container.arrange(null, RectangleConstraint.NONE);
            assertEquals(0.0, size.getWidth(), EPSILON, "Width should be zero for null block");
            assertEquals(0.0, size.getHeight(), EPSILON, "Height should be zero for null block");
        }
    }

    @Nested
    class IncompleteGridHandlingTests {
        private BlockContainer createContainerWithSingleBlock() {
            Block block = new EmptyBlock(5, 5);
            BlockContainer container = new BlockContainer(new GridArrangement(2, 3));
            container.add(block);
            return container;
        }

        @Test
        void arrange_WithFixedWidthAndHeight() {
            BlockContainer container = createContainerWithSingleBlock();
            Size2D size = container.arrange(null, new RectangleConstraint(200, 100));
            assertEquals(200.0, size.getWidth(), EPSILON, "Width should match constraint");
            assertEquals(100.0, size.getHeight(), EPSILON, "Height should match constraint");
        }

        @Test
        void arrange_WithFixedWidth() {
            BlockContainer container = createContainerWithSingleBlock();
            Size2D size = container.arrange(null, RectangleConstraint.NONE.toFixedWidth(30.0));
            assertEquals(30.0, size.getWidth(), EPSILON, "Width should match constraint");
            assertEquals(10.0, size.getHeight(), EPSILON, "Height should be calculated based on grid layout");
        }

        @Test
        void arrange_WithFixedWidthAndHeightRange() {
            BlockContainer container = createContainerWithSingleBlock();
            RectangleConstraint constraint = new RectangleConstraint(
                30.0, new Range(5.0, 10.0)
            );
            Size2D size = container.arrange(null, constraint);
            assertEquals(30.0, size.getWidth(), EPSILON, "Width should match constraint");
            assertEquals(10.0, size.getHeight(), EPSILON, "Height should use maximum range value");
        }

        @Test
        void arrange_WithNoConstraints() {
            BlockContainer container = createContainerWithSingleBlock();
            Size2D size = container.arrange(null, RectangleConstraint.NONE);
            assertEquals(15.0, size.getWidth(), EPSILON, "Width should be calculated based on grid columns");
            assertEquals(10.0, size.getHeight(), EPSILON, "Height should be calculated based on grid rows");
        }
    }
}