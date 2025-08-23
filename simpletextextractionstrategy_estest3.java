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

public class SimpleTextExtractionStrategy_ESTestTest3 extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        SimpleTextExtractionStrategy simpleTextExtractionStrategy0 = new SimpleTextExtractionStrategy();
        GraphicsState graphicsState0 = new GraphicsState();
        Matrix matrix0 = graphicsState0.getCtm();
        LinkedHashSet<MarkedContentInfo> linkedHashSet0 = new LinkedHashSet<MarkedContentInfo>();
        PdfAction pdfAction0 = new PdfAction("UnicodeBig", "UnicodeBig", "", "PDF");
        CMapAwareDocumentFont cMapAwareDocumentFont0 = new CMapAwareDocumentFont(pdfAction0);
        graphicsState0.font = cMapAwareDocumentFont0;
        PdfString pdfString0 = new PdfString("MacRoman", "Times-BoldItalic");
        TextRenderInfo textRenderInfo0 = new TextRenderInfo(pdfString0, graphicsState0, matrix0, linkedHashSet0);
        // Undeclared exception!
        try {
            simpleTextExtractionStrategy0.renderText(textRenderInfo0);
            fail("Expecting exception: UnsupportedCharsetException");
        } catch (UnsupportedCharsetException e) {
            //
            // Times-BoldItalic
            //
            verifyException("java.nio.charset.Charset", e);
        }
    }
}
