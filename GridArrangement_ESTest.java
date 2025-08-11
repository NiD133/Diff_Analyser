package org.jfree.chart.block;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.block.GridArrangement;
import org.jfree.chart.block.LabelBlock;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.data.Range;

import java.awt.Graphics2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A suite of modern, understandable tests for the {@link GridArrangement} class.
 */
@DisplayName("GridArrangement")
class GridArrangement_ESTest {

    private BlockContainer container;

    @BeforeEach
    void setUp() {
        container = new BlockContainer();
    }

    @Nested
    @DisplayName("equals() method")
    class EqualsTests {

        @Test
        @DisplayName("should return true for the same object instance")
        void equals_sameObject_returnsTrue() {
            GridArrangement arrangement = new GridArrangement(2, 3);
            assertEquals(arrangement, arrangement);
        }

        @Test
        @DisplayName("should return true for objects with the same dimensions")
        void equals_sameDimensions_returnsTrue() {
            GridArrangement arrangement1 = new GridArrangement(2, 3);
            GridArrangement arrangement2 = new GridArrangement(2, 3);
            assertEquals(arrangement1, arrangement2);
        }

        @Test
        @DisplayName("should return false for objects with different rows")
        void equals_differentRows_returnsFalse() {
            GridArrangement arrangement1 = new GridArrangement(2, 3);
            GridArrangement arrangement2 = new GridArrangement(5, 3);
            assertNotEquals(arrangement1, arrangement2);
        }

        @Test
        @DisplayName("should return false for objects with different columns")
        void equals_differentColumns_returnsFalse() {
            GridArrangement arrangement1 = new GridArrangement(2, 3);
            GridArrangement arrangement2 = new GridArrangement(2, 5);
            assertNotEquals(arrangement1, arrangement2);
        }

        @Test
        @DisplayName("should return false when compared to null")
        void equals_nullObject_returnsFalse() {
            GridArrangement arrangement = new GridArrangement(2, 3);
            assertNotEquals(null, arrangement);
        }

        @Test
        @DisplayName("should return false when compared to a different class")
        void equals_differentClass_returnsFalse() {
            GridArrangement arrangement = new GridArrangement(2, 3);
            assertNotEquals(arrangement, new Object());
        }
    }

    @Nested
    @DisplayName("Lifecycle methods")
    class LifecycleTests {
        @Test
        @DisplayName("add() should not throw an exception (no-op)")
        void add_doesNotThrowException() {
            GridArrangement arrangement = new GridArrangement(1, 1);
            LabelBlock block = new LabelBlock("Test");
            assertDoesNotThrow(() -> arrangement.add(block, "Key"));
        }

        @Test
        @DisplayName("clear() should not throw an exception (no-op)")
        void clear_doesNotThrowException() {
            GridArrangement arrangement = new GridArrangement(1, 1);
            assertDoesNotThrow(arrangement::clear);
        }
    }

    @Nested
    @DisplayName("Arrangement of an empty container")
    class EmptyContainerTests {

        @Test
        @DisplayName("with fixed size constraint should return the fixed size")
        void arrange_withFixedSize_returnsFixedSize() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(2, 2);
            RectangleConstraint constraint = new RectangleConstraint(100.0, 200.0);

            // Act
            Size2D size = arrangement.arrange(container, null, constraint);

            // Assert
            assertEquals(100.0, size.getWidth());
            assertEquals(200.0, size.getHeight());
        }

        @Test
        @DisplayName("with no constraint should return zero size for non-zero grid")
        void arrange_withNoConstraint_returnsZeroSize() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(2, 2);
            RectangleConstraint constraint = RectangleConstraint.NONE;

            // Act
            Size2D size = arrangement.arrange(container, null, constraint);

