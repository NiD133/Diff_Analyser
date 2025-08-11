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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * Tests for the standard object methods: equals(), immutability, and serialization.
     */
    @Nested
    @DisplayName("Standard Object Methods")
    class StandardObjectTests {

        /**
         * Verifies that the equals() method correctly distinguishes between different instances.
         */
        @Test
        public void equals_shouldDistinguishBasedOnProperties() {
            GridArrangement g1 = new GridArrangement(11, 22);
            GridArrangement g2 = new GridArrangement(11, 22);
            assertEquals(g1, g2);

            // Different rows
            GridArrangement g3 = new GridArrangement(33, 22);
            assertNotEquals(g1, g3);

            // Different columns
            GridArrangement g4 = new GridArrangement(11, 44);
            assertNotEquals(g1, g4);
        }

        /**
         * A GridArrangement instance is immutable, so it does not need to be cloneable.
         */
        @Test
        public void isImmutable_shouldNotBeCloneable() {
            GridArrangement arrangement = new GridArrangement(1, 2);
            assertFalse(arrangement instanceof Cloneable);
        }

        /**
         * Verifies that serialization and deserialization produce an equal object.
         */
        @Test
        public void serialization_shouldPreserveObjectState() {
            GridArrangement original = new GridArrangement(33, 44);
            GridArrangement deserialized = TestUtils.serialised(original);
            assertEquals(original, deserialized);
        }
    }

    /**
     * Tests the arrangement logic for a grid that is fully populated with blocks.
     */
    @Nested
    @DisplayName("Arrange a Fully Populated Grid")
    class ArrangeFullyPopulatedGrid {

        /**
         * Provides test cases with different constraints and their expected resulting sizes.
         * The abbreviations in the description stand for constraint types on (Width, Height):
         * N=None, F=Fixed, R=Range.
         */
        private static Stream<Arguments> constraintAndExpectedSizeProvider() {
            return Stream.of(
                Arguments.of("NN: No constraints", RectangleConstraint.NONE, 90.0, 33.0),
                Arguments.of("FN: Fixed width, No height",
                    RectangleConstraint.NONE.toFixedWidth(100.0), 100.0, 33.0),
                Arguments.of("NF: No width, Fixed height",
                    RectangleConstraint.NONE.toFixedHeight(100.0), 90.0, 100.0),
                Arguments.of("RN: Ranged width, No height",
                    RectangleConstraint.NONE.toRangeWidth(new Range(40.0, 60.0)), 60.0, 33.0),
                Arguments.of("NR: No width, Ranged height",
                    RectangleConstraint.NONE.toRangeHeight(new Range(40.0, 60.0)), 90.0, 40.0),
                Arguments.of("RF: Ranged width, Fixed height",
                    new RectangleConstraint(new Range(40.0, 60.0), 100.0), 60.0, 100.0),
                Arguments.of("RR: Ranged width, Ranged height",
                    new RectangleConstraint(new Range(40.0, 60.0), new Range(50.0, 70.0)), 60.0, 50.0)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("constraintAndExpectedSizeProvider")
        public void arrange_shouldProduceCorrectSize_forVariousConstraints(
            String description, RectangleConstraint constraint, double expectedWidth, double expectedHeight) {
            
            BlockContainer container = createFullyPopulated1x3Container();
            Size2D size = container.arrange(null, constraint);

            assertEquals(expectedWidth, size.width, EPSILON, "Width should match expected value");
            assertEquals(expectedHeight, size.height, EPSILON, "Height should match expected value");
        }

        /**
         * Creates a 1x3 container with three blocks of different sizes.
         * Total natural width = 10 + 20 + 30 = 60 (but max of col widths is 30, so 30*3=90)
         * Total natural height = max(11, 22, 33) = 33
         */
        private BlockContainer createFullyPopulated1x3Container() {
            BlockContainer container = new BlockContainer(new GridArrangement(1, 3));
            container.add(new EmptyBlock(10, 11));
            container.add(new EmptyBlock(20, 22));
            container.add(new EmptyBlock(30, 33));
            return container;
        }
    }

    /**
     * Tests the arrangement logic for a grid containing null blocks.
     */
    @Nested
    @DisplayName("Arrange a Grid with Null Blocks")
    class ArrangeWithNullBlocks {

        private static Stream<Arguments> constraintAndExpectedSizeProvider() {
            return Stream.of(
                Arguments.of("FF: Fixed width and height", new RectangleConstraint(20, 10), 20.0, 10.0),
                Arguments.of("FN: Fixed width, No height", RectangleConstraint.NONE.toFixedWidth(10), 10.0, 0.0),
                Arguments.of("FR: Fixed width, Ranged height", new RectangleConstraint(30.0, new Range(5.0, 10.0)), 30.0, 5.0),
                Arguments.of("NN: No constraints", RectangleConstraint.NONE, 0.0, 0.0)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("constraintAndExpectedSizeProvider")
        public void arrange_shouldHandleNullBlockGracefully(
            String description, RectangleConstraint constraint, double expectedWidth, double expectedHeight) {
            
            BlockContainer container = createContainerWithNullBlock();
            Size2D size = container.arrange(null, constraint);

            assertEquals(expectedWidth, size.getWidth(), EPSILON, "Width should match expected value");
            assertEquals(expectedHeight, size.getHeight(), EPSILON, "Height should match expected value");
        }

        private BlockContainer createContainerWithNullBlock() {
            BlockContainer container = new BlockContainer(new GridArrangement(1, 1));
            container.add(null);
            return container;
        }
    }

    /**
     * Tests the arrangement logic for a grid that is not fully populated with blocks.
     */
    @Nested
    @DisplayName("Arrange a Partially Populated Grid")
    class ArrangePartiallyPopulatedGrid {

        private static Stream<Arguments> constraintAndExpectedSizeProvider() {
            return Stream.of(
                Arguments.of("FF: Fixed width and height", new RectangleConstraint(200, 100), 200.0, 100.0),
                Arguments.of("FN: Fixed width, No height", RectangleConstraint.NONE.toFixedWidth(30.0), 30.0, 10.0),
                Arguments.of("FR: Fixed width, Ranged height", new RectangleConstraint(30.0, new Range(5.0, 10.0)), 30.0, 10.0),
                Arguments.of("NN: No constraints", RectangleConstraint.NONE, 15.0, 10.0)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("constraintAndExpectedSizeProvider")
        public void arrange_shouldHandleFewerBlocksThanGridSpaces(
            String description, RectangleConstraint constraint, double expectedWidth, double expectedHeight) {
            
            BlockContainer container = createPartiallyPopulated2x3Container();
            Size2D size = container.arrange(null, constraint);

            assertEquals(expectedWidth, size.getWidth(), EPSILON, "Width should match expected value");
            assertEquals(expectedHeight, size.getHeight(), EPSILON, "Height should match expected value");
        }

        /**
         * Creates a 2x3 grid container with only one block.
         */
        private BlockContainer createPartiallyPopulated2x3Container() {
            BlockContainer container = new BlockContainer(new GridArrangement(2, 3));
            container.add(new EmptyBlock(5, 5));
            return container;
        }
    }
}