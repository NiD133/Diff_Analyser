package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.GraphicsState;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.MarkedContentInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TextRenderInfoTest {

    private GraphicsState defaultGraphicsState;
    private CMapAwareDocumentFont defaultFont;
    private Matrix defaultMatrix;
    private Stack<MarkedContentInfo> emptyMarkedContentStack;
    private PdfString samplePdfString;

    @Before
    public void setUp() {
        // Set up common test objects
        defaultGraphicsState = new GraphicsState();
        PdfGState pdfGState = new PdfGState();
        defaultFont = new CMapAwareDocumentFont(pdfGState);
        defaultGraphicsState.font = defaultFont;
        defaultMatrix = defaultGraphicsState.getCtm();
        emptyMarkedContentStack = new Stack<MarkedContentInfo>();
        samplePdfString = new PdfDate(); // Using PdfDate as sample PdfString
    }

    @Test
    public void testGetCharacterRenderInfos_WithZeroHorizontalScaling_ReturnsCorrectCount() {
        // Given: Graphics state with zero horizontal scaling
        defaultGraphicsState.horizontalScaling = 0.0F;
        
        // When: Creating TextRenderInfo and getting character render infos
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        List<TextRenderInfo> characterInfos = textRenderInfo.getCharacterRenderInfos();
        
        // Then: Should return 9 character render infos (length of PdfDate string representation)
        assertEquals("Should return 9 character render infos for PdfDate string", 9, characterInfos.size());
    }

    @Test
    public void testGetCharacterRenderInfos_WithCharacterSpacing_ReturnsCorrectCount() {
        // Given: Graphics state with character spacing
        defaultGraphicsState.characterSpacing = 7.0F;
        
        // When: Creating TextRenderInfo and getting character render infos
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        List<TextRenderInfo> characterInfos = textRenderInfo.getCharacterRenderInfos();
        
        // Then: Should return 9 character render infos and not contain the parent TextRenderInfo
        assertEquals("Should return 9 character render infos", 9, characterInfos.size());
        assertFalse("Character infos should not contain parent TextRenderInfo", 
                   characterInfos.contains(textRenderInfo));
    }

    @Test
    public void testGetCharacterRenderInfos_WithCustomFontSize_ReturnsCorrectCount() {
        // Given: Graphics state with custom font size
        defaultGraphicsState.fontSize = 4.0F;
        
        // When: Creating TextRenderInfo and getting character render infos
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        List<TextRenderInfo> characterInfos = textRenderInfo.getCharacterRenderInfos();
        
        // Then: Should return 9 character render infos
        assertEquals("Should return 9 character render infos", 9, characterInfos.size());
        assertFalse("Character infos should not contain parent TextRenderInfo", 
                   characterInfos.contains(textRenderInfo));
    }

    @Test
    public void testGetSingleSpaceWidth_WithZeroHorizontalScaling_ReturnsZero() {
        // Given: Graphics state with zero horizontal scaling
        defaultGraphicsState.horizontalScaling = 0.0F;
        
        // When: Creating TextRenderInfo and getting single space width
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        float spaceWidth = textRenderInfo.getSingleSpaceWidth();
        
        // Then: Should return 0.0 for zero horizontal scaling
        assertEquals("Single space width should be 0.0 with zero horizontal scaling", 
                    0.0F, spaceWidth, 0.01F);
    }

    @Test
    public void testGetSingleSpaceWidth_WithCharacterSpacing_ReturnsCharacterSpacing() {
        // Given: Graphics state with character spacing and custom matrix
        defaultGraphicsState.characterSpacing = 1025.7177F;
        Matrix customMatrix = new Matrix(2766.3F, 1543.6833F);
        
        // When: Creating TextRenderInfo and getting single space width
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          customMatrix, emptyMarkedContentStack);
        float spaceWidth = textRenderInfo.getSingleSpaceWidth();
        
        // Then: Should return the character spacing value
        assertEquals("Single space width should equal character spacing", 
                    1025.7175F, spaceWidth, 0.01F);
    }

    @Test
    public void testGetSingleSpaceWidth_WithWordSpacing_ReturnsWordSpacing() {
        // Given: Graphics state with word spacing
        defaultGraphicsState.wordSpacing = 87.0F;
        
        // When: Creating TextRenderInfo and getting single space width
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        float spaceWidth = textRenderInfo.getSingleSpaceWidth();
        
        // Then: Should return the word spacing value
        assertEquals("Single space width should equal word spacing", 87.0F, spaceWidth, 0.01F);
    }

    @Test
    public void testGetRise_WithNegativeRise_ReturnsAbsoluteValue() {
        // Given: Graphics state with negative rise value
        defaultGraphicsState.rise = -3505.32F;
        
        // When: Creating TextRenderInfo and getting rise
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        float rise = textRenderInfo.getRise();
        
        // Then: Should return absolute value of rise
        assertEquals("Rise should return absolute value", 3505.32F, rise, 0.01F);
    }

    @Test
    public void testGetAscentLine_WithZeroHorizontalScaling_ReturnsValidLineSegment() {
        // Given: Graphics state with zero horizontal scaling
        defaultGraphicsState.horizontalScaling = 0.0F;
        
        // When: Creating TextRenderInfo and getting ascent line
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        LineSegment ascentLine = textRenderInfo.getAscentLine();
        
        // Then: Should return a valid line segment
        assertNotNull("Ascent line should not be null", ascentLine);
    }

    @Test
    public void testGetAscentLine_WithSpaceCharacterAndWordSpacing_ReturnsValidLineSegment() {
        // Given: Graphics state with word spacing and space character
        defaultGraphicsState.wordSpacing = 1.0F;
        PdfString spaceString = new PdfString(" ", "Cp1252");
        
        // When: Creating TextRenderInfo and getting ascent line
        TextRenderInfo textRenderInfo = new TextRenderInfo(spaceString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        LineSegment ascentLine = textRenderInfo.getAscentLine();
        
        // Then: Should return a valid line segment
        assertNotNull("Ascent line should not be null for space character", ascentLine);
    }

    @Test
    public void testGetDescentLine_WithPositiveRise_ReturnsValidLineSegment() {
        // Given: Graphics state with positive rise
        defaultGraphicsState.rise = 1.0F;
        Matrix customMatrix = new Matrix(6, -1301.7483F);
        
        // When: Creating TextRenderInfo and getting descent line
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          customMatrix, emptyMarkedContentStack);
        LineSegment descentLine = textRenderInfo.getDescentLine();
        
        // Then: Should return a valid line segment
        assertNotNull("Descent line should not be null", descentLine);
    }

    @Test
    public void testGetBaseline_ReturnsValidLineSegment() {
        // Given: Graphics state with rise value
        defaultGraphicsState.rise = 2.0F;
        
        // When: Creating TextRenderInfo and getting baseline
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        LineSegment baseline = textRenderInfo.getBaseline();
        
        // Then: Should return a valid line segment
        assertNotNull("Baseline should not be null", baseline);
    }

    @Test
    public void testGetUnscaledBaseline_ReturnsValidLineSegment() {
        // Given: Graphics state with rise value
        defaultGraphicsState.rise = 2.0F;
        
        // When: Creating TextRenderInfo and getting unscaled baseline
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        LineSegment unscaledBaseline = textRenderInfo.getUnscaledBaseline();
        
        // Then: Should return a valid line segment
        assertNotNull("Unscaled baseline should not be null", unscaledBaseline);
    }

    @Test
    public void testHasMcid_WithMarkedContentButDifferentId_ReturnsFalse() {
        // Given: Marked content stack with specific marked content info
        Stack<MarkedContentInfo> markedContentStack = new Stack<MarkedContentInfo>();
        PdfGState pdfGState = new PdfGState();
        MarkedContentInfo markedContentInfo = new MarkedContentInfo(pdfGState.BM_DIFFERENCE, pdfGState);
        markedContentStack.add(markedContentInfo);
        markedContentStack.add(markedContentInfo);
        
        // When: Creating TextRenderInfo and checking for different MCID
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, markedContentStack);
        boolean hasMcid = textRenderInfo.hasMcid(1046, true);
        
        // Then: Should return false for non-matching MCID
        assertFalse("Should return false for non-matching MCID", hasMcid);
    }

    @Test
    public void testGetUnscaledWidth_WithNoCharacterSpacing_ReturnsZero() {
        // Given: Default graphics state with custom matrix
        Matrix customMatrix = new Matrix(2, 4);
        
        // When: Creating TextRenderInfo and getting unscaled width
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          customMatrix, emptyMarkedContentStack);
        float unscaledWidth = textRenderInfo.getUnscaledWidth();
        
        // Then: Should return 0.0 for no character spacing
        assertEquals("Unscaled width should be 0.0 with no character spacing", 
                    0.0F, unscaledWidth, 0.01F);
    }

    @Test
    public void testGetUnscaledWidth_WithCharacterSpacing_ReturnsCalculatedWidth() {
        // Given: Graphics state with character spacing
        defaultGraphicsState.characterSpacing = 7.0F;
        
        // When: Creating TextRenderInfo and getting unscaled width
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        float unscaledWidth = textRenderInfo.getUnscaledWidth();
        
        // Then: Should return calculated width based on character spacing
        assertEquals("Unscaled width should be calculated from character spacing", 
                    63.0F, unscaledWidth, 0.01F);
    }

    @Test
    public void testGetTextRenderMode_WithCustomRenderMode_ReturnsCorrectMode() {
        // Given: Graphics state with custom render mode
        defaultGraphicsState.renderMode = 9;
        
        // When: Creating TextRenderInfo and getting text render mode
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        int renderMode = textRenderInfo.getTextRenderMode();
        
        // Then: Should return the custom render mode
        assertEquals("Should return custom render mode", 9, renderMode);
    }

    @Test
    public void testGetSingleSpaceWidth_WithNegativeFontSize_ReturnsAbsoluteValue() {
        // Given: Graphics state with negative font size
        defaultGraphicsState.fontSize = -744.2958F;
        Matrix customMatrix = new Matrix(2, 4);
        
        // When: Creating TextRenderInfo and getting single space width
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          customMatrix, emptyMarkedContentStack);
        float spaceWidth = textRenderInfo.getSingleSpaceWidth();
        
        // Then: Should return absolute value of font size
        assertEquals("Should return absolute value of negative font size", 
                    744.2958F, spaceWidth, 0.01F);
    }

    @Test
    public void testGetPdfString_WithNullPdfString_ReturnsNull() {
        // Given: TextRenderInfo created with null PdfString
        TextRenderInfo textRenderInfo = new TextRenderInfo(null, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        
        // When: Getting PDF string
        PdfString pdfString = textRenderInfo.getPdfString();
        
        // Then: Should return null
        assertNull("Should return null for null PdfString input", pdfString);
    }

    @Test
    public void testGetPdfString_WithValidPdfString_ReturnsValidPdfString() {
        // Given: PdfDate with hex writing enabled
        PdfDate pdfDate = new PdfDate();
        pdfDate.setHexWriting(true);
        
        // When: Creating TextRenderInfo and getting PDF string
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfDate, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        PdfString pdfString = textRenderInfo.getPdfString();
        
        // Then: Should return valid PDF string that is not indirect
        assertFalse("PDF string should not be indirect", pdfString.isIndirect());
    }

    @Test
    public void testGetFont_ReturnsValidDocumentFont() {
        // Given: Default setup with CMapAwareDocumentFont
        
        // When: Creating TextRenderInfo and getting font
        TextRenderInfo textRenderInfo = new TextRenderInfo(null, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        defaultFont.setSubset(false);
        
        // Then: Should return valid DocumentFont
        assertNotNull("Should return valid DocumentFont", textRenderInfo.getFont());
    }

    @Test
    public void testGetFillColor_WithGrayColor_ReturnsCorrectColor() {
        // Given: Graphics state with gray fill color
        defaultGraphicsState.fillColor = BaseColor.GRAY;
        
        // When: Creating TextRenderInfo and getting fill color
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        BaseColor fillColor = textRenderInfo.getFillColor();
        
        // Then: Should return gray color with correct RGB value
        assertEquals("Should return gray color with correct RGB", -8355712, fillColor.getRGB());
    }

    @Test
    public void testGetFillColor_WithNullFillColor_ReturnsNull() {
        // Given: Graphics state with null fill color (default)
        
        // When: Creating TextRenderInfo and getting fill color
        TextRenderInfo textRenderInfo = new TextRenderInfo(null, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        BaseColor fillColor = textRenderInfo.getFillColor();
        
        // Then: Should return null
        assertNull("Should return null for null fill color", fillColor);
    }

    @Test
    public void testGetStrokeColor_WithDefaultState_ReturnsNull() {
        // Given: Default graphics state (no stroke color set)
        
        // When: Creating TextRenderInfo and getting stroke color
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        BaseColor strokeColor = textRenderInfo.getStrokeColor();
        
        // Then: Should return null for default state
        assertNull("Should return null for default stroke color", strokeColor);
    }

    @Test
    public void testGetMcid_WithEmptyMarkedContentStack_ReturnsNull() {
        // Given: Empty marked content stack
        
        // When: Creating TextRenderInfo and getting MCID
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        Integer mcid = textRenderInfo.getMcid();
        
        // Then: Should return null for empty stack
        assertNull("Should return null for empty marked content stack", mcid);
    }

    @Test
    public void testGetRise_WithZeroRise_ReturnsZero() {
        // Given: Graphics state with zero rise (default)
        PdfString emptyPdfString = new PdfString(new byte[2]);
        
        // When: Creating TextRenderInfo and getting rise
        TextRenderInfo textRenderInfo = new TextRenderInfo(emptyPdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        float rise = textRenderInfo.getRise();
        
        // Then: Should return 0.0 for zero rise
        assertEquals("Should return 0.0 for zero rise", 0.0F, rise, 0.01F);
    }

    @Test
    public void testGetRise_WithPositiveRise_ReturnsCorrectValue() {
        // Given: Graphics state with positive rise
        defaultGraphicsState.rise = 12.0F;
        PdfString emptyPdfString = new PdfString();
        
        // When: Creating TextRenderInfo and getting rise
        TextRenderInfo textRenderInfo = new TextRenderInfo(emptyPdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        float rise = textRenderInfo.getRise();
        
        // Then: Should return the positive rise value
        assertEquals("Should return positive rise value", 12.0F, rise, 0.01F);
    }

    @Test
    public void testHasMcid_WithEmptyMarkedContentStack_ReturnsFalse() {
        // Given: Empty marked content stack
        
        // When: Creating TextRenderInfo and checking for MCID
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        boolean hasMcid = textRenderInfo.hasMcid(32);
        
        // Then: Should return false for empty stack
        assertFalse("Should return false for empty marked content stack", hasMcid);
    }

    @Test
    public void testGetText_CachesResult() {
        // Given: TextRenderInfo with valid PdfString
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        
        // When: Getting text multiple times
        textRenderInfo.getText(); // First call to cache the result
        String cachedText = textRenderInfo.getText(); // Second call should use cached result
        
        // Then: Should return empty string for PdfDate (which has empty text representation)
        assertEquals("Should return empty string for PdfDate", "", cachedText);
    }

    // Exception handling tests
    @Test(expected = NullPointerException.class)
    public void testHasMcid_WithNullMarkedContentInfo_ThrowsNullPointerException() {
        // Given: Marked content stack with null entry
        Stack<MarkedContentInfo> stackWithNull = new Stack<MarkedContentInfo>();
        stackWithNull.add(null);
        
        // When: Creating TextRenderInfo and checking MCID
        TextRenderInfo textRenderInfo = new TextRenderInfo(samplePdfString, defaultGraphicsState, 
                                                          defaultMatrix, stackWithNull);
        
        // Then: Should throw NullPointerException
        textRenderInfo.hasMcid(2, false);
    }

    @Test(expected = NullPointerException.class)
    public void testGetUnscaledWidth_WithNullPdfString_ThrowsNullPointerException() {
        // Given: TextRenderInfo with null PdfString
        TextRenderInfo textRenderInfo = new TextRenderInfo(null, defaultGraphicsState, 
                                                          defaultMatrix, emptyMarkedContentStack);
        
        // When: Getting unscaled width
        // Then: Should throw NullPointerException
        textRenderInfo.getUnscaledWidth();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_WithNullMarkedContentCollection_ThrowsNullPointerException() {
        // Given: Null marked content collection
        
        // When: Creating TextRenderInfo with null collection
        // Then: Should throw NullPointerException
        new TextRenderInfo(samplePdfString, defaultGraphicsState, defaultMatrix, null);
    }
}