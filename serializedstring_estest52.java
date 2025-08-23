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

public class SerializedString_ESTestTest52 extends SerializedString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        SerializedString serializedString0 = new SerializedString("2]sk2");
        char[] charArray0 = serializedString0.asQuotedChars();
        char[] charArray1 = serializedString0.asQuotedChars();
        assertSame(charArray1, charArray0);
    }
}
