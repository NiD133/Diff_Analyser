/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * -------------------------
 * XYLineAnnotationTest.java
 * -------------------------
 * (C) Copyright 2003-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.annotations;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    private static final double EPSILON = 0.000000001;
    private static final Stroke DEFAULT_STROKE = new BasicStroke(2.0f);
    private static final Paint DEFAULT_PAINT = Color.BLUE;

    @Test
    public void constructorShouldSetAllProperties() {
        // Arrange
        double x1 = 10.0, y1 = 20.0, x2 = 100.0, y2 = 200.0;

        // Act
        XYLineAnnotation annotation = new XYLineAnnotation(x1, y1, x2, y2, DEFAULT_STROKE, DEFAULT_PAINT);

        // Assert
        assertEquals(x1, annotation.getX1(), EPSILON);
        assertEquals(y1, annotation.getY1(), EPSILON);
        assertEquals(x2, annotation.getX2(), EPSILON);
        assertEquals(y2, annotation.getY2(), EPSILON);
        assertEquals(DEFAULT_STROKE, annotation.getStroke());
        assertEquals(DEFAULT_PAINT, annotation.getPaint());
    }

    @DisplayName("Constructor should throw IllegalArgumentException for invalid arguments")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("invalidConstructorArgumentProvider")
    public void constructorShouldThrowExceptionForInvalidArguments(String description,
            Supplier<XYLineAnnotation> constructorCall) {
        assertThrows(IllegalArgumentException.class, constructorCall::get);
    }

    private static Stream<Arguments> invalidConstructorArgumentProvider() {
        return Stream.of(
            Arguments.of("NaN x1", (Supplier<XYLineAnnotation>) () -> new XYLineAnnotation(Double.NaN, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT)),
            Arguments.of("NaN y1", (Supplier<XYLineAnnotation>) () -> new XYLineAnnotation(10.0, Double.NaN, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT)),
            Arguments.of("NaN x2", (Supplier<XYLineAnnotation>) () -> new XYLineAnnotation(10.0, 20.0, Double.NaN, 200.0, DEFAULT_STROKE, DEFAULT_PAINT)),
            Arguments.of("NaN y2", (Supplier<XYLineAnnotation>) () -> new XYLineAnnotation(10.0, 20.0, 100.0, Double.NaN, DEFAULT_STROKE, DEFAULT_PAINT)),
            Arguments.of("null stroke", (Supplier<XYLineAnnotation>) () -> new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, null, DEFAULT_PAINT)),
            Arguments.of("null paint", (Supplier<XYLineAnnotation>) () -> new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, null))
        );
    }

    @Test
    public void equalsShouldBehaveCorrectly() {
        // Arrange
        final XYLineAnnotation baseAnnotation = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);

        // Assert - self-equality and null/different type checks
        assertEquals(baseAnnotation, baseAnnotation);
        assertNotEquals(baseAnnotation, null);
        assertNotEquals(baseAnnotation, new Object());

        // Assert - equality with an identical instance
        final XYLineAnnotation identicalAnnotation = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);
        assertEquals(baseAnnotation, identicalAnnotation);

        // Assert - inequality for each differing property
        final XYLineAnnotation differentX1 = new XYLineAnnotation(11.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);
        assertNotEquals(baseAnnotation, differentX1);

        final XYLineAnnotation differentY1 = new XYLineAnnotation(10.0, 21.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);
        assertNotEquals(baseAnnotation, differentY1);

        final XYLineAnnotation differentX2 = new XYLineAnnotation(10.0, 20.0, 101.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);
        assertNotEquals(baseAnnotation, differentX2);

        final XYLineAnnotation differentY2 = new XYLineAnnotation(10.0, 20.0, 100.0, 201.0, DEFAULT_STROKE, DEFAULT_PAINT);
        assertNotEquals(baseAnnotation, differentY2);

        final XYLineAnnotation differentStroke = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, new BasicStroke(3.0f), DEFAULT_PAINT);
        assertNotEquals(baseAnnotation, differentStroke);

        final XYLineAnnotation differentPaint = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, Color.RED);
        assertNotEquals(baseAnnotation, differentPaint);
    }

    @Test
    public void hashCodeShouldBeConsistentWithEquals() {
        // Arrange
        XYLineAnnotation annotation1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);

        // Act & Assert
        assertEquals(annotation1, annotation2);
        assertEquals(annotation1.hashCode(), annotation2.hashCode());
    }

    @Test
    public void cloneShouldCreateIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        XYLineAnnotation original = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);

        // Act
        XYLineAnnotation clone = (XYLineAnnotation) original.clone();

        // Assert
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }

    @Test
    public void shouldImplementPublicCloneable() {
        // Arrange
        XYLineAnnotation annotation = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);

        // Act & Assert
        assertTrue(annotation instanceof PublicCloneable);
    }

    @Test
    public void shouldBeSerializable() {
        // Arrange
        XYLineAnnotation original = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, DEFAULT_STROKE, DEFAULT_PAINT);

        // Act
        XYLineAnnotation deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized);
    }
}