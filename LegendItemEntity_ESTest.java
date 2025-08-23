package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.time.chrono.HijrahEra;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XIntervalSeriesCollection;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class LegendItemEntityTest extends LegendItemEntity_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSetAndGetSeriesKey() {
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<HijrahEra> entity = new LegendItemEntity<>(area);
        HijrahEra expectedKey = HijrahEra.AH;

        entity.setSeriesKey(expectedKey);
        HijrahEra actualKey = entity.getSeriesKey();

        assertSame("The series key should be the same as the one set", expectedKey, actualKey);
    }

    @Test(timeout = 4000)
    public void testSetAndGetDataset() {
        Rectangle2D.Float area = new Rectangle2D.Float();
        Rectangle bounds = area.getBounds();
        LegendItemEntity<Integer> entity = new LegendItemEntity<>(bounds);
        DefaultTableXYDataset<Integer> expectedDataset = new DefaultTableXYDataset<>(false);

        entity.setDataset(expectedDataset);
        DefaultTableXYDataset actualDataset = (DefaultTableXYDataset) entity.getDataset();

        assertTrue("The dataset should notify by default", actualDataset.getNotify());
    }

    @Test(timeout = 4000)
    public void testConstructorThrowsExceptionOnNullShape() {
        try {
            new LegendItemEntity<Integer>(null);
            fail("Expected IllegalArgumentException for null 'area' argument");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloneAndEqualsWithModifiedDataset() {
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<Integer> entity = new LegendItemEntity<>(area);
        Object clonedEntity = entity.clone();
        XIntervalSeriesCollection<Integer> newDataset = new XIntervalSeriesCollection<>();

        entity.setDataset(newDataset);
        boolean isEqual = entity.equals(clonedEntity);

        assertFalse("The entity should not be equal to its clone after modifying the dataset", isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObjectType() {
        Line2D.Float line = new Line2D.Float();
        Rectangle2D bounds = line.getBounds2D();
        LegendItemEntity<Integer> entity = new LegendItemEntity<>(bounds);
        Object differentObject = new Object();

        boolean isEqual = entity.equals(differentObject);

        assertFalse("The entity should not be equal to an object of different type", isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSelf() {
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<Integer> entity = new LegendItemEntity<>(area);

        boolean isEqual = entity.equals(entity);

        assertTrue("The entity should be equal to itself", isEqual);
    }

    @Test(timeout = 4000)
    public void testToStringRepresentation() {
        Rectangle area = new Rectangle(0, 0, 0, 0);
        LegendItemEntity<Integer> entity = new LegendItemEntity<>(area);

        String expectedString = "LegendItemEntity: seriesKey=null, dataset=null";
        String actualString = entity.toString();

        assertEquals("The string representation should match the expected format", expectedString, actualString);
    }

    @Test(timeout = 4000)
    public void testGetSeriesKeyWhenNotSet() {
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<HijrahEra> entity = new LegendItemEntity<>(area);

        assertNull("The series key should be null when not set", entity.getSeriesKey());
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentSeriesKey() {
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<Integer> entity1 = new LegendItemEntity<>(area);
        Integer seriesKey = JLayeredPane.POPUP_LAYER;
        entity1.setSeriesKey(seriesKey);

        LegendItemEntity<Integer> entity2 = new LegendItemEntity<>(area);

        boolean isEqual = entity1.equals(entity2);

        assertFalse("Entities with different series keys should not be equal", isEqual);
    }

    @Test(timeout = 4000)
    public void testCloneAndEqualsWithSameState() {
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<Integer> entity = new LegendItemEntity<>(area);
        Object clonedEntity = entity.clone();

        boolean isEqual = entity.equals(clonedEntity);

        assertTrue("The entity should be equal to its clone when state is unchanged", isEqual);
    }

    @Test(timeout = 4000)
    public void testGetDatasetWhenNotSet() {
        Rectangle2D.Double area = new Rectangle2D.Double();
        LegendItemEntity<Integer> entity = new LegendItemEntity<>(area);

        assertNull("The dataset should be null when not set", entity.getDataset());
    }
}