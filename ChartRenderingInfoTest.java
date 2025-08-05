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
 * Free Software Foundation; either version 2.1 of the License, or
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
 * ChartRenderingInfoTest.java
 * ---------------------------
 * (C) Copyright 2004-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    // Define reusable test constants
    private static final Rectangle2D CHART_AREA = 
        new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
    private static final Rectangle2D PLOT_DATA_AREA = 
        new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
    private static final Rectangle ENTITY_BOUNDS = 
        new Rectangle(1, 2, 3, 4);
    private static final Rectangle2D MODIFIED_CHART_AREA = 
        new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0);

    // =====================================================================
    // Tests for equals() method
    // =====================================================================

    /**
     * Tests that two default ChartRenderingInfo objects are equal.
     */
    @Test
    public void testEqualsWithDefaultObjects() {
        ChartRenderingInfo i1 = new ChartRenderingInfo();
        ChartRenderingInfo i2 = new ChartRenderingInfo();
        assertEquals(i1, i2, "Default objects should be equal");
    }

    /**
     * Tests that chartArea is properly considered in equality checks.
     */
    @Test
    public void testEqualsAfterSettingChartArea() {
        ChartRenderingInfo i1 = new ChartRenderingInfo();
        ChartRenderingInfo i2 = new ChartRenderingInfo();

        i1.setChartArea(CHART_AREA);
        assertNotEquals(i1, i2, "Objects should differ after setting chartArea in one");

        i2.setChartArea(CHART_AREA);
        assertEquals(i1, i2, "Objects should be equal after setting same chartArea in both");
    }

    /**
     * Tests that plotInfo's dataArea is properly considered in equality checks.
     */
    @Test
    public void testEqualsAfterSettingPlotInfoDataArea() {
        ChartRenderingInfo i1 = new ChartRenderingInfo();
        ChartRenderingInfo i2 = new ChartRenderingInfo();
        
        // Start with equal objects
        i1.setChartArea(CHART_AREA);
        i2.setChartArea(CHART_AREA);
        assertEquals(i1, i2, "Objects should be equal initially");
        
        // Modify plot info in first object
        i1.getPlotInfo().setDataArea(PLOT_DATA_AREA);
        assertNotEquals(i1, i2, "Objects should differ after setting plot dataArea in one");
        
        // Set same plot info in second object
        i2.getPlotInfo().setDataArea(PLOT_DATA_AREA);
        assertEquals(i1, i2, "Objects should be equal after setting same plot dataArea in both");
    }

    /**
     * Tests that entityCollection is properly considered in equality checks.
     */
    @Test
    public void testEqualsAfterSettingEntityCollection() {
        ChartRenderingInfo i1 = new ChartRenderingInfo();
        ChartRenderingInfo i2 = new ChartRenderingInfo();
        
        // Start with equal objects
        i1.setChartArea(CHART_AREA);
        i2.setChartArea(CHART_AREA);
        i1.getPlotInfo().setDataArea(PLOT_DATA_AREA);
        i2.getPlotInfo().setDataArea(PLOT_DATA_AREA);
        assertEquals(i1, i2, "Objects should be equal initially");
        
        // Add entity to first object
        StandardEntityCollection e1 = new StandardEntityCollection();
        e1.add(new ChartEntity(ENTITY_BOUNDS));
        i1.setEntityCollection(e1);
        assertNotEquals(i1, i2, "Objects should differ after setting entityCollection in one");
        
        // Add same entity to second object
        StandardEntityCollection e2 = new StandardEntityCollection();
        e2.add(new ChartEntity(ENTITY_BOUNDS));
        i2.setEntityCollection(e2);
        assertEquals(i1, i2, "Objects should be equal after setting same entityCollection in both");
    }

    // =====================================================================
    // Tests for cloning
    // =====================================================================

    /**
     * Tests that cloning creates an equal copy of the default object.
     */
    @Test
    public void testCloningCreatesEqualCopy() throws CloneNotSupportedException {
        ChartRenderingInfo original = new ChartRenderingInfo();
        ChartRenderingInfo clone = CloneUtils.clone(original);
        
        assertNotSame(original, clone, "Clone should be a different object");
        assertSame(original.getClass(), clone.getClass(), "Clone should be same class");
        assertEquals(original, clone, "Clone should be equal to original");
    }

    /**
     * Tests that modifications to chartArea don't affect the clone.
     */
    @Test
    public void testCloningChartAreaIsIndependent() throws CloneNotSupportedException {
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.setChartArea(CHART_AREA);
        ChartRenderingInfo clone = CloneUtils.clone(original);
        
        // Modify original
        original.getChartArea().setRect(MODIFIED_CHART_AREA);
        assertNotEquals(original, clone, "Clone should not change when original's chartArea is modified");
        
        // Apply same modification to clone
        clone.getChartArea().setRect(MODIFIED_CHART_AREA);
        assertEquals(original, clone, "Objects should be equal after same modification to both");
    }

    /**
     * Tests that modifications to entityCollection don't affect the clone.
     */
    @Test
    public void testCloningEntityCollectionIsIndependent() throws CloneNotSupportedException {
        ChartRenderingInfo original = new ChartRenderingInfo();
        ChartRenderingInfo clone = CloneUtils.clone(original);
        
        // Add entity to original
        original.getEntityCollection().add(new ChartEntity(ENTITY_BOUNDS));
        assertNotEquals(original, clone, "Clone should not change when original's entityCollection is modified");
        
        // Add same entity to clone
        clone.getEntityCollection().add(new ChartEntity(ENTITY_BOUNDS));
        assertEquals(original, clone, "Objects should be equal after same entity added to both");
    }

    // =====================================================================
    // Tests for serialization
    // =====================================================================

    /**
     * Tests serialization when chartArea is set.
     */
    @Test
    public void testSerializationWithChartArea() {
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.setChartArea(CHART_AREA);
        
        ChartRenderingInfo deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized object should equal original");
    }

    /**
     * Tests serialization when plotInfo's dataArea is set, including owner reference.
     */
    @Test
    public void testSerializationWithPlotInfo() {
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.getPlotInfo().setDataArea(PLOT_DATA_AREA);
        
        ChartRenderingInfo deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized object should equal original");
        
        // Verify owner reference in plotInfo
        assertSame(
            deserialized, 
            deserialized.getPlotInfo().getOwner(),
            "PlotInfo should reference owning ChartRenderingInfo"
        );
    }
}