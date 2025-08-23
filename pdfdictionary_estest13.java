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

public class PdfDictionary_ESTestTest13 extends PdfDictionary_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        PdfName pdfName0 = PdfName.CFM;
        PdfDictionary pdfDictionary0 = new PdfDictionary(pdfName0);
        boolean boolean0 = pdfDictionary0.checkType(pdfName0);
        assertTrue(boolean0);
    }
}
