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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    // Test data constants
    private static final String INITIAL_TEXT = "ABC";
    private static final String MODIFIED_TEXT = "Test 1";

    /**
     * Verifies that the equals() method correctly compares ShortTextTitle objects
     * based on their text content.
     */
    @Test
    public void testEquals() {
        // Given: Two ShortTextTitle objects with identical text
        ShortTextTitle firstTitle = new ShortTextTitle(INITIAL_TEXT);
        ShortTextTitle secondTitle = new ShortTextTitle(INITIAL_TEXT);
        
        // Then: They should be equal
        assertEquals(firstTitle, secondTitle, 
            "Two ShortTextTitle objects with same text should be equal");

        // When: First title's text is changed
        firstTitle.setText(MODIFIED_TEXT);
        
        // Then: They should no longer be equal
        assertNotEquals(firstTitle, secondTitle, 
            "ShortTextTitle objects with different text should not be equal");
        
        // When: Second title's text is updated to match the first
        secondTitle.setText(MODIFIED_TEXT);
        
        // Then: They should be equal again
        assertEquals(firstTitle, secondTitle, 
            "ShortTextTitle objects with same modified text should be equal");
    }

    /**
     * Verifies that equal ShortTextTitle objects produce the same hash code,
     * which is required for proper behavior in hash-based collections.
     */
    @Test
    public void testHashCode() {
        // Given: Two ShortTextTitle objects with identical text
        ShortTextTitle firstTitle = new ShortTextTitle(INITIAL_TEXT);
        ShortTextTitle secondTitle = new ShortTextTitle(INITIAL_TEXT);
        
        // When: Objects are equal
        assertEquals(firstTitle, secondTitle, "Titles should be equal for hash code test");
        
        // Then: Their hash codes must be identical
        int firstHashCode = firstTitle.hashCode();
        int secondHashCode = secondTitle.hashCode();
        assertEquals(firstHashCode, secondHashCode, 
            "Equal ShortTextTitle objects must have identical hash codes");
    }

    /**
     * Verifies that ShortTextTitle objects can be properly cloned,
     * creating independent copies with the same content.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Given: A ShortTextTitle object
        ShortTextTitle originalTitle = new ShortTextTitle(INITIAL_TEXT);
        
        // When: The object is cloned
        ShortTextTitle clonedTitle = CloneUtils.clone(originalTitle);
        
        // Then: The clone should be a separate instance
        assertNotSame(originalTitle, clonedTitle, 
            "Cloned object should be a different instance");
        
        // And: Should have the same class
        assertSame(originalTitle.getClass(), clonedTitle.getClass(), 
            "Cloned object should have the same class as original");
        
        // And: Should be equal in content
        assertEquals(originalTitle, clonedTitle, 
            "Cloned object should be equal to original in content");
    }

    /**
     * Verifies that ShortTextTitle objects can be serialized and deserialized
     * while maintaining their equality and state.
     */
    @Test
    public void testSerialization() {
        // Given: A ShortTextTitle object
        ShortTextTitle originalTitle = new ShortTextTitle(INITIAL_TEXT);
        
        // When: The object is serialized and then deserialized
        ShortTextTitle deserializedTitle = TestUtils.serialised(originalTitle);
        
        // Then: The deserialized object should equal the original
        assertEquals(originalTitle, deserializedTitle, 
            "Deserialized ShortTextTitle should equal the original");
    }
}