            // Assert
            assertEquals(0.0, size.getWidth());
            assertEquals(0.0, size.getHeight());
        }
        
        @Test
        @DisplayName("with no constraint and zero rows should return NaN height")
        void arrange_withNoConstraintAndZeroRows_returnsSizeWithNaNHeight() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(0, 2); // Zero rows
            RectangleConstraint constraint = RectangleConstraint.NONE;

            // Act
            Size2D size = arrangement.arrange(container, null, constraint);

            // Assert
            assertEquals(0.0, size.getWidth());
            assertTrue(Double.isNaN(size.getHeight()));
        }

        @Test
        @DisplayName("with range constraint should return the constrained zero size")
        void arrange_withRangeConstraint_returnsConstrainedZeroSize() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(2, 2);
            Range widthRange = new Range(50.0, 100.0);
            Range heightRange = new Range(150.0, 200.0);
            RectangleConstraint constraint = new RectangleConstraint(widthRange, heightRange);

            // Act
            Size2D size = arrangement.arrange(container, null, constraint);

            // Assert
            // For an empty container, the size is the lower bound of the range, as 0.0 is constrained by the range.
            assertEquals(50.0, size.getWidth());
            assertEquals(150.0, size.getHeight());
        }
    }

    @Nested
    @DisplayName("Arrangement of a container with blocks")
    class ContainerWithBlocksTests {

        @Test
        @DisplayName("with no constraint should calculate size based on block dimensions")
        void arrange_withNoConstraint_calculatesSizeBasedOnBlocks() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(2, 1);
            container.add(new EmptyBlock(10, 20)); // Max width = 40, Max height = 30
            container.add(new EmptyBlock(40, 30));
            RectangleConstraint constraint = RectangleConstraint.NONE;

            // Act
            Size2D size = arrangement.arrange(container, null, constraint);

            // Assert
            // Total width = max_width * columns = 40 * 1 = 40
            // Total height = max_height_row1 + max_height_row2 = 20 + 30 = 50
            assertEquals(40.0, size.getWidth());
            assertEquals(50.0, size.getHeight());
        }

        @Test
        @DisplayName("with fewer blocks than grid cells should arrange correctly")
        void arrange_withFewerBlocksThanGridCells_arrangesCorrectly() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(2, 2);
            container.add(new EmptyBlock(10, 20)); // Cell (0,0)
            // Cell (0,1) is empty
            container.add(new EmptyBlock(30, 40)); // Cell (1,0)
            RectangleConstraint constraint = RectangleConstraint.NONE;

            // Act
            Size2D size = arrangement.arrange(container, null, constraint);

            // Assert
            // Row 1: max width=10, max height=20
            // Row 2: max width=30, max height=40
            // Total width = (max_width_col1 + max_width_col2) = 30 + 0 = 30
            // Total height = (max_height_row1 + max_height_row2) = 20 + 40 = 60
            assertEquals(30.0, size.getWidth());
            assertEquals(60.0, size.getHeight());
        }

        @Test
        @DisplayName("with a null block in the container should handle it gracefully")
        void arrange_withNullBlock_handlesGracefully() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(1, 2);
            container.add(new EmptyBlock(10, 20));
            container.add(null); // Add a null block
            RectangleConstraint constraint = RectangleConstraint.NONE;

            // Act
            Size2D size = arrangement.arrange(container, null, constraint);

            // Assert
            // Total width = 10 + 0 = 10
            // Total height = max(20, 0) = 20
            assertEquals(10.0, size.getWidth());
            assertEquals(20.0, size.getHeight());
        }
    }

    @Nested
    @DisplayName("Exception handling")
    class ExceptionTests {

        @Test
        @DisplayName("with null Graphics2D and blocks to measure should throw NullPointerException")
        void arrange_withNullGraphicsAndBlocksToMeasure_throwsNullPointerException() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(1, 1);
            // LabelBlock needs Graphics2D to measure its size
            container.add(new LabelBlock("Test"));
            RectangleConstraint constraint = RectangleConstraint.NONE;

            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                arrangement.arrange(container, null, constraint);
            });
        }

        @Test
        @DisplayName("with a recursively added container should throw StackOverflowError")
        void arrange_withRecursiveContainer_throwsStackOverflowError() {
            // Arrange
            GridArrangement arrangement = new GridArrangement(1, 1);
            container.add(container); // Recursive add
            RectangleConstraint constraint = RectangleConstraint.NONE;

            // Act & Assert
            assertThrows(StackOverflowError.class, () -> {
                arrangement.arrange(container, null, constraint);
            });
        }
        
        @Test
        @DisplayName("with an inner container having an unsupported arrangement should throw RuntimeException")
        void arrange_withUnsupportedInnerArrangement_throwsRuntimeException() {
            // Arrange
            GridArrangement gridArrangement = new GridArrangement(1, 1);
            
            // BorderArrangement does not implement arrange() for some constraint types
            BlockContainer innerContainer = new BlockContainer(new BorderArrangement());
            container.add(innerContainer);

            // This constraint will cause BorderArrangement to throw a RuntimeException
            RectangleConstraint constraint = new RectangleConstraint(
                new Range(10, 10), 10.0
            );

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                gridArrangement.arrange(container, null, constraint);
            }, "Not implemented.");
        }

        @Test
        @DisplayName("with an empty inner container should throw IndexOutOfBoundsException")
        void arrange_withEmptyInnerContainer_throwsIndexOutOfBoundsException() {
            // Arrange
            GridArrangement gridArrangement = new GridArrangement(1, 1);
            
            // CenterArrangement tries to access the first block, which doesn't exist
            BlockContainer innerContainer = new BlockContainer(new CenterArrangement());
            container.add(innerContainer);

            RectangleConstraint constraint = new RectangleConstraint(10, 10);

            // Act & Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                gridArrangement.arrange(container, null, constraint);
            });
        }
    }
}