package org.jfree.chart.annotations;

import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for the cloning behavior of the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the clone() method creates an independent copy that is
     * equal to the original object, fulfilling the cloning contract.
     */
    @Test
    void testClone() throws CloneNotSupportedException {
        // Arrange: Create an instance of XYLineAnnotation with specific properties.
        Stroke stroke = new BasicStroke(2.0f);
        XYLineAnnotation original = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, stroke, Color.BLUE);

        // Act: Create a clone of the original annotation.
        XYLineAnnotation clone = (XYLineAnnotation) original.clone();

        // Assert: Verify that the clone meets the general contract for Object.clone().
        // 1. The clone must be a different object instance.
        assertNotSame(original, clone, "The clone should be a new object instance.");

        // 2. The clone must be of the exact same class as the original.
        assertSame(original.getClass(), clone.getClass(), "The clone should be of the same class.");

        // 3. The clone must be equal in value to the original.
        assertEquals(original, clone, "The clone should be equal in value to the original.");
    }
}