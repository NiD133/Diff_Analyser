package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link XYInterval} class.
 * This class tests the constructor, getters, and the equals() method.
 */
public class XYIntervalTest {

    private static final double DELTA = 0.000001;

    /**
     * Verifies that the constructor correctly initializes all the fields
     * and that the getter methods return the expected values.
     */
    @Test
    public void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange
        double xLow = -10.0;
        double xHigh = -5.0;
        double y = 0.0;
        double yLow = 12.5;
        double yHigh = 18.8;

        // Act
        XYInterval interval = new XYInterval(xLow, xHigh, y, yLow, yHigh);

        // Assert
        assertEquals("xLow should be initialized correctly", xLow, interval.getXLow(), DELTA);
        assertEquals("xHigh should be initialized correctly", xHigh, interval.getXHigh(), DELTA);
        assertEquals("y should be initialized correctly", y, interval.getY(), DELTA);
        assertEquals("yLow should be initialized correctly", yLow, interval.getYLow(), DELTA);
        assertEquals("yHigh should be initialized correctly", yHigh, interval.getYHigh(), DELTA);
    }

    /**
     * An object should be equal to itself.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparingObjectToItself() {
        // Arrange
        XYInterval interval = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);

        // Act & Assert
        assertTrue("An interval should be equal to itself.", interval.equals(interval));
    }

    /**
     * Two separate instances with identical values should be considered equal.
     */
    @Test
    public void equals_shouldReturnTrue_whenIntervalsHaveIdenticalValues() {
        // Arrange
        XYInterval interval1 = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);
        XYInterval interval2 = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);

        // Act & Assert
        assertTrue("Intervals with the same values should be equal.", interval1.equals(interval2));
        assertEquals("Hashcodes should be equal for equal objects.", interval1.hashCode(), interval2.hashCode());
    }

    /**
     * An interval should not be equal to null.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithNull() {
        // Arrange
        XYInterval interval = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);

        // Act & Assert
        assertFalse("An interval should not be equal to null.", interval.equals(null));
    }

    /**
     * An interval should not be equal to an object of a different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentClass() {
        // Arrange
        XYInterval interval = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);
        Object otherObject = new Object();

        // Act & Assert
        assertFalse("An interval should not be equal to an object of a different type.", interval.equals(otherObject));
    }

    /**
     * Tests that two intervals are not equal if their xLow values differ.
     */
    @Test
    public void equals_shouldReturnFalse_whenXLowIsDifferent() {
        // Arrange
        XYInterval interval1 = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);
        XYInterval interval2 = new XYInterval(99.0, 2.0, 3.0, 4.0, 5.0);

        // Act & Assert
        assertNotEquals("Intervals with different xLow values should not be equal.", interval1, interval2);
    }

    /**
     * Tests that two intervals are not equal if their xHigh values differ.
     */
    @Test
    public void equals_shouldReturnFalse_whenXHighIsDifferent() {
        // Arrange
        XYInterval interval1 = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);
        XYInterval interval2 = new XYInterval(1.0, 99.0, 3.0, 4.0, 5.0);

        // Act & Assert
        assertNotEquals("Intervals with different xHigh values should not be equal.", interval1, interval2);
    }

    /**
     * Tests that two intervals are not equal if their y values differ.
     */
    @Test
    public void equals_shouldReturnFalse_whenYIsDifferent() {
        // Arrange
        XYInterval interval1 = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);
        XYInterval interval2 = new XYInterval(1.0, 2.0, 99.0, 4.0, 5.0);

        // Act & Assert
        assertNotEquals("Intervals with different y values should not be equal.", interval1, interval2);
    }

    /**
     * Tests that two intervals are not equal if their yLow values differ.
     */
    @Test
    public void equals_shouldReturnFalse_whenYLowIsDifferent() {
        // Arrange
        XYInterval interval1 = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);
        XYInterval interval2 = new XYInterval(1.0, 2.0, 3.0, 99.0, 5.0);

        // Act & Assert
        assertNotEquals("Intervals with different yLow values should not be equal.", interval1, interval2);
    }

    /**
     * Tests that two intervals are not equal if their yHigh values differ.
     */
    @Test
    public void equals_shouldReturnFalse_whenYHighIsDifferent() {
        // Arrange
        XYInterval interval1 = new XYInterval(1.0, 2.0, 3.0, 4.0, 5.0);
        XYInterval interval2 = new XYInterval(1.0, 2.0, 3.0, 4.0, 99.0);

        // Act & Assert
        assertNotEquals("Intervals with different yHigh values should not be equal.", interval1, interval2);
    }
}