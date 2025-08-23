package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * A test suite for the {@link MeterNeedle} class and its subclasses.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the equals() method does not have side effects on the object's state.
     * Specifically, it checks that comparing a needle to another does not alter its 'size' property.
     */
    @Test
    public void equals_shouldNotChangeInternalState() {
        // Arrange: Create a ShipNeedle with a specific, non-default size.
        // The negative value is used to ensure the setter/getter handles it correctly.
        final int testSize = -715_827_883;
        ShipNeedle shipNeedle = new ShipNeedle();
        shipNeedle.setSize(testSize);

        // Create another needle to compare against. It will have a different default size.
        PointerNeedle otherNeedle = new PointerNeedle();

        // Act: Call the equals() method.
        boolean areEqual = shipNeedle.equals(otherNeedle);

        // Assert: Confirm the comparison result is false (as expected for needles
        // with different sizes) and, most importantly, verify that the original
        // needle's size has not been modified.
        assertFalse("Needles with different properties should not be equal", areEqual);
        assertEquals("The size property should not be altered by the equals() comparison",
                testSize, shipNeedle.getSize());
    }
}