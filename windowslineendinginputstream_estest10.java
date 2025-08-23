package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.junit.runner.RunWith;

public class WindowsLineEndingInputStream_ESTestTest10 extends WindowsLineEndingInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        byte[] byteArray0 = new byte[6];
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0);
        PushbackInputStream pushbackInputStream0 = new PushbackInputStream(byteArrayInputStream0);
        DataInputStream dataInputStream0 = new DataInputStream(pushbackInputStream0);
        WindowsLineEndingInputStream windowsLineEndingInputStream0 = new WindowsLineEndingInputStream(dataInputStream0, true);
        // Undeclared exception!
        try {
            windowsLineEndingInputStream0.mark(0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // mark/reset not supported
            //
            verifyException("org.apache.commons.io.input.UnsupportedOperationExceptions", e);
        }
    }
}
