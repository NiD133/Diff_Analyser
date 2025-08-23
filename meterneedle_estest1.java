package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MeterNeedle} class.
 * This test uses a concrete subclass, {@link PlumNeedle}, to test the
 * functionality inherited from the abstract MeterNeedle class.
 */
public class MeterNeedleTest {

    /**
     * Verifies that setRotateY() correctly updates the 'rotateY' property
     * and does not cause unintended side effects on other properties like 'rotateX'.
     */
    @Test
    public void setRotateYShouldUpdateYRotationPointWithoutSideEffects() {
        // Arrange: Create a needle instance and define test values.
        PlumNeedle needle = new PlumNeedle();
        final double initialRotateX = needle.getRotateX(); // Capture the initial state.
        final double newRotateY = -2433.34;

        // Act: Call the method under test.
        needle.setRotateY(newRotateY);

        // Assert: Verify the outcome.
        assertEquals("The rotateY value should be updated to the new value.",
                newRotateY, needle.getRotateY(), 0.01);
        assertEquals("The rotateX value should remain unchanged after setting rotateY.",
                initialRotateX, needle.getRotateX(), 0.01);
    }
}