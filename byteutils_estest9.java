package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PushbackInputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.runner.RunWith;

public class ByteUtils_ESTestTest9 extends ByteUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream();
        PipedInputStream pipedInputStream0 = new PipedInputStream(pipedOutputStream0);
        PushbackInputStream pushbackInputStream0 = new PushbackInputStream(pipedInputStream0);
        BufferedInputStream bufferedInputStream0 = new BufferedInputStream(pushbackInputStream0);
        DataInputStream dataInputStream0 = new DataInputStream(bufferedInputStream0);
        long long0 = ByteUtils.fromLittleEndian((DataInput) dataInputStream0, (-325));
        assertEquals(0L, long0);
    }
}
