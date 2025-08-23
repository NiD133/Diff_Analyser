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

public class GzipCompressorOutputStream_ESTestTest21 extends GzipCompressorOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        GzipParameters gzipParameters0 = new GzipParameters();
        gzipParameters0.setCompressionLevel(9);
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream("GzipParameters [bufferSize=512, comment=null, compressionLevel=9, deflateStrategy=0, extraField=null, fileName=null, fileNameCharset=ISO-8859-1, headerCrc=false, modificationInstant=1970-01-01T00:00:00Z, operatingSystem=UNKNOWN, trailerCrc=0, trailerISize=0]");
        GzipCompressorOutputStream gzipCompressorOutputStream0 = new GzipCompressorOutputStream(mockFileOutputStream0, gzipParameters0);
        assertFalse(gzipCompressorOutputStream0.isClosed());
    }
}
