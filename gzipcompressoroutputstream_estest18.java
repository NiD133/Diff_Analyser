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

public class GzipCompressorOutputStream_ESTestTest18 extends GzipCompressorOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        File file0 = MockFile.createTempFile("?AT", "");
        MockPrintStream mockPrintStream0 = new MockPrintStream(file0);
        GzipParameters gzipParameters0 = new GzipParameters();
        GzipCompressorOutputStream gzipCompressorOutputStream0 = new GzipCompressorOutputStream(mockPrintStream0, gzipParameters0);
        byte[] byteArray0 = new byte[6];
        gzipCompressorOutputStream0.write(byteArray0, 1188, (int) (byte) 0);
        assertEquals(10L, file0.length());
    }
}
