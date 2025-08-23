package org.locationtech.spatial4j.io;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Circle;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Readable, behavior-focused tests for WKTWriter.
 * 
 * These tests intentionally avoid obscure inputs and implementation details
 * to emphasize the contract of WKTWriter:
 * - format name
 * - default number formatting
 * - writing of common shapes (Point, Circle, Rectangle, GeometryCollection)
 * - honoring a provided NumberFormat via the protected append method
 * - basic error handling
 */
public class WKTWriterTest {

  private SpatialContext ctx;
  private WKTWriter writer;

  @Before
  public void setUp() {
    ctx = SpatialContext.GEO;
    writer = new WKTWriter();
  }

  @Test
  public void formatName_isWKT() {
    assertEquals("WKT", writer.getFormatName());
  }

  @Test
  public void defaultNumberFormat_matchesExpectedPattern() {
    DecimalFormat df = (DecimalFormat) writer.getNumberFormat();
    assertEquals("###0.######", df.toLocalizedPattern());
  }

  @Test
  public void append_usesProvidedNumberFormat() {
    // Use US symbols to avoid locale-dependent decimal separators.
    DecimalFormat nf = new DecimalFormat("#0.0", DecimalFormatSymbols.getInstance(Locale.US));
    Point p = ctx.getShapeFactory().pointXY(3.14159, -45.5);

    StringBuilder sb = new StringBuilder();
    writer.append(sb, p, nf);

    // Expect 1 decimal per the format
    assertEquals("3.1 -45.5", sb.toString());
  }

  @Test
  public void toString_point_printsCoordinates() {
    Point p = ctx.getShapeFactory().pointXY(12.5, -7.25);
    assertEquals("POINT (12.5 -7.25)", writer.toString(p));
  }

  @Test
  public void toString_circle_printsBufferExpression() {
    Point center = ctx.getShapeFactory().pointXY(1.0, 2.0);
    Circle circle = ctx.getShapeFactory().circle(center, 3.0);
    // Default number format is up to 6 decimals, no trailing zeros
    assertEquals("BUFFER (POINT (1 2), 3)", writer.toString(circle));
  }

  @Test
  public void toString_rectangle_printsEnvelope() {
    // World bounds for GEO: (-180, 180, -90, 90)
    Rectangle world = ctx.getWorldBounds();
    assertEquals("ENVELOPE (-180, 180, -90, 90)", writer.toString(world));
  }

  @Test
  public void toString_emptyGeometryCollection_printsEmpty() {
    Shape emptyCollection = new ShapeCollection<>(Collections.emptyList(), ctx);
    assertEquals("GEOMETRYCOLLECTION EMPTY", writer.toString(emptyCollection));
  }

  @Test
  public void write_writesSameAsToString() throws IOException {
    Point p = ctx.getShapeFactory().pointXY(10, 20);
    String expected = writer.toString(p);

    StringWriter out = new StringWriter();
    writer.write(out, p);

    assertEquals(expected, out.toString());
  }

  @Test
  public void toString_nullShape_throwsNpe() {
    assertThrows(NullPointerException.class, () -> writer.toString(null));
  }

  @Test
  public void append_nullPoint_throwsNpe() {
    NumberFormat nf = writer.getNumberFormat();
    assertThrows(NullPointerException.class, () -> writer.append(new StringBuilder(), null, nf));
  }

  @Test
  public void write_nullWriter_throwsNpe() {
    Point p = ctx.getShapeFactory().pointXY(0, 0);
    assertThrows(NullPointerException.class, () -> writer.write((Writer) null, p));
  }
}