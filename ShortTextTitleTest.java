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
 * -----------------------
 * ShortTextTitleTest.java
 * -----------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.title;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ShortTextTitle} class.
 * <p>
 * This test suite focuses on the basic object contract methods like
 * {@code equals()}, {@code hashCode()}, cloning, and serialization.
 * The core rendering logic in {@code arrange()} and {@code draw()} is not
 * covered here.
 */
@DisplayName("ShortTextTitle")
class ShortTextTitleTest {

    /**
     * Verifies that the equals() method correctly compares instances based on
     * their text content.
     */
    @Test
    @DisplayName("equals() should distinguish instances based on their text")
    void equals_shouldDistinguishInstancesBasedOnText() {
        // Arrange: Create two titles with the same initial text
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");

        // Assert: Initially, they should be equal
        assertEquals(title1, title2, "Titles with the same initial text should be equal.");

        // Act: Change the text of the first title
        title1.setText("XYZ");

        // Assert: Now they should not be equal
        assertNotEquals(title1, title2, "Titles should be unequal after changing one's text.");

        // Act: Change the text of the second title to match the first
        title2.setText("XYZ");

        // Assert: They should be equal again
        assertEquals(title1, title2, "Titles should be equal again after their texts are made the same.");
    }

    /**
     * Verifies that two equal objects produce the same hash code, fulfilling the
     * hashCode() contract.
     */
    @Test
    @DisplayName("hashCode() should be consistent with equals()")
    void hashCode_shouldBeConsistentWithEquals() {
        // Arrange
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");

        // Assert
        assertEquals(title1, title2, "Precondition: Objects must be equal for this test.");
        assertEquals(title1.hashCode(), title2.hashCode(), "Equal objects must have the same hash code.");
    }

    /**
     * Verifies that cloning produces an independent object that is equal to the
     * original.
     */
    @Test
    @DisplayName("clone() should create an independent and equal copy")
    void cloning_shouldCreateIndependentAndEqualCopy() throws CloneNotSupportedException {
        // Arrange
        ShortTextTitle original = new ShortTextTitle("ABC");

        // Act
        ShortTextTitle clone = CloneUtils.clone(original);

        // Assert
        assertNotSame(original, clone, "Cloned object should be a different instance.");
        assertSame(original.getClass(), clone.getClass(), "Cloned object should be of the same class.");
        assertEquals(original, clone, "Cloned object should be equal to the original.");
    }

    /**
     * Verifies that a serialized and then deserialized instance remains equal
     * to the original object.
     */
    @Test
    @DisplayName("A serialized and deserialized instance should remain equal to the original")
    void serialization_shouldPreserveEquality() {
        // Arrange
        ShortTextTitle original = new ShortTextTitle("ABC");

        // Act
        ShortTextTitle deserialized = TestUtils.serialised(original);

        // Assert
        assertEquals(original, deserialized, "Deserialized object should be equal to the original.");
    }
}