package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Contains unit tests for the {@link MeterNeedle} class and its subclasses.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the outline stroke of a needle can be set to null.
     * A null stroke is a valid state, typically indicating that no outline
     * should be drawn.
     */
    @Test
    public void setOutlineStrokeShouldAcceptNullValue() {
        // Arrange: Create a ShipNeedle instance, which has a default outline stroke.
        // MeterNeedle is abstract, so we use a concrete implementation for the test.
        ShipNeedle needle = new ShipNeedle();
        assertNotNull("Precondition: The default outline stroke should not be null.", needle.getOutlineStroke());

        // Act: Set the outline stroke to null.
        needle.setOutlineStroke(null);

        // Assert: Verify that the outline stroke has been updated to null.
        assertNull("The outline stroke should have been updated to null.", needle.getOutlineStroke());
    }
}