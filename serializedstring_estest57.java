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

public class SerializedString_ESTestTest57 extends SerializedString_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test56() throws Throwable {
        SerializedString serializedString0 = new SerializedString("TP1aO6Zd");
        String string0 = serializedString0.getValue();
        assertEquals("TP1aO6Zd", string0);
    }
}
