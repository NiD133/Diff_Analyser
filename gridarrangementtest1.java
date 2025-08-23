package org.jfree.chart.block;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    /**
     * Tests for the equals() method, ensuring it adheres to the general contract.
     */
    @Nested
    @DisplayName("equals() contract")
    class EqualsContract {

        @Test
        @DisplayName("An object should be equal to itself")
        void testEquals_Reflexive() {
            GridArrangement arrangement = new GridArrangement(10, 20);
            assertEquals(arrangement, arrangement);
        }

        @Test
        @DisplayName("Two arrangements with the same rows and columns should be equal")
        void testEquals_Symmetric() {
            GridArrangement arrangement1 = new GridArrangement(10, 20);
            GridArrangement arrangement2 = new GridArrangement(10, 20);
            assertEquals(arrangement1, arrangement2, "Arrangements with same dimensions should be equal.");
            assertEquals(arrangement2, arrangement1, "Equality should be symmetric.");
        }

        @Test
        @DisplayName("An arrangement should not be equal to null")
        void testEquals_withNull() {
            GridArrangement arrangement = new GridArrangement(10, 20);
            assertNotEquals(null, arrangement);
        }

        @Test
        @DisplayName("An arrangement should not be equal to an object of a different type")
        void testEquals_withDifferentObjectType() {
            GridArrangement arrangement = new GridArrangement(10, 20);
            assertNotEquals(arrangement, new Object());
        }

        @Test
        @DisplayName("Two arrangements with different row counts should not be equal")
        void testEquals_whenRowsDiffer() {
            GridArrangement arrangement1 = new GridArrangement(10, 20);
            GridArrangement arrangement2 = new GridArrangement(99, 20);
            assertNotEquals(arrangement1, arrangement2);
        }

        @Test
        @DisplayName("Two arrangements with different column counts should not be equal")
        void testEquals_whenColumnsDiffer() {
            GridArrangement arrangement1 = new GridArrangement(10, 20);
            GridArrangement arrangement2 = new GridArrangement(10, 99);
            assertNotEquals(arrangement1, arrangement2);
        }
    }
}