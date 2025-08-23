package org.jfree.data.xy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test suite for the equals() method of the XYInterval class.
 * It verifies the contract of the equals() method, ensuring it correctly
 * handles equality based on its field values.
 */
@DisplayName("XYInterval equals() contract")
class XYIntervalTest {

    private XYInterval interval1;

    @BeforeEach
    void setUp() {
        // A standard instance used for comparison in multiple tests.
        interval1 = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);
    }

    @Test
    @DisplayName("should be true for the same object instance")
    void equals_shouldReturnTrue_forSameInstance() {
        assertEquals(interval1, interval1, "An object must be equal to itself.");
    }

    @Test
    @DisplayName("should be true for two separate but identical objects")
    void equals_shouldReturnTrue_forIdenticalObjects() {
        // Arrange: Create a second object with the exact same values.
        XYInterval interval2 = new XYInterval(1.0, 2.0, 3.0, 2.5, 3.5);

        // Assert: The two distinct objects should be considered equal.
        assertEquals(interval1, interval2, "Objects with the same field values should be equal.");
        assertEquals(interval1.hashCode(), interval2.hashCode(), "Hash codes should be equal for equal objects.");
    }

    @Test
    @DisplayName("should be false when compared to null")
    void equals_shouldReturnFalse_forNull() {
        assertNotEquals(null, interval1, "An object should not be equal to null.");
    }

    @Test
    @DisplayName("should be false when compared to an object of a different type")
    void equals_shouldReturnFalse_forDifferentType() {
        Object otherObject = new Object();
        assertNotEquals(interval1, otherObject, "An XYInterval should not be equal to an object of a different type.");
    }

    @Nested
    @DisplayName("should be false when a single field differs")
    class WhenFieldsDiffer {

        @Test
        @DisplayName("and xLow is different")
        void equals_shouldReturnFalse_whenXLowDiffers() {
            XYInterval interval2 = new XYInterval(99.9, 2.0, 3.0, 2.5, 3.5);
            assertNotEquals(interval1, interval2);
        }

        @Test
        @DisplayName("and xHigh is different")
        void equals_shouldReturnFalse_whenXHighDiffers() {
            XYInterval interval2 = new XYInterval(1.0, 99.9, 3.0, 2.5, 3.5);
            assertNotEquals(interval1, interval2);
        }

        @Test
        @DisplayName("and y is different")
        void equals_shouldReturnFalse_whenYDiffers() {
            XYInterval interval2 = new XYInterval(1.0, 2.0, 99.9, 2.5, 3.5);
            assertNotEquals(interval1, interval2);
        }

        @Test
        @DisplayName("and yLow is different")
        void equals_shouldReturnFalse_whenYLowDiffers() {
            XYInterval interval2 = new XYInterval(1.0, 2.0, 3.0, 99.9, 3.5);
            assertNotEquals(interval1, interval2);
        }

        @Test
        @DisplayName("and yHigh is different")
        void equals_shouldReturnFalse_whenYHighDiffers() {
            XYInterval interval2 = new XYInterval(1.0, 2.0, 3.0, 2.5, 99.9);
            assertNotEquals(interval1, interval2);
        }
    }
}