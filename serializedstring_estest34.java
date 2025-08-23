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

public class SerializedString_ESTestTest34 extends SerializedString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        ByteBuffer byteBuffer0 = ByteBuffer.allocateDirect(7);
        SerializedString serializedString0 = new SerializedString("8g9F:23V=x");
        int int0 = serializedString0.putQuotedUTF8(byteBuffer0);
        assertEquals((-1), int0);
    }
}
