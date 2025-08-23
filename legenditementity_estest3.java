package org.jfree.chart.entity;

import org.junit.Test;
import java.awt.Shape;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when the 'area' argument is null. The underlying null check is
     * performed by the superclass, ChartEntity.
     */
    @Test
    public void constructorShouldThrowExceptionForNullArea() {
        try {
            // Attempt to create an entity with a null shape, which is not allowed.
            new LegendItemEntity<>((Shape) null);
            fail("Expected an IllegalArgumentException to be thrown for a null 'area'.");
        } catch (IllegalArgumentException e) {
            // This is the expected outcome.
            // Verify that the exception message is correct.
            assertEquals("Null 'area' argument.", e.getMessage());
        }
    }
}