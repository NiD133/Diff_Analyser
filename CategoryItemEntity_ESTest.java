package org.jfree.chart.entity;

import static org.junit.Assert.*;

import java.awt.geom.Rectangle2D;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.junit.Test;

public class CategoryItemEntityTest {

  private static Rectangle2D area() {
    return new Rectangle2D.Double(0, 0, 10, 10);
  }

  private static DefaultMultiValueCategoryDataset<Integer, Integer> datasetA() {
    return new DefaultMultiValueCategoryDataset<>();
  }

  private static DefaultBoxAndWhiskerCategoryDataset<Integer, Integer> datasetB() {
    return new DefaultBoxAndWhiskerCategoryDataset<>();
  }

  // --------------------------------------------------------------------------
  // Construction and basic accessors
  // --------------------------------------------------------------------------

  @Test
  public void constructorRejectsNullDataset() {
    try {
      new CategoryItemEntity<Integer, Integer>(area(), "tip", "url", null, 1, 1);
      fail("Expected IllegalArgumentException for null dataset");
    } catch (IllegalArgumentException ex) {
      // message text comes from Args.nullNotPermitted(..)
      assertTrue(ex.getMessage() == null || ex.getMessage().toLowerCase().contains("null"));
    }
  }

  @Test
  public void getDatasetReturnsSameInstancePassedToConstructor() {
    CategoryDataset<Integer, Integer> ds = datasetA();
    CategoryItemEntity<Integer, Integer> entity =
        new CategoryItemEntity<>(area(), "tip", "url", ds, 1, 2);

    assertSame(ds, entity.getDataset());
  }

  @Test
  public void toStringIsNonNull() {
    CategoryItemEntity<Integer, Integer> entity =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);

    assertNotNull(entity.toString());
  }

  // --------------------------------------------------------------------------
  // Dataset mutation
  // --------------------------------------------------------------------------

  @Test
  public void setDatasetRejectsNull() {
    CategoryItemEntity<Integer, Integer> entity =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);

    try {
      entity.setDataset(null);
      fail("Expected IllegalArgumentException for null dataset");
    } catch (IllegalArgumentException ex) {
      // ok
    }
  }

  // --------------------------------------------------------------------------
  // Row/column key accessors
  // --------------------------------------------------------------------------

  @Test
  public void getRowKeyAndColumnKeyReturnValuesProvided() {
    CategoryItemEntity<Integer, Integer> entity =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 7, 9);

    assertEquals(Integer.valueOf(7), entity.getRowKey());
    assertEquals(Integer.valueOf(9), entity.getColumnKey());
  }

  @Test
  public void setRowKeyAllowsNullEvenThoughJavadocSaysNotPermitted() {
    // Document current behavior: null is accepted and returned.
    CategoryItemEntity<Integer, Integer> entity =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);

    entity.setRowKey(null);
    assertNull(entity.getRowKey());
  }

  @Test
  public void setColumnKeyAllowsNullEvenThoughJavadocSaysNotPermitted() {
    // Document current behavior: null is accepted and returned.
    CategoryItemEntity<Integer, Integer> entity =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);

    entity.setColumnKey(null);
    assertNull(entity.getColumnKey());
  }

  // --------------------------------------------------------------------------
  // Equality
  // --------------------------------------------------------------------------

  @Test
  public void equalsIsReflexive() {
    CategoryItemEntity<Integer, Integer> entity =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);

    assertTrue(entity.equals(entity));
  }

  @Test
  public void equalsReturnsFalseForDifferentType() {
    CategoryItemEntity<Integer, Integer> entity =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);

    assertFalse(entity.equals("not an entity"));
  }

  @Test
  public void equalsConsidersRowAndColumnKeys() {
    CategoryItemEntity<Integer, Integer> a =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);
    CategoryItemEntity<Integer, Integer> bDifferentRow =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 3, 2);
    CategoryItemEntity<Integer, Integer> cDifferentCol =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 4);

    assertFalse(a.equals(bDifferentRow));
    assertFalse(a.equals(cDifferentCol));
  }

  @Test
  public void equalsConsidersDatasetReference() {
    // Same keys and metadata but different dataset instances => not equal
    CategoryItemEntity<Integer, Integer> a =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);
    CategoryItemEntity<Integer, Integer> b =
        new CategoryItemEntity<>(area(), "tip", "url", datasetB(), 1, 2);

    assertFalse(a.equals(b));
  }

  @Test
  public void cloneProducesEqualButDistinctInstanceWhenKeysAreNonNull() throws Exception {
    CategoryItemEntity<Integer, Integer> original =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), 1, 2);

    CategoryItemEntity<Integer, Integer> copy =
        (CategoryItemEntity<Integer, Integer>) original.clone();

    assertNotSame(original, copy);
    assertTrue(original.equals(copy));
  }

  @Test
  public void equalsMayThrowNpeWhenBothKeysAreNull_DocumentedCurrentBehavior() throws Exception {
    // Note: The constructor accepts null keys; calling equals() can throw NPE.
    // This test documents that behavior to prevent accidental regressions.
    CategoryItemEntity<Integer, Integer> entityWithNullKeys =
        new CategoryItemEntity<>(area(), "tip", "url", datasetA(), null, null);
    Object clone = entityWithNullKeys.clone();

    try {
      // Historically throws NullPointerException when comparing null keys.
      entityWithNullKeys.equals(clone);
      fail("Expected NullPointerException when comparing entities with null keys");
    } catch (NullPointerException expected) {
      // ok - documenting current behavior
    }
  }
}