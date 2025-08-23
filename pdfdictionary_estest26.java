package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.collection.PdfCollectionField;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.LinkedHashMap;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class PdfDictionary_ESTestTest26 extends PdfDictionary_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        PdfName pdfName0 = PdfName.LINEHEIGHT;
        PdfDictionary pdfDictionary0 = new PdfDictionary(pdfName0);
        PdfWriter pdfWriter0 = new PdfWriter();
        PdfRectangle pdfRectangle0 = new PdfRectangle(0.0F, 642.05963F, 8388608, (-424.6F));
        Rectangle rectangle0 = PdfReader.getNormalizedRectangle(pdfRectangle0);
        PdfAnnotation pdfAnnotation0 = PdfAnnotation.createLink(pdfWriter0, rectangle0, pdfDictionary0.CATALOG);
        PdfObject pdfObject0 = pdfAnnotation0.getDirectObject(pdfWriter0.PDF_VERSION_1_4);
        assertNull(pdfObject0);
    }
}