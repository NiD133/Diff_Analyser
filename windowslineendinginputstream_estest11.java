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

public class WindowsLineEndingInputStream_ESTestTest11 extends WindowsLineEndingInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        byte[] byteArray0 = new byte[0];
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0, 2384, 0);
        WindowsLineEndingInputStream windowsLineEndingInputStream0 = new WindowsLineEndingInputStream(byteArrayInputStream0, true);
        windowsLineEndingInputStream0.close();
    }
}
