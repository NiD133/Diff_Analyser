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

public class GzipCompressorOutputStream_ESTestTest9 extends GzipCompressorOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipCompressorOutputStream0 = new GzipCompressorOutputStream(byteArrayOutputStream0);
        gzipCompressorOutputStream0.close();
        // Undeclared exception!
        try {
            gzipCompressorOutputStream0.finish();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // Deflater has been closed
            //
            verifyException("java.util.zip.Deflater", e);
        }
    }
}
