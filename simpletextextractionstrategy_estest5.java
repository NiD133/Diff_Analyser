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

public class SimpleTextExtractionStrategy_ESTestTest5 extends SimpleTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        GraphicsState graphicsState0 = new GraphicsState();
        Matrix matrix0 = graphicsState0.getCtm();
        LinkedHashSet<MarkedContentInfo> linkedHashSet0 = new LinkedHashSet<MarkedContentInfo>();
        PdfAction pdfAction0 = new PdfAction("");
        CMapAwareDocumentFont cMapAwareDocumentFont0 = new CMapAwareDocumentFont(pdfAction0);
        graphicsState0.font = cMapAwareDocumentFont0;
        SimpleTextExtractionStrategy simpleTextExtractionStrategy0 = new SimpleTextExtractionStrategy();
        PdfString pdfString0 = new PdfString("Cp1252", "Cp1250");
        TextRenderInfo textRenderInfo0 = new TextRenderInfo(pdfString0, graphicsState0, matrix0, linkedHashSet0);
        // Undeclared exception!
        try {
            simpleTextExtractionStrategy0.renderText(textRenderInfo0);
            fail("Expecting exception: NoSuchMethodError");
        } catch (NoSuchMethodError e) {
            //
            // java.nio.ByteBuffer.rewind()Ljava/nio/ByteBuffer;
            //
            verifyException("com.itextpdf.text.pdf.PdfEncodings", e);
        }
    }
}
