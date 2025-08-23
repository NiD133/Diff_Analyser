package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.Enumeration;
import java.util.zip.Deflater;
import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.runner.RunWith;

public class StreamCompressor_ESTestTest5 extends StreamCompressor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream();
        PipedInputStream pipedInputStream0 = new PipedInputStream(pipedOutputStream0);
        StreamCompressor streamCompressor0 = StreamCompressor.create((OutputStream) pipedOutputStream0);
        byte[] byteArray0 = new byte[1];
        streamCompressor0.writeCounted(byteArray0);
        assertEquals(1L, streamCompressor0.getTotalBytesWritten());
    }
}
