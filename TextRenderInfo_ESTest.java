package com.itextpdf.text.pdf.parser;

import org.junit.Test;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TextRenderInfo_ESTest extends TextRenderInfo_ESTest_scaffolding {

    private static final float DELTA = 0.01F;
    private static final int EXPECTED_LIST_SIZE = 9;

    @Test(timeout = 4000)
    public void testCharacterRenderInfosSize() throws Throwable {
        // Setup
        GraphicsState graphicsState = new GraphicsState();
        PdfGState pdfGState = new PdfGState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfGState);
        graphicsState.font = font;
        graphicsState.horizontalScaling = 0.0F;
        Matrix matrix = graphicsState.ctm;
        Stack<MarkedContentInfo> markedContentStack = new Stack<>();

        // Execute
        TextRenderInfo textRenderInfo = new TextRenderInfo(new PdfDate(), graphicsState, matrix, markedContentStack);
        List<TextRenderInfo> characterRenderInfos = textRenderInfo.getCharacterRenderInfos();

        // Verify
        assertEquals(EXPECTED_LIST_SIZE, characterRenderInfos.size());
    }

    @Test(timeout = 4000)
    public void testSingleSpaceWidthWithZeroHorizontalScaling() throws Throwable {
        // Setup
        GraphicsState graphicsState = new GraphicsState();
        PdfGState pdfGState = new PdfGState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfGState);
        graphicsState.font = font;
        graphicsState.horizontalScaling = 0.0F;
        Matrix matrix = graphicsState.ctm;
        Stack<MarkedContentInfo> markedContentStack = new Stack<>();

        // Execute
        TextRenderInfo textRenderInfo = new TextRenderInfo(new PdfDate(), graphicsState, matrix, markedContentStack);
        float singleSpaceWidth = textRenderInfo.getSingleSpaceWidth();

        // Verify
        assertEquals(0.0F, singleSpaceWidth, DELTA);
    }

    @Test(timeout = 4000)
    public void testSingleSpaceWidthWithCharacterSpacing() throws Throwable {
        // Setup
        GraphicsState graphicsState = new GraphicsState();
        PdfGState pdfGState = new PdfGState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfGState);
        graphicsState.font = font;
        graphicsState.characterSpacing = 1025.7177F;
        Matrix matrix = new Matrix(2766.3F, 1543.6833F);
        Collection<MarkedContentInfo> markedContentInfos = new HashSet<>();

        // Execute
        TextRenderInfo textRenderInfo = new TextRenderInfo(new PdfDate(), graphicsState, matrix, markedContentInfos);
        float singleSpaceWidth = textRenderInfo.getSingleSpaceWidth();

        // Verify
        assertEquals(1025.7175F, singleSpaceWidth, DELTA);
    }

    @Test(timeout = 4000)
    public void testRiseValue() throws Throwable {
        // Setup
        GraphicsState graphicsState = new GraphicsState();
        PdfGState pdfGState = new PdfGState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfGState);
        graphicsState.font = font;
        graphicsState.rise = -3505.32F;
        Matrix matrix = graphicsState.ctm;
        LinkedHashSet<MarkedContentInfo> markedContentInfos = new LinkedHashSet<>();

        // Execute
        TextRenderInfo textRenderInfo = new TextRenderInfo(new PdfString(), graphicsState, matrix, markedContentInfos);
        float rise = textRenderInfo.getRise();

        // Verify
        assertEquals(3505.32F, rise, DELTA);
    }

    @Test(timeout = 4000)
    public void testAscentLineNotNull() throws Throwable {
        // Setup
        GraphicsState graphicsState = new GraphicsState();
        PdfGState pdfGState = new PdfGState();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfGState);
        graphicsState.font = font;
        graphicsState.horizontalScaling = 0.0F;
        Matrix matrix = graphicsState.ctm;
        Stack<MarkedContentInfo> markedContentStack = new Stack<>();

        // Execute
        TextRenderInfo textRenderInfo = new TextRenderInfo(new PdfDate(), graphicsState, matrix, markedContentStack);
        LineSegment ascentLine = textRenderInfo.getAscentLine();

        // Verify
        assertNotNull(ascentLine);
    }

    // Additional tests follow the same pattern...

}