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

public class SerializedString_ESTestTest18 extends SerializedString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        SerializedString serializedString0 = new SerializedString("");
        char[] charArray0 = new char[4];
        serializedString0._quotedChars = charArray0;
        int int0 = serializedString0.appendQuoted(charArray0, 0);
        assertEquals(4, int0);
    }
}