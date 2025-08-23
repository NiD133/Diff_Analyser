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

public class GzipCompressorOutputStream_ESTestTest15 extends GzipCompressorOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        GzipCompressorOutputStream gzipCompressorOutputStream0 = null;
        try {
            gzipCompressorOutputStream0 = new GzipCompressorOutputStream((OutputStream) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream", e);
        }
    }
}
