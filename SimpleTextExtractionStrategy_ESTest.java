package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

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

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class SimpleTextExtractionStrategy_ESTest extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    // Helper method to create a basic TextRenderInfo for testing
    private TextRenderInfo createBasicTextRenderInfo() {
        PdfDate pdfDate = new PdfDate();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = new Matrix();
        LinkedHashSet<MarkedContentInfo> markedContent = new LinkedHashSet<>();
        
        PdfAction pdfAction = new PdfAction();
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        
        return new TextRenderInfo(pdfDate, graphicsState, matrix, markedContent);
    }

    @Test(timeout = 4000)
    public void testAppendTextChunkAndRenderText_ShouldReturnAppendedText() {
        // Given: A text extraction strategy
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        TextRenderInfo textInfo = createBasicTextRenderInfo();
        
        // When: We render text, append a chunk, and render again
        strategy.renderText(textInfo);
        strategy.appendTextChunk("Cp1257");
        strategy.renderText(textInfo);
        
        // Then: The resultant text should contain the appended chunk
        assertEquals("Cp1257", strategy.getResultantText());
    }

    @Test(timeout = 4000)
    public void testAppendTextChunk_WithStringBuffer_ShouldAppendCorrectly() {
        // Given: A text extraction strategy and a StringBuffer with text
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        StringBuffer textBuffer = new StringBuffer(",.,u/K'~XTk8go");
        
        // When: We append the StringBuffer content
        strategy.appendTextChunk(textBuffer);
        
        // Then: The text should be extracted correctly
        String result = strategy.getResultantText();
        assertEquals(",.,u/K'~XTk8go", result);
    }

    @Test(timeout = 4000)
    public void testRenderText_WithUnsupportedCharset_ShouldThrowException() {
        // Given: A strategy and TextRenderInfo with unsupported charset
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = graphicsState.getCtm();
        LinkedHashSet<MarkedContentInfo> markedContent = new LinkedHashSet<>();
        
        PdfAction pdfAction = new PdfAction("UnicodeBig", "UnicodeBig", "", "PDF");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        
        PdfString pdfString = new PdfString("MacRoman", "Times-BoldItalic");
        TextRenderInfo textInfo = new TextRenderInfo(pdfString, graphicsState, matrix, markedContent);
        
        // When & Then: Rendering should throw UnsupportedCharsetException
        try { 
            strategy.renderText(textInfo);
            fail("Expected UnsupportedCharsetException");
        } catch(UnsupportedCharsetException e) {
            assertEquals("Times-BoldItalic", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testRenderText_WithIllegalCharsetName_ShouldThrowException() {
        // Given: A strategy and TextRenderInfo with illegal charset name
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = new Matrix();
        LinkedHashSet<MarkedContentInfo> markedContent = new LinkedHashSet<>();
        
        PdfAction pdfAction = PdfAction.createLaunch("", "", "PDF", "");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        
        PdfString pdfString = new PdfString("Identity-H", ">|"); // ">|" is illegal charset name
        TextRenderInfo textInfo = new TextRenderInfo(pdfString, graphicsState, matrix, markedContent);
        
        // When & Then: Rendering should throw IllegalCharsetNameException
        try { 
            strategy.renderText(textInfo);
            fail("Expected IllegalCharsetNameException");
        } catch(IllegalCharsetNameException e) {
            assertEquals(">|", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testRenderText_WithValidCharset_ShouldThrowNoSuchMethodError() {
        // Given: A strategy and TextRenderInfo with valid charset but missing method
        GraphicsState graphicsState = new GraphicsState();
        Matrix matrix = graphicsState.getCtm();
        LinkedHashSet<MarkedContentInfo> markedContent = new LinkedHashSet<>();
        
        PdfAction pdfAction = new PdfAction("");
        CMapAwareDocumentFont font = new CMapAwareDocumentFont(pdfAction);
        graphicsState.font = font;
        
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        PdfString pdfString = new PdfString("Cp1252", "Cp1250");
        TextRenderInfo textInfo = new TextRenderInfo(pdfString, graphicsState, matrix, markedContent);
        
        // When & Then: Should throw NoSuchMethodError due to missing ByteBuffer.rewind() method
        try { 
            strategy.renderText(textInfo);
            fail("Expected NoSuchMethodError");
        } catch(NoSuchMethodError e) {
            assertTrue(e.getMessage().contains("java.nio.ByteBuffer.rewind()"));
        }
    }

    @Test(timeout = 4000)
    public void testAppendTextChunk_WithNullSegment_ShouldThrowNullPointerException() {
        // Given: A strategy and a Segment with null array
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        Segment nullSegment = new Segment((char[]) null, 2087, 2087);
        
        // When & Then: Should throw NullPointerException
        try { 
            strategy.appendTextChunk(nullSegment);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected - null array in Segment causes NPE
        }
    }

    @Test(timeout = 4000)
    public void testAppendTextChunk_WithNegativeIndices_ShouldThrowIndexOutOfBoundsException() {
        // Given: A strategy and a Segment with negative indices
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        char[] charArray = new char[2];
        Segment invalidSegment = new Segment(charArray, -26, -26);
        
        // When & Then: Should throw IndexOutOfBoundsException
        try { 
            strategy.appendTextChunk(invalidSegment);
            fail("Expected IndexOutOfBoundsException");
        } catch(IndexOutOfBoundsException e) {
            assertTrue(e.getMessage().contains("start 0, end -26"));
        }
    }

    @Test(timeout = 4000)
    public void testAppendTextChunk_WithInvalidSegmentIndices_ShouldThrowArrayIndexOutOfBoundsException() {
        // Given: A strategy and a Segment with indices beyond array bounds
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        char[] emptyArray = new char[0];
        Segment invalidSegment = new Segment(emptyArray, 1, 1);
        
        // When & Then: Should throw ArrayIndexOutOfBoundsException
        try { 
            strategy.appendTextChunk(invalidSegment);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch(ArrayIndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMultipleOperations_ShouldMaintainTextOrder() {
        // Given: A strategy and text render info
        TextRenderInfo textInfo = createBasicTextRenderInfo();
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        
        // When: We perform multiple operations in sequence
        strategy.renderText(textInfo);
        StringBuffer textBuffer = new StringBuffer("Inserting row at position ");
        strategy.appendTextChunk(textBuffer);
        strategy.renderText(textInfo);
        
        // Then: The text should be maintained correctly
        assertEquals("Inserting row at position ", strategy.getResultantText());
    }

    @Test(timeout = 4000)
    public void testRenderText_AfterCharBufferAppend_ShouldThrowNullPointerException() {
        // Given: A strategy with CharBuffer content and text render info
        TextRenderInfo textInfo = createBasicTextRenderInfo();
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        CharBuffer charBuffer = CharBuffer.allocate(1037);
        
        // When: We append CharBuffer then try to render text
        strategy.appendTextChunk(charBuffer);
        
        // Then: Should throw NullPointerException
        try { 
            strategy.renderText(textInfo);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected due to internal state issues with CharBuffer
        }
    }

    @Test(timeout = 4000)
    public void testBeginTextBlock_ShouldNotAffectEmptyResult() {
        // Given: A new text extraction strategy
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        
        // When: We begin a text block
        strategy.beginTextBlock();
        
        // Then: Result should still be empty
        assertEquals("", strategy.getResultantText());
    }

    @Test(timeout = 4000)
    public void testCharBufferOperations_ShouldWorkCorrectly() {
        // Given: A strategy and text render info
        TextRenderInfo textInfo = createBasicTextRenderInfo();
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        
        // When: We render text first, then append CharBuffer, then render again
        strategy.renderText(textInfo);
        CharBuffer charBuffer = CharBuffer.allocate(1037);
        strategy.appendTextChunk(charBuffer);
        strategy.renderText(textInfo);
        
        // Then: Text render mode should be accessible
        assertEquals(0, textInfo.getTextRenderMode());
    }

    @Test(timeout = 4000)
    public void testGetResultantText_OnEmptyStrategy_ShouldReturnEmptyString() {
        // Given: A new text extraction strategy
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        
        // When: We get the resultant text without any operations
        String result = strategy.getResultantText();
        
        // Then: Should return empty string
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testEndTextBlock_ShouldNotAffectEmptyResult() {
        // Given: A new text extraction strategy
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        
        // When: We end a text block
        strategy.endTextBlock();
        
        // Then: Result should still be empty
        assertEquals("", strategy.getResultantText());
    }

    @Test(timeout = 4000)
    public void testRenderImage_ShouldBeNoOp() {
        // Given: A strategy and image render info
        SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        PdfSigLockDictionary.LockPermissions permissions = PdfSigLockDictionary.LockPermissions.FORM_FILLING_AND_ANNOTATION;
        PdfSigLockDictionary lockDict = new PdfSigLockDictionary(permissions);
        GraphicsState graphicsState = new GraphicsState();
        HashSet<MarkedContentInfo> markedContent = new HashSet<>();
        
        ImageRenderInfo imageInfo = ImageRenderInfo.createForXObject(
            graphicsState, 
            (PdfIndirectReference) null, 
            (PdfDictionary) lockDict, 
            (Collection<MarkedContentInfo>) markedContent
        );
        
        // When: We render an image
        strategy.renderImage(imageInfo);
        
        // Then: Should have no effect on text result (no-op method)
        assertEquals("", strategy.getResultantText());
    }
}