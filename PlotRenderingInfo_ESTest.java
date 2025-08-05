/*
 * ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * A refactored version of the original EvoSuite test for PlotRenderingInfo.
 * The goal of this refactoring is to improve readability and maintainability.
 */
package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;

/**
 * A collection of tests for the {@link PlotRenderingInfo} class.
 * This suite focuses on verifying the correctness of its state management,
 * subplot handling, and core object methods like equals(), hashCode(), and clone().
 */
public class PlotRenderingInfoTest {

    private ChartRenderingInfo chartInfo;
    private PlotRenderingInfo plotInfo;

    @Before
    public void setUp() {
        chartInfo = new ChartRenderingInfo();
        plotInfo = new PlotRenderingInfo(chartInfo);
    }

    // -----------------
    // Constructor and Initial State Tests
    // -----------------

    @Test
    public void newInstance_shouldHaveZeroSubplots() {
        assertEquals("A new PlotRenderingInfo should have 0 subplots.", 0, plotInfo.getSubplotCount());
    }

    @Test
    public void newInstance_shouldHaveNullPlotArea() {
        assertNull("The plot area should be null by default.", plotInfo.getPlotArea());
    }

    @Test
    public void newInstance_shouldHaveNonNullDataArea() {
        // The default data area is an empty rectangle at (0,0).
        assertNotNull("The data area should not be null by default.", plotInfo.getDataArea());
        assertEquals(new Rectangle2D.Double(), plotInfo.getDataArea());
    }

    @Test
    public void getOwner_shouldReturnTheChartRenderingInfoItWasCreatedWith() {
        assertSame("getOwner() should return the instance provided in the constructor.", chartInfo, plotInfo.getOwner());
    }

    @Test
    public void getOwner_shouldReturnNull_whenConstructedWithNull() {
        PlotRenderingInfo infoWithNullOwner = new PlotRenderingInfo(null);
        assertNull("getOwner() should return null if constructed with null.", infoWithNullOwner.getOwner());
    }

    // -----------------
    // Property Getter/Setter Tests
    // -----------------

    @Test
    public void getPlotArea_shouldReturnPreviouslySetArea() {
        // Arrange
        Rectangle2D.Double expectedArea = new Rectangle2D.Double(10, 20, 30, 40);
        plotInfo.setPlotArea(expectedArea);

        // Act
        Rectangle2D actualArea = plotInfo.getPlotArea();

        // Assert
        assertEquals(expectedArea, actualArea);
    }

    @Test
    public void getDataArea_shouldReturnPreviouslySetArea() {
        // Arrange
        Rectangle2D.Double expectedArea = new Rectangle2D.Double(5, 15, 25, 35);
        plotInfo.setDataArea(expectedArea);

        // Act
        Rectangle2D actualArea = plotInfo.getDataArea();

        // Assert
        assertEquals(expectedArea, actualArea);
    }

    // -----------------
    // Subplot Management Tests
    // -----------------

