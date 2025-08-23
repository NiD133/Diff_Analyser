package org.jfree.chart.axis;

import org.junit.Test;

import static org.junit.Assert.*;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;

public class CategoryLabelPositionTest {

  @Test
  public void defaultConstructor_hasDocumentedDefaults() {
    CategoryLabelPosition p = new CategoryLabelPosition();

    assertEquals(RectangleAnchor.CENTER, p.getCategoryAnchor());
    assertEquals(TextBlockAnchor.BOTTOM_CENTER, p.getLabelAnchor());
    assertEquals(TextAnchor.CENTER, p.getRotationAnchor());
    assertEquals(0.0, p.getAngle(), 0.0);
    assertEquals(CategoryLabelWidthType.CATEGORY, p.getWidthType());
    assertEquals(0.95f, p.getWidthRatio(), 0.0f);
  }

  @Test
  public void twoArgConstructor_setsAnchorsAndUsesDefaultsForOthers() {
    CategoryLabelPosition p = new CategoryLabelPosition(
        RectangleAnchor.TOP_RIGHT, TextBlockAnchor.CENTER_RIGHT);

    assertEquals(RectangleAnchor.TOP_RIGHT, p.getCategoryAnchor());
    assertEquals(TextBlockAnchor.CENTER_RIGHT, p.getLabelAnchor());
    assertEquals(TextAnchor.CENTER, p.getRotationAnchor());
    assertEquals(0.0, p.getAngle(), 0.0);
    assertEquals(CategoryLabelWidthType.CATEGORY, p.getWidthType());
    assertEquals(0.95f, p.getWidthRatio(), 0.0f);
  }

  @Test
  public void widthTypeConstructor_setsWidthFieldsOnly() {
    CategoryLabelPosition p = new CategoryLabelPosition(
        RectangleAnchor.BOTTOM_RIGHT, TextBlockAnchor.CENTER_LEFT,
        CategoryLabelWidthType.RANGE, -641.25f);

    assertEquals(RectangleAnchor.BOTTOM_RIGHT, p.getCategoryAnchor());
    assertEquals(TextBlockAnchor.CENTER_LEFT, p.getLabelAnchor());
    assertEquals(CategoryLabelWidthType.RANGE, p.getWidthType());
    assertEquals(-641.25f, p.getWidthRatio(), 0.0f);
    assertEquals(TextAnchor.CENTER, p.getRotationAnchor());
    assertEquals(0.0, p.getAngle(), 0.0);
  }

  @Test
  public void fullConstructor_setsAllFields() {
    CategoryLabelPosition p = new CategoryLabelPosition(
        RectangleAnchor.BOTTOM,
        TextBlockAnchor.TOP_LEFT,
        TextAnchor.BASELINE_RIGHT,
        -1.0,
        CategoryLabelWidthType.CATEGORY,
        -1989.5195f
    );

    assertEquals(RectangleAnchor.BOTTOM, p.getCategoryAnchor());
    assertEquals(TextBlockAnchor.TOP_LEFT, p.getLabelAnchor());
    assertEquals(TextAnchor.BASELINE_RIGHT, p.getRotationAnchor());
    assertEquals(-1.0, p.getAngle(), 0.0);
    assertEquals(CategoryLabelWidthType.CATEGORY, p.getWidthType());
    assertEquals(-1989.5195f, p.getWidthRatio(), 0.0f);
  }

  @Test
  public void getters_returnExactlyWhatWasProvided() {
    RectangleAnchor c = RectangleAnchor.RIGHT;
    TextBlockAnchor l = TextBlockAnchor.BOTTOM_CENTER;
    TextAnchor r = TextAnchor.BASELINE_CENTER;
    double angle = 0.123;
    CategoryLabelWidthType wt = CategoryLabelWidthType.RANGE;
    float wr = -985.5677f;

    CategoryLabelPosition p = new CategoryLabelPosition(c, l, r, angle, wt, wr);

    assertSame(c, p.getCategoryAnchor());
    assertSame(l, p.getLabelAnchor());
    assertSame(r, p.getRotationAnchor());
    assertSame(wt, p.getWidthType());
    assertEquals(angle, p.getAngle(), 0.0);
    assertEquals(wr, p.getWidthRatio(), 0.0f);
  }

