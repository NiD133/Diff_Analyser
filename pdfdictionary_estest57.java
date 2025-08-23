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

public class PdfDictionary_ESTestTest57 extends PdfDictionary_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test56() throws Throwable {
        PdfRectangle pdfRectangle0 = new PdfRectangle(0.0F, 0.0F, 2884.4F, 2884.4F);
        LinkedHashMap<String, PdfRectangle> linkedHashMap0 = new LinkedHashMap<String, PdfRectangle>(4, 5);
        PdfPage pdfPage0 = new PdfPage(pdfRectangle0, linkedHashMap0, (PdfDictionary) null, 1836);
        boolean boolean0 = pdfPage0.isPage();
        assertTrue(boolean0);
    }
}
