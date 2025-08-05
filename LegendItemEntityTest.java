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
 * LegendItemEntityTest.java
 * -------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    /**
     * Helper method to create a baseline entity with predefined values for testing.
     *
     * @return A {@code LegendItemEntity} configured with baseline attributes.
     */
    private LegendItemEntity<String> createBaselineEntity() {
        LegendItemEntity<String> entity = new LegendItemEntity<>(
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        entity.setToolTipText("ToolTip");
        entity.setURLText("URL");
        entity.setDataset(new DefaultCategoryDataset<String, String>());
        entity.setSeriesKey("Key");
        return entity;
    }

    /**
     * Tests that the same object instance is equal to itself.
     */
    @Test
    public void testEqualsWithSameObject() {
        LegendItemEntity<String> entity = createBaselineEntity();
        assertEquals(entity, entity, "Object should be equal to itself");
    }

    /**
     * Tests that two distinct objects with identical attributes are equal.
     */
    @Test
    public void testEqualsWithEqualObjects() {
        LegendItemEntity<String> e1 = createBaselineEntity();
        LegendItemEntity<String> e2 = createBaselineEntity();
        assertEquals(e1, e2, "Objects with identical attributes should be equal");
    }

    /**
     * Tests that objects with different areas are not equal.
     */
    @Test
    public void testEqualsWithDifferentArea() {
        LegendItemEntity<String> e1 = createBaselineEntity();
        LegendItemEntity<String> e2 = createBaselineEntity();
        e2.setArea(new Rectangle2D.Double(99.0, 99.0, 99.0, 99.0));
        assertNotEquals(e1, e2, "Objects with different areas should not be equal");
    }

    /**
     * Tests that objects with different tooltips are not equal.
     */
    @Test
    public void testEqualsWithDifferentToolTip() {
        LegendItemEntity<String> e1 = createBaselineEntity();
        LegendItemEntity<String> e2 = createBaselineEntity();
        e2.setToolTipText("DifferentToolTip");
        assertNotEquals(e1, e2, "Objects with different tooltips should not be equal");
    }

    /**
     * Tests that objects with different URLs are not equal.
     */
    @Test
    public void testEqualsWithDifferentURL() {
        LegendItemEntity<String> e1 = createBaselineEntity();
        LegendItemEntity<String> e2 = createBaselineEntity();
        e2.setURLText("DifferentURL");
        assertNotEquals(e1, e2, "Objects with different URLs should not be equal");
    }

    /**
     * Tests that objects with different datasets are not equal.
     */
    @Test
    public void testEqualsWithDifferentDataset() {
        LegendItemEntity<String> e1 = createBaselineEntity();
        LegendItemEntity<String> e2 = createBaselineEntity();
        e2.setDataset(new DefaultCategoryDataset<String, String>());
        assertNotEquals(e1, e2, "Objects with different datasets should not be equal");
    }

    /**
     * Tests that objects with different series keys are not equal.
     */
    @Test
    public void testEqualsWithDifferentSeriesKey() {
        LegendItemEntity<String> e1 = createBaselineEntity();
        LegendItemEntity<String> e2 = createBaselineEntity();
        e2.setSeriesKey("DifferentKey");
        assertNotEquals(e1, e2, "Objects with different series keys should not be equal");
    }

    /**
     * Tests that cloning creates a distinct object with identical attributes.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        LegendItemEntity<String> e1 = createBaselineEntity();
        LegendItemEntity<String> e2 = CloneUtils.clone(e1);

        // Verify distinct instances
        assertNotSame(e1, e2, "Cloned object should be a distinct instance");
        assertSame(e1.getClass(), e2.getClass(), "Cloned object should have the same class");
        assertEquals(e1, e2, "Cloned object should be equal to the original");
    }

    /**
     * Tests that serialization and deserialization produce equal objects.
     */
    @Test
    public void testSerialization() {
        LegendItemEntity<String> e1 = createBaselineEntity();
        LegendItemEntity<String> e2 = TestUtils.serialised(e1);
        assertEquals(e1, e2, "Deserialized object should equal the original");
    }
}