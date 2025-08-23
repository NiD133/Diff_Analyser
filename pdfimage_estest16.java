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

public class PdfImage_ESTestTest16 extends PdfImage_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream();
        PipedInputStream pipedInputStream0 = new PipedInputStream(pipedOutputStream0);
        BufferedInputStream bufferedInputStream0 = new BufferedInputStream(pipedInputStream0, 1291);
        MockPrintStream mockPrintStream0 = new MockPrintStream(pipedOutputStream0);
        Locale locale0 = Locale.SIMPLIFIED_CHINESE;
        Object[] objectArray0 = new Object[3];
        PrintStream printStream0 = mockPrintStream0.printf(locale0, ">E&1|", objectArray0);
        PdfImage.transferBytes(bufferedInputStream0, printStream0, 4);
        assertEquals(4, pipedInputStream0.available());
    }
}
