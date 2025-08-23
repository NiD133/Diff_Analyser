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

public class PdfDictionary_ESTestTest15 extends PdfDictionary_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        PdfDocument.PdfInfo pdfDocument_PdfInfo0 = new PdfDocument.PdfInfo();
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream();
        FdfWriter fdfWriter0 = new FdfWriter(pipedOutputStream0);
        FdfWriter.Wrt fdfWriter_Wrt0 = fdfWriter0.wrt;
        try {
            pdfDocument_PdfInfo0.toPdf(fdfWriter_Wrt0, pipedOutputStream0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Pipe not connected
            //
            verifyException("java.io.PipedOutputStream", e);
        }
    }
}
