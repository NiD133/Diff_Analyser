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

public class StreamCompressor_ESTestTest10 extends StreamCompressor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream("Sq~)ZO");
        StreamCompressor streamCompressor0 = StreamCompressor.create((OutputStream) mockFileOutputStream0);
        byte[] byteArray0 = new byte[4];
        // Undeclared exception!
        try {
            streamCompressor0.writeCounted(byteArray0, (-2345), (int) (byte) 33);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // -2345
            //
            verifyException("org.evosuite.runtime.vfs.VFile", e);
        }
    }
}
