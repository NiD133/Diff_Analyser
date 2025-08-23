package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import java.awt.Color;

/**
 * Contains tests for the equals() method of the MeterNeedle class and its subclasses.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the equals() method returns false when comparing two different
     * subclasses of MeterNeedle.
     * <p>
     * The original test called equals() on two different needle types but failed
     * to assert the result, instead checking unrelated default properties. This
     * revised test clarifies the intent by focusing on the inequality of different types.
     * </p>
     */
    @Test
    public void equals_whenComparingDifferentNeedleTypes_shouldReturnFalse() {
        // Arrange: Create two instances of different MeterNeedle subclasses.
        // A property is set on one to further ensure they are not identical.
        ShipNeedle shipNeedle = new ShipNeedle();
        shipNeedle.setFillPaint(Color.BLUE);

        PointerNeedle pointerNeedle = new PointerNeedle();

        // Act: Compare the two objects using the equals method.
        boolean areEqual = shipNeedle.equals(pointerNeedle);

        // Assert: The result should be false, as the objects are of different types.
        assertFalse("A ShipNeedle instance should not be equal to a PointerNeedle instance.", areEqual);
    }
}