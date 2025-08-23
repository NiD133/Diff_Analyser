package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class GzipCompressorOutputStream_ESTestTest22 extends GzipCompressorOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream();
        GzipParameters gzipParameters0 = new GzipParameters();
        gzipParameters0.setHeaderCRC(true);
        GzipCompressorOutputStream gzipCompressorOutputStream0 = new GzipCompressorOutputStream(byteArrayOutputStream0, gzipParameters0);
        assertEquals(12, byteArrayOutputStream0.size());
        assertEquals("\u001F\uFFFD\b\u0002\u0000\u0000\u0000\u0000\u0000\uFFFD\uFFFD\uFFFD", byteArrayOutputStream0.toString());
    }
}
