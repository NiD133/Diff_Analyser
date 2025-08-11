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
 * ---------------------------
 * StandardBarPainterTest.java
 * ---------------------------
 * (C) Copyright 2008-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.renderer.category;

import static org.junit.jupiter.api.Assertions.*;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    @Test
    public void testEquals() {
        StandardBarPainter p1 = new StandardBarPainter();
        StandardBarPainter p2 = new StandardBarPainter();

        // Reflexivity check
        assertEquals(p1, p1, "Object should be equal to itself");

        // Symmetry and equality check
        assertEquals(p1, p2, "Two new instances should be equal");
        assertEquals(p2, p1, "Equality should be symmetric");

        // Null check
        assertNotEquals(p1, null, "Object should not equal null");

        // Type check
        assertNotEquals(p1, new Object(), "Object should not equal different type");
    }

    @Test
    public void testHashCode() {
        StandardBarPainter p1 = new StandardBarPainter();
        StandardBarPainter p2 = new StandardBarPainter();
        
        // Verify equal objects have same hash code
        assertEquals(p1.hashCode(), p2.hashCode(), 
            "Equal objects must have identical hash codes");
    }

    @Test
    public void testCloning() {
        StandardBarPainter p = new StandardBarPainter();
        
        // Confirm class doesn't support cloning
        assertFalse(p instanceof Cloneable, 
            "Should not implement Cloneable due to immutability");
        assertFalse(p instanceof PublicCloneable, 
            "Should not implement PublicCloneable");
    }

    @Test
    public void testSerialization() {
        StandardBarPainter p1 = new StandardBarPainter();
        StandardBarPainter p2 = TestUtils.serialised(p1);
        
        // Verify post-serialization behavior
        assertEquals(p1, p2, "Deserialized object should equal original");
        assertNotSame(p1, p2, 
            "Deserialized object should be a different instance");
    }

}