    @Test
    public void addSubplotInfo_shouldIncreaseSubplotCount() {
        // Arrange
        PlotRenderingInfo subplotInfo = new PlotRenderingInfo(chartInfo);

        // Act
        plotInfo.addSubplotInfo(subplotInfo);

        // Assert
        assertEquals("Subplot count should be 1 after adding a subplot.", 1, plotInfo.getSubplotCount());
        assertSame("getSubplotInfo should return the added subplot.", subplotInfo, plotInfo.getSubplotInfo(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSubplotInfo_shouldThrowException_forInvalidNegativeIndex() {
        plotInfo.getSubplotInfo(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSubplotInfo_shouldThrowException_forInvalidPositiveIndex() {
        // No subplots added, so index 0 is out of bounds.
        plotInfo.getSubplotInfo(0);
    }

    @Test
    public void getSubplotIndex_shouldReturnCorrectIndex_whenPointIsInSubplotDataArea() {
        // Arrange
        PlotRenderingInfo subplotInfo = new PlotRenderingInfo(chartInfo);
        subplotInfo.setDataArea(new Rectangle2D.Double(50, 50, 100, 100));
        plotInfo.addSubplotInfo(subplotInfo);
        Point2D pointInside = new Point2D.Double(75, 75);

        // Act
        int subplotIndex = plotInfo.getSubplotIndex(pointInside);

        // Assert
        assertEquals("Should return the index of the subplot containing the point.", 0, subplotIndex);
    }

    @Test
    public void getSubplotIndex_shouldReturnNegativeOne_whenPointIsOutsideAllSubplots() {
        // Arrange
        PlotRenderingInfo subplotInfo = new PlotRenderingInfo(chartInfo);
        subplotInfo.setDataArea(new Rectangle2D.Double(50, 50, 100, 100));
        plotInfo.addSubplotInfo(subplotInfo);
        Point2D pointOutside = new Point2D.Double(10, 10);

        // Act
        int subplotIndex = plotInfo.getSubplotIndex(pointOutside);

        // Assert
        assertEquals("Should return -1 when the point is not in any subplot.", -1, subplotIndex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSubplotIndex_shouldThrowException_whenSourcePointIsNull() {
        plotInfo.getSubplotIndex(null);
    }

    // -----------------
    // equals() and hashCode() Contract Tests
    // -----------------

    @Test
    public void equals_shouldReturnTrue_forSameInstance() {
        assertTrue("An object should be equal to itself.", plotInfo.equals(plotInfo));
    }

    @Test
    public void equals_shouldReturnTrue_forTwoSeparateButIdenticalInstances() {
        PlotRenderingInfo plotInfo1 = new PlotRenderingInfo(chartInfo);
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo);
        assertTrue("Two new instances with the same owner should be equal.", plotInfo1.equals(plotInfo2));
    }

    @Test
    public void equals_shouldReturnFalse_forNull() {
        assertFalse("An object should not be equal to null.", plotInfo.equals(null));
    }

    @Test
    public void equals_shouldReturnFalse_forDifferentClass() {
        assertFalse("An object should not be equal to an object of a different class.", plotInfo.equals("a string"));
    }

    @Test
    public void equals_shouldReturnFalse_whenPlotAreaDiffers() {
        PlotRenderingInfo plotInfo1 = new PlotRenderingInfo(chartInfo);
        plotInfo1.setPlotArea(new Rectangle2D.Double(1, 1, 1, 1));
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo); // plotArea is null

        assertFalse("Instances with different plot areas should not be equal.", plotInfo1.equals(plotInfo2));
    }

    @Test
    public void equals_shouldReturnFalse_whenDataAreaDiffers() {
        PlotRenderingInfo plotInfo1 = new PlotRenderingInfo(chartInfo);
        plotInfo1.setDataArea(new Rectangle2D.Double(1, 1, 1, 1));
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo); // default data area

        assertFalse("Instances with different data areas should not be equal.", plotInfo1.equals(plotInfo2));
    }

    @Test
    public void equals_shouldReturnFalse_whenSubplotsDiffer() {
        PlotRenderingInfo plotInfo1 = new PlotRenderingInfo(chartInfo);
        plotInfo1.addSubplotInfo(new PlotRenderingInfo(chartInfo));
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo); // no subplots

        assertFalse("Instances with different subplots should not be equal.", plotInfo1.equals(plotInfo2));
    }


    @Test
    public void hashCode_shouldBeConsistentForEqualObjects() {
        PlotRenderingInfo plotInfo1 = new PlotRenderingInfo(chartInfo);
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo);

        assertEquals("Equal objects must have equal hash codes.", plotInfo1.hashCode(), plotInfo2.hashCode());
    }

    // -----------------
    // clone() Tests
    // -----------------

    @Test
    public void clone_shouldProduceIndependentObjectWithSameState() throws CloneNotSupportedException {
        // Arrange
        plotInfo.setPlotArea(new Rectangle2D.Double(10, 20, 30, 40));
        plotInfo.setDataArea(new Rectangle2D.Double(15, 25, 20, 20));
        plotInfo.addSubplotInfo(new PlotRenderingInfo(chartInfo));

        // Act
        PlotRenderingInfo clonedInfo = (PlotRenderingInfo) plotInfo.clone();

        // Assert
        assertNotSame("Clone must be a different object.", plotInfo, clonedInfo);
        assertEquals("Clone must be equal to the original.", plotInfo, clonedInfo);

        // Verify deep copy of mutable fields
        clonedInfo.getDataArea().setRect(0, 0, 0, 0);
        assertNotEquals("Modifying clone's dataArea should not affect original.",
                clonedInfo.getDataArea(), plotInfo.getDataArea());
    }
}