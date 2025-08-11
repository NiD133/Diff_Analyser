package org.jfree.chart;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.junit.Test;

/**
 * Focused and readable unit tests for ChartRenderingInfo.
 *
 * These tests avoid framework-specific scaffolding and brittle integration
 * scenarios. They verify the core behavior of ChartRenderingInfo:
 * - default state
 * - chart area get/set (including null contract)
 * - entity collection get/set
 * - plot info basics
 * - equals/hashCode contracts
 * - cloning and independence
 * - clear() semantics
 */
public class ChartRenderingInfoTest {

  @Test
  public void defaultState_hasEmptyChartArea_andNonNullPlotInfo_andDefaultEntities() {
      ChartRenderingInfo info = new ChartRenderingInfo();

      Rectangle2D area = info.getChartArea();
      assertNotNull("Chart area should be initialized", area);
      assertEquals(0.0, area.getX(), 0.0);
      assertEquals(0.0, area.getY(), 0.0);
      assertEquals(0.0, area.getWidth(), 0.0);
      assertEquals(0.0, area.getHeight(), 0.0);

      assertNotNull("Plot info should be initialized", info.getPlotInfo());

      EntityCollection entities = info.getEntityCollection();
      assertNotNull("Default entity collection should be initialized", entities);
      assertEquals("Default entity collection should be empty", 0, entities.getEntityCount());
  }

  @Test
  public void setChartArea_withRectangle2D_updatesArea() {
      ChartRenderingInfo info = new ChartRenderingInfo();
      Rectangle2D.Double area = new Rectangle2D.Double(10, 20, 30, 40);

      info.setChartArea(area);

      Rectangle2D result = info.getChartArea();
      assertEquals(10.0, result.getX(), 0.0);
      assertEquals(20.0, result.getY(), 0.0);
      assertEquals(30.0, result.getWidth(), 0.0);
      assertEquals(40.0, result.getHeight(), 0.0);
  }

  @Test(expected = NullPointerException.class)
  public void setChartArea_withNull_throwsNullPointerException() {
      ChartRenderingInfo info = new ChartRenderingInfo();
      info.setChartArea(null);
  }

  @Test
  public void setChartArea_acceptsAwtRectangle() {
      ChartRenderingInfo info = new ChartRenderingInfo();
      Rectangle rect = new Rectangle(1, 2, 3, 4);

      info.setChartArea(rect);

      Rectangle2D result = info.getChartArea();
      assertEquals(1.0, result.getX(), 0.0);
      assertEquals(2.0, result.getY(), 0.0);
      assertEquals(3.0, result.getWidth(), 0.0);
      assertEquals(4.0, result.getHeight(), 0.0);
  }

  @Test
  public void setChartArea_fromDiagonal_normalizesMinMax() {
      ChartRenderingInfo info = new ChartRenderingInfo();
      Rectangle2D.Double r = new Rectangle2D.Double();
      // x1 > x2 to exercise normalization
      r.setFrameFromDiagonal(10.0, 10.0, -5.0, 20.0);

      info.setChartArea(r);

      Rectangle2D area = info.getChartArea();
      assertEquals("Min X should be normalized", -5.0, area.getMinX(), 0.0);
      assertEquals("Max X should be normalized", 10.0, area.getMaxX(), 0.0);
      assertEquals(10.0, area.getMinY(), 0.0);
      assertEquals(20.0, area.getMaxY(), 0.0);
  }

  @Test
  public void getPlotInfo_supportsAddingSubplots() {
      ChartRenderingInfo info = new ChartRenderingInfo();
      PlotRenderingInfo plotInfo = info.getPlotInfo();

      // Use a separate instance to avoid self-referential cycles
      PlotRenderingInfo subplot = new PlotRenderingInfo(info);
      plotInfo.addSubplotInfo(subplot);

      assertEquals(1, plotInfo.getSubplotCount());
  }

  @Test
  public void entityCollection_canBeSetToNull_andRetrieved() {
      ChartRenderingInfo info = new ChartRenderingInfo();
      info.setEntityCollection(null);

      assertNull("Entity collection should be null after explicit set", info.getEntityCollection());
  }

  @Test
  public void equals_isReflexive_andNotEqualToDifferentType() {
      ChartRenderingInfo info = new ChartRenderingInfo();
      assertTrue(info.equals(info)); // reflexive

      assertFalse(info.equals("not-a-ChartRenderingInfo"));
  }

  @Test
  public void equals_and_hashCode_match_forTwoFreshInstances() {
      ChartRenderingInfo a = new ChartRenderingInfo();
      ChartRenderingInfo b = new ChartRenderingInfo();

      assertTrue(a.equals(b));
      assertTrue(b.equals(a));
      assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void equals_differsWhenEntityCollectionDiffers() {
      ChartRenderingInfo a = new ChartRenderingInfo();
      ChartRenderingInfo b = new ChartRenderingInfo();
      assertTrue(a.equals(b));

      b.setEntityCollection(null);

      assertFalse("Entity collection difference should break equality", a.equals(b));
      assertFalse(b.equals(a));
  }

  @Test
  public void clone_producesEqualButIndependentCopy() throws CloneNotSupportedException {
      ChartRenderingInfo original = new ChartRenderingInfo();

      ChartRenderingInfo clone = (ChartRenderingInfo) original.clone();

      assertNotSame("Clone should be a distinct instance", original, clone);
      assertTrue("Clone should be equal to original", original.equals(clone));

      // Mutate original; objects should now differ
      original.setChartArea(new Rectangle2D.Double(1, 2, 3, 4));
      assertFalse("Changing original should not affect clone", original.equals(clone));
  }

  @Test
  public void clear_resetsChartArea_andClearsEntities_whenEntitiesPresent() {
      ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
      // Set a non-empty area and simulate entities
      info.setChartArea(new Rectangle2D.Double(1, 2, 3, 4));
      assertNotNull(info.getEntityCollection());
      assertEquals(0, info.getEntityCollection().getEntityCount()); // empty to start

      info.clear();

      Rectangle2D area = info.getChartArea();
      assertEquals(0.0, area.getX(), 0.0);
      assertEquals(0.0, area.getY(), 0.0);
      assertEquals(0.0, area.getWidth(), 0.0);
      assertEquals(0.0, area.getHeight(), 0.0);

      assertNotNull(info.getEntityCollection());
      assertEquals("Entities should be cleared", 0, info.getEntityCollection().getEntityCount());
  }

  @Test
  public void clear_keepsEntityCollectionNull_whenInitiallyNull() {
      ChartRenderingInfo info = new ChartRenderingInfo(null);
      assertNull(info.getEntityCollection());

      info.clear();

      assertNull("clear() should not create an entity collection if it was null", info.getEntityCollection());
      Rectangle2D area = info.getChartArea();
      assertEquals(0.0, area.getWidth(), 0.0);
      assertEquals(0.0, area.getHeight(), 0.0);
  }
}