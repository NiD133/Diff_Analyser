package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Provides tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /** 
     * A small tolerance for comparing double values to handle potential floating-point inaccuracies. 
     */
    private static final double DELTA = 1e-9;

    /**
     * Verifies that calling hashCode() on an XYLineAnnotation instance
     * does not throw an exception and does not alter the object's state.
     *
     * This test's structure is derived from the original auto-generated test,
     * which called hashCode() for coverage and then verified the object's state.
     * This refactored version clarifies that intent by ensuring the hashCode()
     * call is safe and has no side effects on the object's properties.
     */
    @Test
    public void hashCode_shouldNotThrowExceptionOrAlterState() {
        // Arrange: Define coordinates and create an annotation instance.
        final double x1 = -3139.810815486518;
        final double y1 = -3139.810815486518;
        final double x2 = -3139.810815486518;
        final double y2 = -605.8001;
        XYLineAnnotation annotation = new XYLineAnnotation(x1, y1, x2, y2);

        // Act: Call the hashCode() method. The test will fail if this throws an exception.
        annotation.hashCode();

        // Assert: Verify that the object's state remains unchanged after calling hashCode().
        assertEquals("The x1 coordinate should remain unchanged after hashCode() call.", x1, annotation.getX1(), DELTA);
        assertEquals("The y1 coordinate should remain unchanged after hashCode() call.", y1, annotation.getY1(), DELTA);
        assertEquals("The x2 coordinate should remain unchanged after hashCode() call.", x2, annotation.getX2(), DELTA);
        assertEquals("The y2 coordinate should remain unchanged after hashCode() call.", y2, annotation.getY2(), DELTA);
    }
}