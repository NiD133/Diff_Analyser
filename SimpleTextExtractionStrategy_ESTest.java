package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfSigLockDictionary;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.GraphicsState;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.MarkedContentInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.nio.CharBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.swing.text.Segment;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SimpleTextExtractionStrategy_ESTest extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRenderTextWithValidData() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        PdfDate pdfDate = new PdfDate();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = new Matrix();
        LinkedHashSet<MarkedContentInfo> markedContentInfos = new LinkedHashSet<>();
        PdfAction pdfAction = new PdfAction();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfDate, graphicsState, matrix, markedContentInfos);

        strategy.renderText(textRenderInfo);
        strategy.appendTextChunk("Cp1257");
        strategy.renderText(textRenderInfo);

        assertEquals("Cp1257", strategy.getResultantText());
    }

    @Test(timeout = 4000)
    public void testAppendTextChunkWithStringBuffer() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        StringBuffer textBuffer = new StringBuffer(",.,u/K'~XTk8go");

        strategy.appendTextChunk(textBuffer);
        String result = strategy.getResultantText();

        assertEquals(",.,u/K'~XTk8go", result);
    }

    @Test(timeout = 4000)
    public void testRenderTextWithUnsupportedCharset() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = graphicsState.getCtm();
        LinkedHashSet<MarkedContentInfo> markedContentInfos = new LinkedHashSet<>();
        PdfAction pdfAction = new PdfAction("UnicodeBig", "UnicodeBig", "", "PDF");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        PdfString pdfString = new PdfString("MacRoman", "Times-BoldItalic");
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfString, graphicsState, matrix, markedContentInfos);

        try {
            strategy.renderText(textRenderInfo);
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextWithIllegalCharsetName() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = new Matrix();
        LinkedHashSet<MarkedContentInfo> markedContentInfos = new LinkedHashSet<>();
        PdfAction pdfAction = PdfAction.createLaunch("", "", "PDF", "");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        PdfString pdfString = new PdfString("Identity-H", ">|");
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfString, graphicsState, matrix, markedContentInfos);

        try {
            strategy.renderText(textRenderInfo);
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            verifyException("java.nio.charset.Charset", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextWithNoSuchMethodError() throws Throwable {
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = graphicsState.getCtm();
        LinkedHashSet<MarkedContentInfo> markedContentInfos = new LinkedHashSet<>();
        PdfAction pdfAction = new PdfAction("");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        PdfString pdfString = new PdfString("Cp1252", "Cp1250");
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfString, graphicsState, matrix, markedContentInfos);

        try {
            strategy.renderText(textRenderInfo);
            fail("Expecting exception: NoSuchMethodError");
        } catch (NoSuchMethodError e) {
            verifyException("com.itextpdf.text.pdf.PdfEncodings", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendTextChunkWithNullSegment() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        Segment segment = new Segment((char[]) null, 2087, 2087);

        try {
            strategy.appendTextChunk(segment);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("javax.swing.text.Segment", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendTextChunkWithInvalidSegmentIndices() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        char[] charArray = new char[2];
        Segment segment = new Segment(charArray, -26, -26);

        try {
            strategy.appendTextChunk(segment);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.lang.AbstractStringBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendTextChunkWithOutOfBoundsSegment() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        char[] charArray = new char[0];
        Segment segment = new Segment(charArray, 1, 1);

        try {
            strategy.appendTextChunk(segment);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("javax.swing.text.Segment", e);
        }
    }

    @Test(timeout = 4000)
    public void testRenderTextAndAppendTextChunk() throws Throwable {
        PdfDate pdfDate = new PdfDate();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = graphicsState.ctm;
        LinkedHashSet<MarkedContentInfo> markedContentInfos = new LinkedHashSet<>();
        PdfAction pdfAction = new PdfAction();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfDate, graphicsState, matrix, markedContentInfos);
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

        strategy.renderText(textRenderInfo);
        StringBuffer textBuffer = new StringBuffer("Inserting row at position ");
        strategy.appendTextChunk(textBuffer);
        strategy.renderText(textRenderInfo);

        assertEquals("Inserting row at position ", strategy.getResultantText());
    }

    @Test(timeout = 4000)
    public void testRenderTextWithNullPointerException() throws Throwable {
        PdfDate pdfDate = new PdfDate();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = graphicsState.ctm;
        LinkedHashSet<MarkedContentInfo> markedContentInfos = new LinkedHashSet<>();
        PdfAction pdfAction = PdfAction.createLaunch("", "", "PDF", "");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfDate, graphicsState, matrix, markedContentInfos);
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        CharBuffer charBuffer = CharBuffer.allocate(1037);

        strategy.appendTextChunk(charBuffer);

        try {
            strategy.renderText(textRenderInfo);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy", e);
        }
    }

    @Test(timeout = 4000)
    public void testBeginTextBlock() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.beginTextBlock();

        assertEquals("", strategy.getResultantText());
    }

    @Test(timeout = 4000)
    public void testRenderTextAndAppendTextChunkWithCharBuffer() throws Throwable {
        PdfDate pdfDate = new PdfDate();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = graphicsState.ctm;
        LinkedHashSet<MarkedContentInfo> markedContentInfos = new LinkedHashSet<>();
        PdfAction pdfAction = PdfAction.createLaunch("", "", "PDF", "");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        TextRenderInfo textRenderInfo = new TextRenderInfo(pdfDate, graphicsState, matrix, markedContentInfos);
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        CharBuffer charBuffer = CharBuffer.allocate(1037);

        strategy.renderText(textRenderInfo);
        strategy.appendTextChunk(charBuffer);
        strategy.renderText(textRenderInfo);

        assertEquals(0, textRenderInfo.getTextRenderMode());
    }

    @Test(timeout = 4000)
    public void testGetResultantTextWithNoText() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String result = strategy.getResultantText();

        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testEndTextBlock() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        strategy.endTextBlock();

        assertEquals("", strategy.getResultantText());
    }

    @Test(timeout = 4000)
    public void testRenderImage() throws Throwable {
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        PdfSigLockDictionary.LockPermissions lockPermissions = PdfSigLockDictionary.LockPermissions.FORM_FILLING_AND_ANNOTATION;
        PdfSigLockDictionary sigLockDictionary = new PdfSigLockDictionary(lockPermissions);
        GraphicsState graphicsState = new GraphicsState();
        HashSet<MarkedContentInfo> markedContentInfos = new HashSet<>();
        ImageRenderInfo imageRenderInfo = ImageRenderInfo.createForXObject(graphicsState, (PdfIndirectReference) null, (PdfDictionary) sigLockDictionary, (Collection<MarkedContentInfo>) markedContentInfos);

        strategy.renderImage(imageRenderInfo);

        assertEquals("", strategy.getResultantText());
    }
}