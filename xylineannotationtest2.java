package org.jfree.chart.annotations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Tests for the constructor of the {@link XYLineAnnotation} class, focusing on
 * invalid argument handling.
 */
@DisplayName("XYLineAnnotation Constructor")
class XYLineAnnotationTest {

    private Stroke defaultStroke;
    private Paint defaultPaint;

    @BeforeEach
    void setUp() {
        defaultStroke = new BasicStroke(2.0f);
        defaultPaint = Color.BLUE;
    }

    /**
     * Provides arguments for testing non-finite coordinates. Each argument set
     * includes the coordinates and a description of the test case.
     */
    static Stream<Arguments> nonFiniteCoordinateProvider() {
        return Stream.of(
            arguments(Double.NaN, 20.0, 100.0, 200.0, "x1 is NaN"),
            arguments(10.0, Double.NaN, 100.0, 200.0, "y1 is NaN"),
            arguments(10.0, 20.0, Double.NaN, 200.0, "x2 is NaN"),
            arguments(10.0, 20.0, 100.0, Double.NaN, "y2 is NaN")
        );
    }

    @DisplayName("should throw IllegalArgumentException for non-finite coordinates")
    @ParameterizedTest(name = "when {4}")
    @MethodSource("nonFiniteCoordinateProvider")
    void constructor_shouldThrowException_forNonFiniteCoordinates(double x1, double y1, double x2, double y2, String description) {
        assertThrows(IllegalArgumentException.class, () ->
            new XYLineAnnotation(x1, y1, x2, y2, defaultStroke, defaultPaint)
        );
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for a null stroke")
    void constructor_shouldThrowException_forNullStroke() {
        assertThrows(IllegalArgumentException.class, () ->
            new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, null, defaultPaint)
        );
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for a null paint")
    void constructor_shouldThrowException_forNullPaint() {
        assertThrows(IllegalArgumentException.class, () ->
            new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, defaultStroke, null)
        );
    }
}