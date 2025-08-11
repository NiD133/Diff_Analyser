package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Readable, focused tests for TextRenderInfo.
 *
 * These tests aim to validate the core, stable behavior of TextRenderInfo using simple,
 * well-named scenarios. They avoid relying on environment-specific behavior (e.g. charset
 * availability) and avoid assertions on incidental implementation details.
 */
public class TextRenderInfoTest {

    private GraphicsState gs;
    private Matrix textMatrix;

    @Before
    public void setUp() {
        // Minimal, valid graphics state: ensure a usable font is present.
        gs = new GraphicsState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(new PdfGState());
        gs.font = font;

        // Use the current transformation matrix from the graphics state for consistency.
        textMatrix = gs.getCtm();
    }

    private TextRenderInfo newTRI(PdfString string) {
        return new TextRenderInfo(string, gs, textMatrix, new ArrayList<MarkedContentInfo>());
    }

    // --------------------------------------------------------------------------------------------
    // Construction
    // --------------------------------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void constructor_nullMarkedContentCollection_throwsNPE() {
        new TextRenderInfo(new PdfString(), gs, textMatrix, null /* markedContentInfos */);
    }

    // --------------------------------------------------------------------------------------------
    // Fonts and colors
    // --------------------------------------------------------------------------------------------

    @Test
    public void getFont_returnsFontFromGraphicsState() {
        TextRenderInfo tri = newTRI(new PdfString());
        DocumentFont font = tri.getFont();

        assertNotNull("DocumentFont should be available", font);
        assertSame("Font instance should match graphics state's font", gs.font, font);
    }

    @Test
    public void fillAndStrokeColors_areExposedFromGraphicsState() {
        gs.fillColor = BaseColor.GRAY; // set only fill color
        TextRenderInfo tri = newTRI(new PdfString());

        assertEquals("Fill color should reflect graphics state", BaseColor.GRAY, tri.getFillColor());
        assertNull("Stroke color should be null by default", tri.getStrokeColor());
    }

    // --------------------------------------------------------------------------------------------
    // Text render mode
    // --------------------------------------------------------------------------------------------

    @Test
    public void getTextRenderMode_defaultsToFillText() {
        TextRenderInfo tri = newTRI(new PdfString());
        assertEquals("Default render mode is 0 (Fill text)", 0, tri.getTextRenderMode());
    }

    @Test
    public void getTextRenderMode_reflectsGraphicsState() {
        gs.renderMode = 9; // arbitrary non-default value
        TextRenderInfo tri = newTRI(new PdfString());
        assertEquals(9, tri.getTextRenderMode());
    }

    // --------------------------------------------------------------------------------------------
    // Lines and positions
    // --------------------------------------------------------------------------------------------

    @Test
    public void baseline_ascent_descent_and_unscaledBaseline_areAvailable() {
        TextRenderInfo tri = newTRI(new PdfString());

        LineSegment baseline = tri.getBaseline();
        LineSegment ascent = tri.getAscentLine();
        LineSegment descent = tri.getDescentLine();
        LineSegment unscaledBaseline = tri.getUnscaledBaseline();

        assertNotNull(baseline);
        assertNotNull(ascent);
        assertNotNull(descent);
        assertNotNull(unscaledBaseline);

        // The lines represent different metrics; they should not all be equal.
        assertFalse("Ascent line should differ from baseline", ascent.equals(baseline));
    }

    @Test
    public void rise_isNonNegativeMagnitudeOfGraphicsStateRise() {
        gs.rise = -12.5f;
        TextRenderInfo triNegative = newTRI(new PdfString());
        assertEquals("Rise should be the magnitude of gs.rise", Math.abs(gs.rise), triNegative.getRise(), 0.01f);

        gs.rise = 7.0f;
        TextRenderInfo triPositive = newTRI(new PdfString());
        assertEquals("Rise should be the magnitude of gs.rise", Math.abs(gs.rise), triPositive.getRise(), 0.01f);
    }

    // --------------------------------------------------------------------------------------------
    // Widths and spacing
    // --------------------------------------------------------------------------------------------

    @Test
    public void getSingleSpaceWidth_isZeroWhenHorizontalScalingIsZero() {
        gs.horizontalScaling = 0.0f;
        TextRenderInfo tri = newTRI(new PdfString());
        assertEquals(0.0f, tri.getSingleSpaceWidth(), 0.01f);
    }

    @Test
    public void getSingleSpaceWidth_returnsWordSpacingWhenSet() {
        gs.wordSpacing = 10.0f;
        TextRenderInfo tri = newTRI(new PdfString());
        assertEquals(10.0f, tri.getSingleSpaceWidth(), 0.01f);
    }

    @Test
    public void getUnscaledWidth_isZeroForEmptyPdfString() {
        TextRenderInfo tri = newTRI(new PdfString()); // empty string
        assertEquals(0.0f, tri.getUnscaledWidth(), 0.01f);
    }

    // --------------------------------------------------------------------------------------------
    // Text extraction and glyphs
    // --------------------------------------------------------------------------------------------

    @Test
    public void getText_isStableAcrossMultipleCalls() {
        TextRenderInfo tri = newTRI(new PdfString(" "));
        String first = tri.getText();
        String second = tri.getText();

        assertNotNull(first);
        assertEquals("Subsequent calls should return the same text", first, second);
    }

    @Test
    public void getCharacterRenderInfos_returnsOneEntryPerDecodedGlyph() {
        TextRenderInfo tri = newTRI(new PdfString()); // empty -> zero glyphs
        List<TextRenderInfo> perGlyph = tri.getCharacterRenderInfos();

        assertNotNull(perGlyph);
        assertFalse("Glyph list should not contain the parent instance", perGlyph.contains(tri));
        assertEquals("For empty text, there should be no glyph entries", 0, perGlyph.size());
    }

    // --------------------------------------------------------------------------------------------
    // Marked content (MCID)
    // --------------------------------------------------------------------------------------------

    @Test
    public void hasMcid_isFalseAndMcidIsNull_whenNoMarkedContent() {
        TextRenderInfo tri = newTRI(new PdfString());

        assertFalse(tri.hasMcid(1));
        assertFalse(tri.hasMcid(1, true));
        assertNull(tri.getMcid());
    }

    // --------------------------------------------------------------------------------------------
    // PDF string handling
    // --------------------------------------------------------------------------------------------

    @Test
    public void getPdfString_returnsSameInstancePassedToConstructor() {
        PdfString pdfString = new PdfString("hello");
        TextRenderInfo tri = newTRI(pdfString);
        assertSame(pdfString, tri.getPdfString());
    }

    @Test
    public void getPdfString_isNullWhenConstructedWithNull() {
        TextRenderInfo tri = new TextRenderInfo(null, gs, textMatrix, new ArrayList<MarkedContentInfo>());
        assertNull(tri.getPdfString());
    }
}