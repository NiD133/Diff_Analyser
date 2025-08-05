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
 * StandardXYBarPainterTest.java
 * -----------------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer.xy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StandardXYBarPainter} class, focusing on its
 * fundamental object methods like equals(), hashCode(), and serialization.
 */
@DisplayName("A StandardXYBarPainter")
class StandardXYBarPainterTest {

    private StandardXYBarPainter painter;

    @BeforeEach
    void setUp() {
        this.painter = new StandardXYBarPainter();
    }

    @Test
    @DisplayName("should be equal to another instance")
    void equals_withAnotherInstance_shouldBeTrue() {
        // Arrange
        StandardXYBarPainter anotherPainter = new StandardXYBarPainter();
        
        // Assert
        // Since the class is stateless, any two instances should be equal.
        assertEquals(painter, anotherPainter);
        assertEquals(anotherPainter, painter); // Check for symmetry
    }

    @Test
    @DisplayName("should not be equal to null")
    void equals_withNull_shouldBeFalse() {
        assertFalse(painter.equals(null));
    }

    @Test
    @DisplayName("should not be equal to an object of a different type")
    void equals_withDifferentType_shouldBeFalse() {
        assertFalse(painter.equals("A String Object"));
    }

    @Test
    @DisplayName("should have a hash code that is consistent with equals")
    void hashCode_shouldBeConsistentForEqualObjects() {
        // Arrange
        StandardXYBarPainter anotherPainter = new StandardXYBarPainter();
        
        // Assert
        // The hashCode() contract requires that equal objects have equal hash codes.
        assertEquals(painter, anotherPainter, "Precondition: painters must be equal.");
        assertEquals(painter.hashCode(), anotherPainter.hashCode());
    }

    @Test
    @DisplayName("should not support cloning")
    void cloning_shouldNotBeSupported() {
        // This class is immutable, so cloning is unnecessary and not implemented.
        assertFalse(painter instanceof Cloneable);
        assertFalse(painter instanceof PublicCloneable);
    }

    @Test
    @DisplayName("should be serializable")
    void serialization_shouldPreserveEquality() {
        // Act
        StandardXYBarPainter deserializedPainter = TestUtils.serialised(this.painter);
        
        // Assert
        assertEquals(this.painter, deserializedPainter);
    }
}