package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.Rectangle;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class PdfImage_ESTestTest17 extends PdfImage_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        ByteBuffer byteBuffer0 = new ByteBuffer();
        FdfWriter fdfWriter0 = new FdfWriter();
        FdfWriter.Wrt fdfWriter_Wrt0 = new FdfWriter.Wrt(byteBuffer0, fdfWriter0);
        PdfTemplate pdfTemplate0 = PdfTemplate.createTemplate((PdfWriter) fdfWriter_Wrt0, (float) 1, 2330.0F, fdfWriter_Wrt0.PDF_VERSION_1_4);
        ImgTemplate imgTemplate0 = new ImgTemplate(pdfTemplate0);
        int[] intArray0 = new int[1];
        imgTemplate0.setTransparency(intArray0);
        PdfImage pdfImage0 = null;
        try {
            pdfImage0 = new PdfImage(imgTemplate0, (String) null, (PdfIndirectReference) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }
}
