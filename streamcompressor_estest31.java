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

public class StreamCompressor_ESTestTest31 extends StreamCompressor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream();
        StreamCompressor streamCompressor0 = StreamCompressor.create((OutputStream) pipedOutputStream0);
        long long0 = streamCompressor0.getBytesWrittenForLastEntry();
        assertEquals(0L, long0);
    }
}
