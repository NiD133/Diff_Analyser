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

public class PdfImage_ESTestTest5 extends PdfImage_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        byte[] byteArray0 = new byte[0];
        ImgJBIG2 imgJBIG2_0 = new ImgJBIG2(484, 484, byteArray0, byteArray0);
        imgJBIG2_0.makeMask();
        PdfIndirectReference pdfIndirectReference0 = new PdfIndirectReference(9496, 29, 5);
        PdfImage pdfImage0 = new PdfImage(imgJBIG2_0, "", pdfIndirectReference0);
        Image image0 = pdfImage0.getImage();
        assertEquals(484.0F, image0.getWidth(), 0.01F);
    }
}
