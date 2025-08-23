package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link MeterNeedle} class and its subclasses.
 * This suite focuses on property initialization, modification, and equality checks.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the ShipNeedle's default constructor correctly initializes
     * its properties to their default values.
     */
    @Test
    public void shipNeedleShouldBeCreatedWithDefaultValues() {
        // Arrange: Create a new ShipNeedle instance.
        ShipNeedle needle = new ShipNeedle();

        // Assert: Verify that the default properties are set as expected.
        assertEquals("Default size should be 5", 5, needle.getSize());
        assertEquals("Default rotateX should be 0.5", 0.5, needle.getRotateX(), 0.0);
        assertEquals("Default rotateY should be 0.5", 0.5, needle.getRotateY(), 0.0);
    }

    /**
     * Ensures that the setRotateX() method correctly updates the needle's
     * horizontal rotation point.
     */
    @Test
    public void setRotateXShouldUpdateTheRotateXValue() {
        // Arrange: Create a needle and define a new value for rotateX.
        ShipNeedle needle = new ShipNeedle();
        double newRotateX = 0.8;

        // Act: Update the rotateX property.
        needle.setRotateX(newRotateX);

        // Assert: Check that the getter returns the new value.
        assertEquals("The rotateX value should be updated by the setter.", newRotateX, needle.getRotateX(), 0.0);
    }

    /**
     * Verifies that two different MeterNeedle subclasses are considered equal
     * if all their base properties are identical.
     */
    @Test
    public void equalsShouldReturnTrueForDifferentNeedleTypesWithSameDefaultProperties() {
        // Arrange: Create instances of two different needle types.
        // By default, they share the same base properties.
        MeterNeedle shipNeedle = new ShipNeedle();
        MeterNeedle pointerNeedle = new PointerNeedle();

        // Act & Assert: The two needles should be considered equal based on their properties.
        assertTrue("Needles of different types with the same default properties should be equal.", shipNeedle.equals(pointerNeedle));
        assertEquals("Hashcodes should be equal for equal objects.", shipNeedle.hashCode(), pointerNeedle.hashCode());
    }
    
    /**
     * Verifies that the equals() method returns false when comparing two needles
     * with different property values.
     */
    @Test
    public void equalsShouldReturnFalseWhenPropertiesDiffer() {
        // Arrange: Create two needles and make one of their properties different.
        MeterNeedle needle1 = new ShipNeedle();
        MeterNeedle needle2 = new ShipNeedle();
        needle2.setSize(10); // Change a property on the second needle.

        // Act & Assert: The needles should no longer be considered equal.
        assertFalse("Needles with different properties should not be equal.", needle1.equals(needle2));
    }
}