  @Test
  public void equals_andHashCode_matchForIdenticalValues() {
    CategoryLabelPosition a = new CategoryLabelPosition(
        RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.TOP_CENTER,
        1.0, CategoryLabelWidthType.RANGE, 0.5f);
    CategoryLabelPosition b = new CategoryLabelPosition(
        RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.TOP_CENTER,
        1.0, CategoryLabelWidthType.RANGE, 0.5f);

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void equals_detectsDifferencesInEachField() {
    CategoryLabelPosition base = new CategoryLabelPosition(
        RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.TOP_CENTER,
        1.0, CategoryLabelWidthType.RANGE, 0.5f);

    assertNotEquals(base, new CategoryLabelPosition(
        RectangleAnchor.TOP, TextBlockAnchor.CENTER, TextAnchor.TOP_CENTER,
        1.0, CategoryLabelWidthType.RANGE, 0.5f));

    assertNotEquals(base, new CategoryLabelPosition(
        RectangleAnchor.CENTER, TextBlockAnchor.BOTTOM_CENTER, TextAnchor.TOP_CENTER,
        1.0, CategoryLabelWidthType.RANGE, 0.5f));

    assertNotEquals(base, new CategoryLabelPosition(
        RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER,
        1.0, CategoryLabelWidthType.RANGE, 0.5f));

    assertNotEquals(base, new CategoryLabelPosition(
        RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.TOP_CENTER,
        2.0, CategoryLabelWidthType.RANGE, 0.5f));

    assertNotEquals(base, new CategoryLabelPosition(
        RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.TOP_CENTER,
        1.0, CategoryLabelWidthType.CATEGORY, 0.5f));

    assertNotEquals(base, new CategoryLabelPosition(
        RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.TOP_CENTER,
        1.0, CategoryLabelWidthType.RANGE, 0.6f));
  }

  @Test
  public void equals_isReflexiveAndNotEqualToOtherTypesOrNull() {
    CategoryLabelPosition p = new CategoryLabelPosition();

    assertEquals(p, p);
    assertNotEquals(p, null);
    assertNotEquals(p, new Object());
  }

  @Test
  public void nullChecks_twoArgConstructor() {
    IllegalArgumentException e1 = assertThrows(
        IllegalArgumentException.class,
        () -> new CategoryLabelPosition(null, TextBlockAnchor.CENTER));
    assertTrue(e1.getMessage().toLowerCase().contains("categoryanchor"));

    IllegalArgumentException e2 = assertThrows(
        IllegalArgumentException.class,
        () -> new CategoryLabelPosition(RectangleAnchor.CENTER, null));
    assertTrue(e2.getMessage().toLowerCase().contains("labelanchor"));
  }

  @Test
  public void nullChecks_widthTypeConstructor() {
    IllegalArgumentException e1 = assertThrows(
        IllegalArgumentException.class,
        () -> new CategoryLabelPosition(null, TextBlockAnchor.CENTER, CategoryLabelWidthType.CATEGORY, 0.5f));
    assertTrue(e1.getMessage().toLowerCase().contains("categoryanchor"));

    IllegalArgumentException e2 = assertThrows(
        IllegalArgumentException.class,
        () -> new CategoryLabelPosition(RectangleAnchor.CENTER, null, CategoryLabelWidthType.CATEGORY, 0.5f));
    assertTrue(e2.getMessage().toLowerCase().contains("labelanchor"));

    IllegalArgumentException e3 = assertThrows(
        IllegalArgumentException.class,
        () -> new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, null, 0.5f));
    assertTrue(e3.getMessage().toLowerCase().contains("widthtype"));
  }

  @Test
  public void nullChecks_fullConstructor() {
    assertThrows(IllegalArgumentException.class,
        () -> new CategoryLabelPosition(null, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.0,
            CategoryLabelWidthType.CATEGORY, 0.5f));

    assertThrows(IllegalArgumentException.class,
        () -> new CategoryLabelPosition(RectangleAnchor.CENTER, null, TextAnchor.CENTER, 0.0,
            CategoryLabelWidthType.CATEGORY, 0.5f));

    assertThrows(IllegalArgumentException.class,
        () -> new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, null, 0.0,
            CategoryLabelWidthType.CATEGORY, 0.5f));

    assertThrows(IllegalArgumentException.class,
        () -> new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.CENTER, TextAnchor.CENTER, 0.0,
            null, 0.5f));
  }
}