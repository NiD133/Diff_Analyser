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

    // Test data constants for better maintainability
    private static final Rectangle2D CHART_AREA_1 = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
    private static final Rectangle2D CHART_AREA_2 = new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0);
    private static final Rectangle DATA_AREA = new Rectangle(1, 2, 3, 4);
    private static final Rectangle ENTITY_AREA_1 = new Rectangle(1, 2, 3, 4);
    private static final Rectangle ENTITY_AREA_2 = new Rectangle(1, 2, 2, 1);

    /**
     * Tests that the equals method correctly compares all fields of ChartRenderingInfo objects.
     * This test verifies equality behavior for:
     * - Default instances
     * - Chart area differences
     * - Plot data area differences  
     * - Entity collection differences
     */
    @Test
    public void testEquals() {
        // Test 1: Default instances should be equal
        ChartRenderingInfo defaultInfo1 = new ChartRenderingInfo();
        ChartRenderingInfo defaultInfo2 = new ChartRenderingInfo();
        assertEquals(defaultInfo1, defaultInfo2, "Default ChartRenderingInfo instances should be equal");

        // Test 2: Chart area affects equality
        defaultInfo1.setChartArea(CHART_AREA_1);
        assertNotEquals(defaultInfo1, defaultInfo2, "Instances with different chart areas should not be equal");
        
        defaultInfo2.setChartArea(CHART_AREA_1);
        assertEquals(defaultInfo1, defaultInfo2, "Instances with same chart areas should be equal");

        // Test 3: Plot data area affects equality
        defaultInfo1.getPlotInfo().setDataArea(DATA_AREA);
        assertNotEquals(defaultInfo1, defaultInfo2, "Instances with different plot data areas should not be equal");
        
        defaultInfo2.getPlotInfo().setDataArea(DATA_AREA);
        assertEquals(defaultInfo1, defaultInfo2, "Instances with same plot data areas should be equal");

        // Test 4: Entity collection affects equality
        StandardEntityCollection entityCollection1 = createEntityCollectionWithSingleEntity(ENTITY_AREA_1);
        defaultInfo1.setEntityCollection(entityCollection1);
        assertNotEquals(defaultInfo1, defaultInfo2, "Instances with different entity collections should not be equal");
        
        StandardEntityCollection entityCollection2 = createEntityCollectionWithSingleEntity(ENTITY_AREA_1);
        defaultInfo2.setEntityCollection(entityCollection2);
        assertEquals(defaultInfo1, defaultInfo2, "Instances with same entity collections should be equal");
    }

    /**
     * Tests that ChartRenderingInfo objects can be properly cloned and that
     * the cloned objects are independent of the original.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Create original instance
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        
        // Clone the instance
        ChartRenderingInfo clonedInfo = CloneUtils.clone(originalInfo);

        // Verify basic clone properties
        assertNotSame(originalInfo, clonedInfo, "Cloned instance should be a different object");
        assertSame(originalInfo.getClass(), clonedInfo.getClass(), "Cloned instance should have same class");
        assertEquals(originalInfo, clonedInfo, "Cloned instance should be equal to original");

        // Test independence: Modifying chart area
        originalInfo.getChartArea().setRect(CHART_AREA_2.getX(), CHART_AREA_2.getY(), 
                                          CHART_AREA_2.getWidth(), CHART_AREA_2.getHeight());
        assertNotEquals(originalInfo, clonedInfo, "Modifying original chart area should not affect clone");
        
        clonedInfo.getChartArea().setRect(CHART_AREA_2.getX(), CHART_AREA_2.getY(), 
                                        CHART_AREA_2.getWidth(), CHART_AREA_2.getHeight());
        assertEquals(originalInfo, clonedInfo, "Both instances should be equal after same modifications");

        // Test independence: Modifying entity collection
        originalInfo.getEntityCollection().add(new ChartEntity(ENTITY_AREA_2));
        assertNotEquals(originalInfo, clonedInfo, "Modifying original entity collection should not affect clone");
        
        clonedInfo.getEntityCollection().add(new ChartEntity(ENTITY_AREA_2));
        assertEquals(originalInfo, clonedInfo, "Both instances should be equal after same entity additions");
    }

    /**
     * Tests serialization of ChartRenderingInfo with chart area set.
     * Verifies that serialized and deserialized objects are equal.
     */
    @Test
    public void testSerializationWithChartArea() {
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        originalInfo.setChartArea(CHART_AREA_1);
        
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);
        
        assertEquals(originalInfo, deserializedInfo, 
                    "Deserialized ChartRenderingInfo should equal original");
    }

    /**
     * Tests serialization of ChartRenderingInfo with plot data area set.
     * Verifies that serialized and deserialized objects are equal and that
     * the plot info maintains proper owner reference after deserialization.
     */
    @Test
    public void testSerializationWithPlotDataArea() {
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        Rectangle2D plotDataArea = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        originalInfo.getPlotInfo().setDataArea(plotDataArea);
        
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);
        
        assertEquals(originalInfo, deserializedInfo, 
                    "Deserialized ChartRenderingInfo should equal original");
        assertEquals(deserializedInfo, deserializedInfo.getPlotInfo().getOwner(), 
                    "PlotInfo should maintain correct owner reference after deserialization");
    }

    /**
     * Helper method to create a StandardEntityCollection with a single ChartEntity.
     * 
     * @param entityArea the area for the chart entity
     * @return a new StandardEntityCollection containing one ChartEntity
     */
    private StandardEntityCollection createEntityCollectionWithSingleEntity(Rectangle entityArea) {
        StandardEntityCollection collection = new StandardEntityCollection();
        collection.add(new ChartEntity(entityArea));
        return collection;
    }
}