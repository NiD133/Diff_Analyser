package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfOCProperties;
import com.itextpdf.text.pdf.PdfSigLockDictionary;
import com.itextpdf.text.pdf.PdfString;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Collection;
import java.util.LinkedList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class LocationTextExtractionStrategy_ESTestTest17 extends LocationTextExtractionStrategy_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        GraphicsState graphicsState0 = new GraphicsState();
        PdfOCProperties pdfOCProperties0 = new PdfOCProperties();
        CMapAwareDocumentFont cMapAwareDocumentFont0 = new CMapAwareDocumentFont(pdfOCProperties0);
        graphicsState0.font = cMapAwareDocumentFont0;
        LinkedList<MarkedContentInfo> linkedList0 = new LinkedList<MarkedContentInfo>();
        LocationTextExtractionStrategy locationTextExtractionStrategy0 = new LocationTextExtractionStrategy();
        PdfString pdfString0 = new PdfString("],", ".notdef");
        Matrix matrix0 = new Matrix(9, 1374.654F, 644.4266F, 23, 2, 3.1684228E7F);
        TextRenderInfo textRenderInfo0 = new TextRenderInfo(pdfString0, graphicsState0, matrix0, linkedList0);
        // Undeclared exception!
        try {
            locationTextExtractionStrategy0.renderText(textRenderInfo0);
            fail("Expecting exception: IllegalCharsetNameException");
        } catch (IllegalCharsetNameException e) {
            //
            // .notdef
            //
            verifyException("java.nio.charset.Charset", e);
        }
    }
}
