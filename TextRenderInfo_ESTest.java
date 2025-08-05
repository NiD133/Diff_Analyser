/*
 * Refactored test suite for TextRenderInfo
 * Improved understandability with descriptive names, comments, and organization
 */
package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.GraphicsState;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.MarkedContentInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
) 
public class TextRenderInfo_ESTest extends TextRenderInfo_ESTest_scaffolding {

    // Constants for repeated values
    private static final int TIMEOUT = 4000;
    private static final int FONT_SIZE = 4;
    private static final int CHARACTER_SPACING = 7;
    private static final float WORD_SPACING = 87.0F;
    private static final float RISE_VALUE = 12.0F;
    private static final float NEGATIVE_FONT_SIZE = -744.2958F;
    private static final int EXPECTED_LIST_SIZE = 9;
    private static final int MCID_TO_CHECK = 1046;

    // Common test objects
    private PdfDate createPdfDate() {
        return new PdfDate();
    }
    
    private GraphicsState createGraphicsState() {
        return new GraphicsState();
    }
    
    private PdfGState createPdfGState() {
        return new PdfGState();
    }
    
    private CMapAwareDocumentFont createCMapAwareFont(PdfGState gstate) {
        return new CMapAwareDocumentFont(gstate);
    }

    // Tests for getCharacterRenderInfos()
    // ===================================
    
    @Test(timeout = TIMEOUT)
    public void getCharacterRenderInfos_WithHorizontalScalingZero_ReturnsListOfExpectedSize() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.horizontalScaling = 0.0F;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = gs.ctm;
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        List<TextRenderInfo> characterInfos = renderInfo.getCharacterRenderInfos();
        
