package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class SerializedString_ESTestTest4 extends SerializedString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        SerializedString serializedString0 = new SerializedString("");
        ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream(14);
        MockPrintStream mockPrintStream0 = new MockPrintStream(byteArrayOutputStream0, false);
        BufferedOutputStream bufferedOutputStream0 = new BufferedOutputStream(mockPrintStream0, 65);
        int int0 = serializedString0.writeQuotedUTF8(bufferedOutputStream0);
        assertEquals(0, int0);
    }
}
