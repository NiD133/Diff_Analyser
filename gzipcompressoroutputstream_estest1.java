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

public class GzipCompressorOutputStream_ESTestTest1 extends GzipCompressorOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        MockFile mockFile0 = new MockFile("L{'7<X6}>CRV", "A<!S*Ru-Q;9jYnC");
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream(mockFile0, false);
        FilterOutputStream filterOutputStream0 = new FilterOutputStream(mockFileOutputStream0);
        GzipCompressorOutputStream gzipCompressorOutputStream0 = new GzipCompressorOutputStream(filterOutputStream0);
        byte[] byteArray0 = new byte[1];
        gzipCompressorOutputStream0.write(byteArray0, (int) (byte) 48, (-302));
        assertEquals(10L, mockFile0.length());
    }
}
