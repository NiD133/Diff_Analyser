package org.jfree.chart.block;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

/**
 * Readable unit tests for LabelBlock.
 *
 * The tests focus on:
 * - Constructor and setter preconditions
 * - Default values for new instances
 * - Basic getters/setters behavior
 * - Equality behavior when properties change
 * - Simple arrange/draw happy-path usage
 * - Cloning semantics
 */
public class LabelBlockTest {

  // Helper to create a Graphics2D instance for arrange/draw
  private static Graphics2D newGraphics() {
    return new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB).createGraphics();
  }

  @Test
  public void defaultValues_areAsDocumented() {
    LabelBlock block = new LabelBlock("Hello");

    assertEquals("SansSerif", block.getFont().getName());
    assertEquals(Font.PLAIN, block.getFont().getStyle());
    assertEquals(10, block.getFont().getSize());
    assertEquals(Color.BLACK, block.getPaint());
    assertEquals(TextBlockAnchor.CENTER, block.getContentAlignmentPoint());
    assertEquals(RectangleAnchor.CENTER, block.getTextAnchor());
    assertNull(block.getToolTipText());
    assertNull(block.getURLText());
  }

  @Test
  public void urlText_getterAndSetter() {
    LabelBlock block = new LabelBlock("Link");
    assertNull(block.getURLText());

    block.setURLText("https://example.com");
    assertEquals("https://example.com", block.getURLText());

    block.setURLText(null); // allowed
    assertNull(block.getURLText());
  }

  @Test
  public void toolTip_getterAndSetter() {
    LabelBlock block = new LabelBlock("Tip");
    assertNull(block.getToolTipText());

    block.setToolTipText("Tooltip text");
    assertEquals("Tooltip text", block.getToolTipText());

    block.setToolTipText(null); // allowed
    assertNull(block.getToolTipText());
  }

  @Test
  public void setFont_updatesFont_andRejectsNull() {
    LabelBlock block = new LabelBlock("Font");
    Font f = new Font("Dialog", Font.BOLD, 12);
    block.setFont(f);
    assertSame(f, block.getFont());

    try {
      block.setFont(null);
      fail("Expected IllegalArgumentException for null font");
    } catch (IllegalArgumentException expected) {
      // ok
    }
  }

  @Test
  public void setPaint_updatesPaint_andRejectsNull() {
    LabelBlock block = new LabelBlock("Paint");
    Paint p = Color.RED;
    block.setPaint(p);
    assertSame(p, block.getPaint());

    try {
      block.setPaint(null);
      fail("Expected IllegalArgumentException for null paint");
    } catch (IllegalArgumentException expected) {
      // ok
    }
  }

  @Test
  public void setContentAlignmentPoint_updatesAndRejectsNull() {
    LabelBlock block = new LabelBlock("Align");
    block.setContentAlignmentPoint(TextBlockAnchor.TOP_LEFT);
    assertEquals(TextBlockAnchor.TOP_LEFT, block.getContentAlignmentPoint());

    try {
      block.setContentAlignmentPoint(null);
      fail("Expected IllegalArgumentException for null anchor");
    } catch (IllegalArgumentException expected) {
      // ok
    }
  }

  @Test
  public void setTextAnchor_updatesAndRejectsNull() {
    LabelBlock block = new LabelBlock("Anchor");
    block.setTextAnchor(RectangleAnchor.BOTTOM);
    assertEquals(RectangleAnchor.BOTTOM, block.getTextAnchor());

    try {
      block.setTextAnchor(null);
      fail("Expected IllegalArgumentException for null anchor");
    } catch (IllegalArgumentException expected) {
      // ok
    }
  }

  @Test
  public void constructor_rejectsNullArguments() {
    Font font = new Font("SansSerif", Font.PLAIN, 10);

    try {
      new LabelBlock(null);
      fail("Expected IllegalArgumentException for null text");
    } catch (IllegalArgumentException expected) {
      // ok
    }

    try {
      new LabelBlock("text", null);
      fail("Expected IllegalArgumentException for null font");
    } catch (IllegalArgumentException expected) {
      // ok
    }

    try {
      new LabelBlock("text", font, null);
      fail("Expected IllegalArgumentException for null paint");
    } catch (IllegalArgumentException expected) {
      // ok
    }
  }

  @Test
  public void equals_sameProperties_isTrue() {
    Font font = new Font("Dialog", Font.PLAIN, 11);
    Paint paint = Color.BLUE;

    LabelBlock a = new LabelBlock("Title", font, paint);
    LabelBlock b = new LabelBlock("Title", font, paint);

    assertTrue(a.equals(b));
    assertTrue(b.equals(a));

    // self
    assertTrue(a.equals(a));
  }

  @Test
  public void equals_differsByPaint_isFalse() {
    Font font = new Font("Dialog", Font.PLAIN, 11);

    LabelBlock a = new LabelBlock("Title", font, Color.BLUE);
    LabelBlock b = new LabelBlock("Title", font, Color.RED);

    assertFalse(a.equals(b));
  }

  @Test
  public void equals_differsByAnchor_isFalse() {
    Font font = new Font("Dialog", Font.PLAIN, 11);
    Paint paint = Color.BLACK;

    LabelBlock a = new LabelBlock("Title", font, paint);
    LabelBlock b = new LabelBlock("Title", font, paint);

    b.setContentAlignmentPoint(TextBlockAnchor.BOTTOM_RIGHT);

    assertFalse(a.equals(b));
  }

  @Test
  public void equals_differsByToolTip_isFalse() {
    Font font = new Font("Dialog", Font.PLAIN, 11);
    Paint paint = Color.BLACK;

    LabelBlock a = new LabelBlock("Title", font, paint);
    LabelBlock b = new LabelBlock("Title", font, paint);

    a.setToolTipText("tip");
    assertFalse(a.equals(b));
  }

  @Test
  public void equals_differsByUrl_isFalse() {
    Font font = new Font("Dialog", Font.PLAIN, 11);
    Paint paint = Color.BLACK;

    LabelBlock a = new LabelBlock("Title", font, paint);
    LabelBlock b = new LabelBlock("Title", font, paint);

    a.setURLText("https://example.com");
    assertFalse(a.equals(b));
  }

  @Test
  public void draw_withParams_returnsNull() {
    LabelBlock block = new LabelBlock("Draw me");
    Graphics2D g2 = newGraphics();
    Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 40);

    Object result = block.draw(g2, area, null);

    assertNull(result);
  }

  @Test
  public void arrange_withNoConstraints_returnsNonZeroSize_forNonEmptyText() {
    LabelBlock block = new LabelBlock("Measure me");
    Graphics2D g2 = newGraphics();

    Size2D size = block.arrange(g2, RectangleConstraint.NONE);

    assertNotNull(size);
    assertTrue("width should be > 0", size.getWidth() > 0.0);
    assertTrue("height should be > 0", size.getHeight() > 0.0);
  }

  @Test
  public void arrange_rejectsNullConstraint() {
    LabelBlock block = new LabelBlock("text");
    Graphics2D g2 = newGraphics();

    try {
      block.arrange(g2, null);
      fail("Expected NullPointerException/IllegalArgumentException for null constraint");
    } catch (NullPointerException | IllegalArgumentException expected) {
      // ok
    }
  }

  @Test
  public void clone_producesEqualButDistinctInstance() throws CloneNotSupportedException {
    LabelBlock original = new LabelBlock("Clone me");
    original.setToolTipText("tip");
    original.setURLText("url");
    original.setContentAlignmentPoint(TextBlockAnchor.TOP_RIGHT);
    original.setTextAnchor(RectangleAnchor.TOP_RIGHT);

    LabelBlock copy = (LabelBlock) original.clone();

    assertNotSame(original, copy);
    assertEquals(original, copy);

    // Mutating the copy shouldn't affect the original
    copy.setToolTipText("changed");
    assertNotEquals(original, copy);
    assertEquals("tip", original.getToolTipText());
  }
}