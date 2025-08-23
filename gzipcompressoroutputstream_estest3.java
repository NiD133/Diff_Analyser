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

public class GzipCompressorOutputStream_ESTestTest3 extends GzipCompressorOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream("Y[6ArI`UH_`7e@4");
        GzipCompressorOutputStream gzipCompressorOutputStream0 = new GzipCompressorOutputStream(mockFileOutputStream0);
        gzipCompressorOutputStream0.close();
        MockPrintStream mockPrintStream0 = new MockPrintStream(gzipCompressorOutputStream0, false);
        Locale locale0 = Locale.PRC;
        PrintStream printStream0 = mockPrintStream0.printf(locale0, "6^/UjI,*<M[PMH", (Object[]) null);
        GzipCompressorOutputStream gzipCompressorOutputStream1 = new GzipCompressorOutputStream(printStream0);
        assertTrue(gzipCompressorOutputStream0.isClosed());
    }
}