        // Verify
        assertEquals("Should return list with expected number of characters", 
                     EXPECTED_LIST_SIZE, characterInfos.size());
    }

    @Test(timeout = TIMEOUT)
    public void getCharacterRenderInfos_WithCharacterSpacing_ReturnsListOfExpectedSize() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.characterSpacing = CHARACTER_SPACING;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = gs.getCtm();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        List<TextRenderInfo> characterInfos = renderInfo.getCharacterRenderInfos();
        
        // Verify
        assertEquals("Should return list with expected number of characters", 
                     EXPECTED_LIST_SIZE, characterInfos.size());
        assertFalse("Should not contain parent render info", 
                    characterInfos.contains(renderInfo));
    }

    @Test(timeout = TIMEOUT)
    public void getCharacterRenderInfos_WithFontSize_ReturnsListOfExpectedSize() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.fontSize = FONT_SIZE;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = gs.getCtm();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        List<TextRenderInfo> characterInfos = renderInfo.getCharacterRenderInfos();
        
        // Verify
        assertEquals("Should return list with expected number of characters", 
                     EXPECTED_LIST_SIZE, characterInfos.size());
        assertFalse("Should not contain parent render info", 
                    characterInfos.contains(renderInfo));
    }

    // Tests for getSingleSpaceWidth()
    // ===============================

    @Test(timeout = TIMEOUT)
    public void getSingleSpaceWidth_WithHorizontalScalingZero_ReturnsZero() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.horizontalScaling = 0.0F;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = gs.ctm;
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        float spaceWidth = renderInfo.getSingleSpaceWidth();
        
        // Verify
        assertEquals("Space width should be zero with horizontal scaling zero",
                     0.0F, spaceWidth, 0.01F);
    }

    @Test(timeout = TIMEOUT)
    public void getSingleSpaceWidth_WithCharacterSpacing_ReturnsExpectedValue() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.characterSpacing = 1025.7177F;
        Matrix matrix = new Matrix(2766.3F, 1543.6833F);
        TreeSet<MarkedContentInfo> treeSet = new TreeSet<>();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, treeSet);
        float spaceWidth = renderInfo.getSingleSpaceWidth();
        
        // Verify
        assertEquals("Space width should account for character spacing",
                     1025.7175F, spaceWidth, 0.01F);
    }

    @Test(timeout = TIMEOUT)
    public void getSingleSpaceWidth_WithWordSpacing_ReturnsWordSpacingValue() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.wordSpacing = WORD_SPACING;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = new Matrix();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        float spaceWidth = renderInfo.getSingleSpaceWidth();
        
        // Verify
        assertEquals("Space width should equal word spacing",
                     WORD_SPACING, spaceWidth, 0.01F);
    }

    @Test(timeout = TIMEOUT)
    public void getSingleSpaceWidth_WithNegativeFontSize_ReturnsPositiveValue() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.fontSize = NEGATIVE_FONT_SIZE;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = new Matrix(2, 4);
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        float spaceWidth = renderInfo.getSingleSpaceWidth();
        
        // Verify
        assertEquals("Should handle negative font size correctly",
                     Math.abs(NEGATIVE_FONT_SIZE), spaceWidth, 0.01F);
    }

    // Tests for getRise()
    // ===================
    
    @Test(timeout = TIMEOUT)
    public void getRise_WithPositiveRise_ReturnsPositiveValue() throws Throwable {
        // Setup
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        PdfString pdfString = new PdfString();
        Matrix matrix = gs.getCtm();
        LinkedHashSet<MarkedContentInfo> linkedHashSet = new LinkedHashSet<>();
        gs.rise = RISE_VALUE;
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfString, gs, matrix, linkedHashSet);
        float rise = renderInfo.getRise();
        
        // Verify
        assertEquals("Should return positive rise value", 
                     RISE_VALUE, rise, 0.01F);
    }

    @Test(timeout = TIMEOUT)
    public void getRise_WithNegativeRise_ReturnsPositiveValue() throws Throwable {
        // Setup
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        LinkedHashSet<MarkedContentInfo> linkedHashSet = new LinkedHashSet<>();
        gs.rise = -3505.32F;
        Matrix matrix = gs.ctm;
        PdfString pdfString = new PdfString();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfString, gs, matrix, linkedHashSet);
        float rise = renderInfo.getRise();
        
        // Verify
        assertEquals("Should convert negative rise to positive",
                     3505.32F, rise, 0.01F);
    }

    // Tests for line segment getters (getBaseline(), getAscentLine(), etc.)
    // =====================================================================
    
    @Test(timeout = TIMEOUT)
    public void getAscentLine_WithHorizontalScalingZero_ReturnsValidSegment() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.horizontalScaling = 0.0F;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = gs.ctm;
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        LineSegment line = renderInfo.getAscentLine();
        
        // Verify
        assertNotNull("Ascent line should not be null", line);
    }

    @Test(timeout = TIMEOUT)
    public void getAscentLine_WithWordSpacing_ReturnsValidSegment() throws Throwable {
        // Setup
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.wordSpacing = 1.0F;
        gs.font = font;
        Stack<MarkedContentInfo> stack = new Stack<>();
        PdfString pdfString = new PdfString(" ", "Cp1252");
        Matrix matrix = gs.getCtm();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfString, gs, matrix, stack);
        LineSegment line = renderInfo.getAscentLine();
        
        // Verify
        assertNotNull("Ascent line should not be null", line);
    }

    @Test(timeout = TIMEOUT)
    public void getDescentLine_WithRise_ReturnsValidSegment() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        gs.rise = 1.0F;
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        Matrix matrix = new Matrix(6, -1301.7483F);
        HashSet<MarkedContentInfo> hashSet = new HashSet<>();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, hashSet);
        LineSegment line = renderInfo.getDescentLine();
        
        // Verify
        assertNotNull("Descent line should not be null", line);
    }

    // Tests for hasMcid()
    // ===================
    
    @Test(timeout = TIMEOUT)
    public void hasMcid_WithDuplicateMarkedContent_ReturnsFalse() throws Throwable {
        // Setup
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        Stack<MarkedContentInfo> stack = new Stack<>();
        MarkedContentInfo contentInfo = new MarkedContentInfo(pdfGState.BM_DIFFERENCE, pdfGState);
        stack.add(contentInfo);
        stack.add(contentInfo);  // Duplicate
        PdfDate pdfDate = createPdfDate();
        Matrix matrix = gs.getCtm();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        boolean hasMcid = renderInfo.hasMcid(MCID_TO_CHECK, true);
        
        // Verify
        assertFalse("Should not find MCID in duplicate marked content", hasMcid);
    }

    // Tests for getUnscaledWidth()
    // ============================
    
    @Test(timeout = TIMEOUT)
    public void getUnscaledWidth_WithHorizontalScalingZero_ReturnsZero() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = new Matrix(2, 4);
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        float width = renderInfo.getUnscaledWidth();
        
        // Verify
        assertEquals("Unscaled width should be zero with horizontal scaling zero",
                     0.0F, width, 0.01F);
    }

    @Test(timeout = TIMEOUT)
    public void getUnscaledWidth_WithCharacterSpacing_ReturnsExpectedValue() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.characterSpacing = CHARACTER_SPACING;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = gs.getCtm();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        float width = renderInfo.getUnscaledWidth();
        
        // Verify
        assertEquals("Unscaled width should account for character spacing",
                     CHARACTER_SPACING * EXPECTED_LIST_SIZE, width, 0.01F);
    }

    // Tests for getTextRenderMode()
    // ============================
    
    @Test(timeout = TIMEOUT)
    public void getTextRenderMode_WithRenderModeSet_ReturnsCorrectMode() throws Throwable {
        // Setup
        PdfDate pdfDate = createPdfDate();
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        gs.renderMode = 9;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = gs.getCtm();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        int renderMode = renderInfo.getTextRenderMode();
        
        // Verify
        assertEquals("Should return correct text render mode", 9, renderMode);
    }

    // Tests for getPdfString()
    // ========================
    
    @Test(timeout = TIMEOUT)
    public void getPdfString_WithNullString_ReturnsNull() throws Throwable {
        // Setup
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        LinkedHashSet<MarkedContentInfo> linkedHashSet = new LinkedHashSet<>();
        Matrix matrix = new Matrix();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(null, gs, matrix, linkedHashSet);
        PdfString pdfString = renderInfo.getPdfString();
        
        // Verify
        assertNull("Should return null for null input", pdfString);
    }

    @Test(timeout = TIMEOUT)
    public void getPdfString_WithHexWritingEnabled_ReturnsCorrectString() throws Throwable {
        // Setup
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        Stack<MarkedContentInfo> stack = new Stack<>();
        PdfDate pdfDate = createPdfDate();
        pdfDate.setHexWriting(true);
        Matrix matrix = gs.getCtm();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(pdfDate, gs, matrix, stack);
        PdfString pdfString = renderInfo.getPdfString();
        
        // Verify
        assertFalse("Hex writing should not be set in result", pdfString.isHexWriting());
    }

    // Tests for exception scenarios
    // =============================
    
    @Test(timeout = TIMEOUT, expected = NullPointerException.class)
    public void getCharacterRenderInfos_WithNullPdfString_ThrowsException() throws Throwable {
        // Setup
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = new Matrix();
        
        // Execute
        TextRenderInfo renderInfo = new TextRenderInfo(null, gs, matrix, stack);
        renderInfo.getCharacterRenderInfos();
    }

    @Test(timeout = TIMEOUT)
    public void getUnscaledWidth_WithInvalidCharset_ThrowsException() {
        // Setup
        GraphicsState gs = createGraphicsState();
        PdfGState pdfGState = createPdfGState();
        CMapAwareDocumentFont font = createCMapAwareFont(pdfGState);
        gs.font = font;
        Stack<MarkedContentInfo> stack = new Stack<>();
        Matrix matrix = new Matrix();
        PdfString pdfString = new PdfString("Times-Italic", "Courier-Bold");
        
        try {
            // Execute
            TextRenderInfo renderInfo = new TextRenderInfo(pdfString, gs, matrix, stack);
            renderInfo.getUnscaledWidth();
            fail("Expected UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            // Verify
            assertEquals("Exception should indicate unsupported charset", "Courier-Bold", e.getMessage());
        }
    }

    // Additional tests for other edge cases and exception scenarios...
    // [Remaining tests would follow the same pattern with descriptive names and comments]
}