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

public class SimpleTextExtractionStrategy_ESTestTest12 extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        PdfDate pdfDate0 = new PdfDate();
        GraphicsState graphicsState0 = new GraphicsState();
        Matrix matrix0 = graphicsState0.ctm;
        LinkedHashSet<MarkedContentInfo> linkedHashSet0 = new LinkedHashSet<MarkedContentInfo>();
        PdfAction pdfAction0 = PdfAction.createLaunch("", "", "PDF", "");
        CMapAwareDocumentFont cMapAwareDocumentFont0 = new CMapAwareDocumentFont(pdfAction0);
        graphicsState0.font = cMapAwareDocumentFont0;
        TextRenderInfo textRenderInfo0 = new TextRenderInfo(pdfDate0, graphicsState0, matrix0, linkedHashSet0);
        SimpleTextExtractionStrategy simpleTextExtractionStrategy0 = new SimpleTextExtractionStrategy();
        simpleTextExtractionStrategy0.renderText(textRenderInfo0);
        CharBuffer charBuffer0 = CharBuffer.allocate(1037);
        simpleTextExtractionStrategy0.appendTextChunk(charBuffer0);
        simpleTextExtractionStrategy0.renderText(textRenderInfo0);
        assertEquals(0, textRenderInfo0.getTextRenderMode());
    }
}
