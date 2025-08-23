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

public class PdfDictionary_ESTestTest47 extends PdfDictionary_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        PdfTransparencyGroup pdfTransparencyGroup0 = new PdfTransparencyGroup();
        PdfName pdfName0 = PdfName.FUNCTIONTYPE;
        pdfTransparencyGroup0.put(pdfName0, pdfName0);
        assertEquals(10, PdfObject.INDIRECT);
    }
}
