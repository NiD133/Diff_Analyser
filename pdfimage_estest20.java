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

public class PdfImage_ESTestTest20 extends PdfImage_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        PdfPSXObject pdfPSXObject0 = new PdfPSXObject();
        Image image0 = Image.getInstance((PdfTemplate) pdfPSXObject0);
        PdfIndirectReference pdfIndirectReference0 = new PdfIndirectReference(2, 3);
        image0.setInterpolation(true);
        PdfImage pdfImage0 = null;
        try {
            pdfImage0 = new PdfImage(image0, (String) null, pdfIndirectReference0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.evosuite.runtime.mock.java.net.MockURL", e);
        }
    }
}
