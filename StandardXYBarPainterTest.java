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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Tests the {@code equals} method for the {@link StandardXYBarPainter} class.
     * Verifies the contract of the equals method including reflexivity, symmetry,
     * transitivity, consistency, and handling of null and different types.
     */
    @Test
    public void testEquals() {
        StandardXYBarPainter p1 = new StandardXYBarPainter();
        StandardXYBarPainter p2 = new StandardXYBarPainter();
        StandardXYBarPainter p3 = new StandardXYBarPainter();

        // Reflexivity: an object must be equal to itself
        assertTrue(p1.equals(p1));

        // Symmetry: if p1 equals p2, then p2 must equal p1
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));

        // Transitivity: if p1 equals p2 and p2 equals p3, then p1 must equal p3
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p3));
        assertTrue(p1.equals(p3));

        // Consistency: multiple invocations return the same result
        assertTrue(p1.equals(p2));

        // Null handling: an instance should not be equal to null
        assertFalse(p1.equals(null));

        // Different type: an instance should not be equal to an object of a different type
        assertFalse(p1.equals(new Object()));
    }

    /**
     * Tests the {@code hashCode} method for the {@link StandardXYBarPainter} class.
     * Verifies that equal objects produce the same hash code.
     */
    @Test
    public void testHashCode() {
        StandardXYBarPainter p1 = new StandardXYBarPainter();
        StandardXYBarPainter p2 = new StandardXYBarPainter();

        // Equal objects must have the same hash code
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    /**
     * Tests that the {@link StandardXYBarPainter} class does not support cloning.
     * Since the class is immutable, cloning is not required.
     */
    @Test
    public void testCloning() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        
        // Verify the painter does not implement Cloneable or PublicCloneable
        assertFalse(painter instanceof Cloneable, 
                "StandardXYBarPainter should not implement Cloneable");
        assertFalse(painter instanceof PublicCloneable, 
                "StandardXYBarPainter should not implement PublicCloneable");
    }

    /**
     * Tests serialization and deserialization of {@link StandardXYBarPainter}.
     * Verifies that the deserialized instance is equal to the original.
     */
    @Test
    public void testSerialization() {
        StandardXYBarPainter p1 = new StandardXYBarPainter();
        
        // Serialize and deserialize the painter
        StandardXYBarPainter p2 = TestUtils.serialised(p1);
        
        // Verify the deserialized object is equal to the original
        assertEquals(p1, p2, "Deserialized instance should equal the original");
    }

}