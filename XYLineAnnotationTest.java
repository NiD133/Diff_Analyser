package org.jfree.chart.annotations;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.List;
import java.util.stream.Stream;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the XYLineAnnotation class.
 *
 * Goals:
 * - Make intent explicit via descriptive test names.
 * - Reduce duplication with helpers and parameterized tests.
 * - Follow Arrange/Act/Assert (AAA) structure for readability.
 */
public class XYLineAnnotationTest {

    private static final double EPSILON = 1e-9;

    // Default test data used across tests
    private static final double X1 = 10.0;
    private static final double Y1 = 20.0;
    private static final double X2 = 100.0;
    private static final double Y2 = 200.0;
    private static final Stroke DEFAULT_STROKE = new BasicStroke(2.0f);
    private static final Paint DEFAULT_PAINT = Color.BLUE;

    private static XYLineAnnotation newAnnotation() {
        return new XYLineAnnotation(X1, Y1, X2, Y2, DEFAULT_STROKE, DEFAULT_PAINT);
    }

    private static XYLineAnnotation newAnnotation(double x1, double y1, double x2, double y2,
                                                  Stroke stroke, Paint paint) {
        return new XYLineAnnotation(x1, y1, x2, y2, stroke, paint);
    }

    @Test
    @DisplayName("Constructor sets all fields as provided")
    public void constructor_setsAllFields() {
        // Arrange
        Stroke stroke = DEFAULT_STROKE;
        Paint paint = DEFAULT_PAINT;

        // Act
        XYLineAnnotation a = new XYLineAnnotation(X1, Y1, X2, Y2, stroke, paint);

        // Assert
        assertAll(
            () -> assertEquals(X1, a.getX1(), EPSILON, "x1"),
            () -> assertEquals(Y1, a.getY1(), EPSILON, "y1"),
            () -> assertEquals(X2, a.getX2(), EPSILON, "x2"),
            () -> assertEquals(Y2, a.getY2(), EPSILON, "y2"),
            () -> assertEquals(stroke, a.getStroke(), "stroke"),
            () -> assertEquals(paint, a.getPaint(), "paint")
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("invalidConstructorArguments")
    @DisplayName("Constructor rejects invalid arguments")
    public void constructor_withInvalidArgs_throwsIllegalArgumentException(String caseName,
                                                                          Executable constructorCall) {
        assertThrows(IllegalArgumentException.class, constructorCall, caseName);
    }

    private static Stream<Arguments> invalidConstructorArguments() {
        Stroke stroke = DEFAULT_STROKE;
        Paint paint = DEFAULT_PAINT;

        return Stream.of(
            Arguments.of("x1 is NaN",
                (Executable) () -> new XYLineAnnotation(Double.NaN, Y1, X2, Y2, stroke, paint)),
            Arguments.of("y1 is NaN",
                (Executable) () -> new XYLineAnnotation(X1, Double.NaN, X2, Y2, stroke, paint)),
            Arguments.of("x2 is NaN",
                (Executable) () -> new XYLineAnnotation(X1, Y1, Double.NaN, Y2, stroke, paint)),
            Arguments.of("y2 is NaN",
                (Executable) () -> new XYLineAnnotation(X1, Y1, X2, Double.NaN, stroke, paint)),
            Arguments.of("stroke is null",
                (Executable) () -> new XYLineAnnotation(X1, Y1, X2, Y2, null, paint)),
            Arguments.of("paint is null",
                (Executable) () -> new XYLineAnnotation(X1, Y1, X2, Y2, stroke, null))
        );
    }

    @Test
    @DisplayName("equals() distinguishes all fields and is symmetric")
    public void equals_shouldDifferentiateAllFields() {
        // Arrange
        XYLineAnnotation base = newAnnotation();
        XYLineAnnotation equalCopy = newAnnotation();

        // Act & Assert: basic equality and symmetry
        assertEquals(base, equalCopy);
        assertEquals(equalCopy, base);

        // Variants differing by a single field
        Stroke otherStroke = new BasicStroke(0.99f);
        GradientPaint gradient1 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE);
        GradientPaint gradient2 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.WHITE); // equal to gradient1

        List<XYLineAnnotation> differentVariants = List.of(
            newAnnotation(X1 + 1.0, Y1, X2, Y2, DEFAULT_STROKE, DEFAULT_PAINT),
            newAnnotation(X1, Y1 + 1.0, X2, Y2, DEFAULT_STROKE, DEFAULT_PAINT),
            newAnnotation(X1, Y1, X2 + 1.0, Y2, DEFAULT_STROKE, DEFAULT_PAINT),
            newAnnotation(X1, Y1, X2, Y2 + 1.0, DEFAULT_STROKE, DEFAULT_PAINT),
            newAnnotation(X1, Y1, X2, Y2, otherStroke, DEFAULT_PAINT),
            newAnnotation(X1, Y1, X2, Y2, DEFAULT_STROKE, gradient1)
        );

        differentVariants.forEach(variant ->
            assertNotEquals(base, variant, "Variant should not equal base when a field differs")
        );

        // Equality should hold when both sides have identical differing values
        XYLineAnnotation variantCopySameStroke =
            newAnnotation(X1, Y1, X2, Y2, otherStroke, DEFAULT_PAINT);
        assertEquals(differentVariants.get(4), variantCopySameStroke);

        XYLineAnnotation variantCopySamePaint =
            newAnnotation(X1, Y1, X2, Y2, DEFAULT_STROKE, gradient2);
        assertEquals(differentVariants.get(5), variantCopySamePaint);
    }

    @Test
    @DisplayName("hashCode() is consistent with equals()")
    public void hashCode_consistentWithEquals() {
        // Arrange
        XYLineAnnotation a1 = newAnnotation();
        XYLineAnnotation a2 = newAnnotation();

        // Act
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();

        // Assert
        assertEquals(a1, a2);
        assertEquals(h1, h2);
    }

    @Test
    @DisplayName("clone() produces an equal but distinct instance")
    public void clone_createsEqualButSeparateInstance() throws CloneNotSupportedException {
        // Arrange
        XYLineAnnotation original = newAnnotation();

        // Act
        XYLineAnnotation clone = (XYLineAnnotation) original.clone();

        // Assert
        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);
    }

    @Test
    @DisplayName("Class implements PublicCloneable")
    public void implementsPublicCloneable() {
        // Arrange
        XYLineAnnotation a = newAnnotation();

        // Assert
        assertTrue(a instanceof PublicCloneable);
    }

    @Test
    @DisplayName("Serialization round-trip preserves equality")
    public void serialization_roundTripYieldsEqualObject() {
        // Arrange
        XYLineAnnotation original = newAnnotation();

        // Act
        XYLineAnnotation restored = TestUtils.serialised(original);

        // Assert
        assertEquals(original, restored);
    }
}