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

public class PdfImage_ESTestTest8 extends PdfImage_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        byte[] byteArray0 = new byte[3];
        ImgJBIG2 imgJBIG2_0 = new ImgJBIG2(25, 25, byteArray0, byteArray0);
        imgJBIG2_0.setIndentationRight((-1.0F));
        PdfIndirectReference pdfIndirectReference0 = new PdfIndirectReference(25, 6);
        PdfImage pdfImage0 = new PdfImage(imgJBIG2_0, "UnicodeBig", pdfIndirectReference0);
        Image image0 = pdfImage0.getImage();
        assertEquals(Float.NaN, image0.getAbsoluteY(), 0.01F);
    }
}
