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

public class ByteUtils_ESTestTest41 extends ByteUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        ByteUtils.ByteSupplier byteUtils_ByteSupplier0 = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(byteUtils_ByteSupplier0).getAsByte();
        long long0 = ByteUtils.fromLittleEndian(byteUtils_ByteSupplier0, 1);
        assertEquals(0L, long0);
    }
}
