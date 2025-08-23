package org.jfree.chart.annotations;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * A test suite for the {@link XYLineAnnotation} class.
 */
@DisplayName("XYLineAnnotation")
public class XYLineAnnotationTest {

    /**
     * Verifies that XYLineAnnotation instances are cloneable through the
     * PublicCloneable interface.
     */
    @Test
    @DisplayName("should implement PublicCloneable")
    void isPublicCloneable() {
        // Arrange: Create a standard XYLineAnnotation instance.
        // The specific constructor arguments are arbitrary valid values.
        XYLineAnnotation annotation = new XYLineAnnotation(
                10.0, 20.0, 100.0, 200.0,
                new BasicStroke(2.0f), Color.BLUE
        );

        // Assert: Check if the instance conforms to the PublicCloneable interface.
        assertInstanceOf(PublicCloneable.class, annotation,
                "XYLineAnnotation must implement the PublicCloneable interface.");
    }
}