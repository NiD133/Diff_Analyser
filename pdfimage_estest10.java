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

public class PdfImage_ESTestTest10 extends PdfImage_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        byte[] byteArray0 = new byte[1];
        Image image0 = Image.getInstance(4145, (-84), byteArray0, byteArray0);
        PdfImage pdfImage0 = new PdfImage(image0, "", (PdfIndirectReference) null);
        Image image1 = pdfImage0.getImage();
        assertFalse(image1.isScaleToFitLineWhenOverflow());
    }
}
