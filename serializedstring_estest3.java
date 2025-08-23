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

public class SerializedString_ESTestTest3 extends SerializedString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        SerializedString serializedString0 = new SerializedString("E]`R4#OI%");
        MockFile mockFile0 = new MockFile("E]`R4#OI%", "E]`R4#OI%");
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream(mockFile0);
        int int0 = serializedString0.writeUnquotedUTF8(mockFileOutputStream0);
        assertEquals(9, int0);
        assertEquals(9L, mockFile0.length());
    }
}
