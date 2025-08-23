package org.jfree.chart.annotations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * A test suite for the equals() method of the {@link XYLineAnnotation} class.
 * This suite verifies the correctness of the equality contract.
 */
@DisplayName("XYLineAnnotation equals() contract")
class XYLineAnnotationEqualsTest {

    private static final Stroke DEFAULT_STROKE = new BasicStroke(2.0f);
    private static final Paint DEFAULT_PAINT = Color.BLUE;

    private static XYLineAnnotation createDefaultAnnotation() {
        return new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);
    }

    @Test
    @DisplayName("should return true for the same instance")
    void equals_shouldReturnTrue_forSameInstance() {
        // Arrange
        XYLineAnnotation annotation = createDefaultAnnotation();

        // Act & Assert
        assertEquals(annotation, annotation, "An object must be equal to itself (reflexivity).");
    }

    @Test
    @DisplayName("should return true for two separate but identical instances")
    void equals_shouldReturnTrue_forIdenticalInstances() {
        // Arrange
        XYLineAnnotation annotation1 = createDefaultAnnotation();
        XYLineAnnotation annotation2 = createDefaultAnnotation();

        // Act & Assert
        assertEquals(annotation1, annotation2, "Two identical but separate instances should be equal.");
        assertEquals(annotation2, annotation1, "Equality should be symmetric.");
    }

    @Test
    @DisplayName("should return true for instances with equal but distinct Paint objects")
    void equals_shouldReturnTrue_forEqualButDistinctPaintObjects() {
        // Arrange
        Paint paint1 = new GradientPaint(1f, 2f, Color.RED, 3f, 4f, Color.GREEN);
        Paint paint2 = new GradientPaint(1f, 2f, Color.RED, 3f, 4f, Color.GREEN);

        XYLineAnnotation annotation1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, paint1);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, paint2);

        // Act & Assert
        assertEquals(annotation1, annotation2, "Annotations with equal but distinct Paint objects should be equal.");
    }

    @Test
    @DisplayName("should return false when compared to null")
    void equals_shouldReturnFalse_forNull() {
        // Arrange
        XYLineAnnotation annotation = createDefaultAnnotation();

        // Act & Assert
        assertNotEquals(null, annotation, "An object should not be equal to null.");
    }

    @Test
    @DisplayName("should return false when compared to an object of a different type")
    void equals_shouldReturnFalse_forDifferentType() {
        // Arrange
        XYLineAnnotation annotation = createDefaultAnnotation();
        Object otherObject = new Object();

        // Act & Assert
        assertNotEquals(annotation, otherObject, "An object should not be equal to an object of a different type.");
    }

    private static Stream<Arguments> inequalityTestCases() {
        XYLineAnnotation base = createDefaultAnnotation();
        return Stream.of(
            Arguments.of("different x1", new XYLineAnnotation(99.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT)),
            Arguments.of("different y1", new XYLineAnnotation(10.0, 99.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT)),
            Arguments.of("different x2", new XYLineAnnotation(10.0, 20.0, 999.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT)),
            Arguments.of("different y2", new XYLineAnnotation(10.0, 20.0, 100.0, 999.0, DEFAULT_STROKE, DEFAULT_PAINT)),
            Arguments.of("different stroke", new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, new BasicStroke(5.0f), DEFAULT_PAINT)),
            Arguments.of("different paint", new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, Color.RED))
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("inequalityTestCases")
    @DisplayName("should return false if any property is different")
    void equals_shouldReturnFalse_whenAnyPropertyIsDifferent(String description, XYLineAnnotation modifiedAnnotation) {
        // Arrange
        XYLineAnnotation baseAnnotation = createDefaultAnnotation();

        // Act & Assert
        assertNotEquals(baseAnnotation, modifiedAnnotation);
        assertNotEquals(modifiedAnnotation, baseAnnotation, "Equality should be symmetric.");
    }
}