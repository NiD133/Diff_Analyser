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
 * -----------------------------
 * XYDrawableAnnotationTest.java
 * -----------------------------
 * (C) Copyright 2003-present, by David Gilbert.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link XYDrawableAnnotation} class.
 */
@DisplayName("XYDrawableAnnotation")
class XYDrawableAnnotationTest {

    /**
     * A mock Drawable for testing purposes. It includes an 'id' to allow for
     * creating instances that are not equal to each other, which is crucial
     * for properly testing the equals() method of XYDrawableAnnotation.
     */
    static class TestDrawable implements Drawable, Cloneable, Serializable {
        private final int id;

        public TestDrawable() {
            this(1);
        }

        public TestDrawable(int id) {
            this.id = id;
        }

        @Override
        public void draw(Graphics2D g2, Rectangle2D area) {
            // No-op for testing
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestDrawable that = (TestDrawable) obj;
            return this.id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    @Test
    @DisplayName("should be serializable")
    void testSerialization() {
        // Arrange
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, new TestDrawable(1));

        // Act
        XYDrawableAnnotation annotation2 = TestUtils.serialised(annotation1);

        // Assert
        assertEquals(annotation1, annotation2);
    }

    @Test
    @DisplayName("should implement PublicCloneable")
    void testPublicCloneable() {
        // Arrange
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, new TestDrawable());

        // Assert
        assertTrue(annotation instanceof PublicCloneable);
    }

    @Test
    @DisplayName("clone() should produce an independent, equal object")
    void testCloning() throws CloneNotSupportedException {
        // Arrange
        XYDrawableAnnotation original = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, new TestDrawable());

        // Act
        XYDrawableAnnotation clone = (XYDrawableAnnotation) original.clone();

        // Assert
        assertNotSame(original, clone, "Clone should be a different instance.");
        assertEquals(original, clone, "Clone should be equal to the original.");
    }

    @Nested
    @DisplayName("equals() and hashCode() contract")
    class EqualsAndHashCodeContract {

        private XYDrawableAnnotation baseAnnotation;
        private final TestDrawable baseDrawable = new TestDrawable(1);

        @BeforeEach
        void setUp() {
            baseAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, 1.0, baseDrawable);
        }

        @Test
        @DisplayName("should be equal to an identical instance")
        void shouldBeEqualToIdenticalInstance() {
            // Arrange
            XYDrawableAnnotation identicalAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, 1.0, baseDrawable);
            // Assert
            assertEquals(baseAnnotation, identicalAnnotation);
        }

        @Test
        @DisplayName("should not be equal to an instance with a different x-coordinate")
        void shouldNotBeEqualWhenXDiffers() {
            // Arrange
            XYDrawableAnnotation modifiedAnnotation = new XYDrawableAnnotation(11.0, 20.0, 100.0, 200.0, 1.0, baseDrawable);
            // Assert
            assertNotEquals(baseAnnotation, modifiedAnnotation);
        }

        @Test
        @DisplayName("should not be equal to an instance with a different y-coordinate")
        void shouldNotBeEqualWhenYDiffers() {
            // Arrange
            XYDrawableAnnotation modifiedAnnotation = new XYDrawableAnnotation(10.0, 22.0, 100.0, 200.0, 1.0, baseDrawable);
            // Assert
            assertNotEquals(baseAnnotation, modifiedAnnotation);
        }

        @Test
        @DisplayName("should not be equal to an instance with a different display width")
        void shouldNotBeEqualWhenWidthDiffers() {
            // Arrange
            XYDrawableAnnotation modifiedAnnotation = new XYDrawableAnnotation(10.0, 20.0, 101.0, 200.0, 1.0, baseDrawable);
            // Assert
            assertNotEquals(baseAnnotation, modifiedAnnotation);
        }

        @Test
        @DisplayName("should not be equal to an instance with a different display height")
        void shouldNotBeEqualWhenHeightDiffers() {
            // Arrange
            XYDrawableAnnotation modifiedAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 202.0, 1.0, baseDrawable);
            // Assert
            assertNotEquals(baseAnnotation, modifiedAnnotation);
        }

        @Test
        @DisplayName("should not be equal to an instance with a different draw scale factor")
        void shouldNotBeEqualWhenScaleFactorDiffers() {
            // Arrange
            XYDrawableAnnotation modifiedAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, 2.0, baseDrawable);
            // Assert
            assertNotEquals(baseAnnotation, modifiedAnnotation);
        }

        @Test
        @DisplayName("should not be equal to an instance with a different drawable")
        void shouldNotBeEqualWhenDrawableDiffers() {
            // Arrange
            TestDrawable differentDrawable = new TestDrawable(2);
            XYDrawableAnnotation modifiedAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, 1.0, differentDrawable);
            // Assert
            assertNotEquals(baseAnnotation, modifiedAnnotation);
        }

        @Test
        @DisplayName("hashCode() should be consistent for equal instances")
        void hashCodeShouldBeConsistentForEqualInstances() {
            // Arrange
            XYDrawableAnnotation identicalAnnotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 200.0, 1.0, baseDrawable);
            // Assert
            assertEquals(baseAnnotation.hashCode(), identicalAnnotation.hashCode());
        }
    }
}