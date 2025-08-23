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

public class PdfImage_ESTestTest4 extends PdfImage_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        ByteBuffer byteBuffer0 = new ByteBuffer();
        byte[] byteArray0 = new byte[8];
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0);
        PdfImage.transferBytes(byteArrayInputStream0, byteBuffer0, (byte) 0);
        assertEquals(8, byteArrayInputStream0.available());
        assertEquals(0, byteBuffer0.size());
    }
}
