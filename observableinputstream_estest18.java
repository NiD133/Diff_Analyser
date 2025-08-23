package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PushbackInputStream;
import java.io.SequenceInputStream;
import java.io.StringWriter;
import java.nio.CharBuffer;
import java.nio.file.NoSuchFileException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.runner.RunWith;

public class ObservableInputStream_ESTestTest18 extends ObservableInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        PushbackInputStream pushbackInputStream0 = new PushbackInputStream((InputStream) null);
        ObservableInputStream.Observer[] observableInputStream_ObserverArray0 = new ObservableInputStream.Observer[0];
        ObservableInputStream observableInputStream0 = new ObservableInputStream(pushbackInputStream0, observableInputStream_ObserverArray0);
        byte[] byteArray0 = new byte[2];
        try {
            observableInputStream0.read(byteArray0, 3, (-2701));
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Stream closed
            //
            verifyException("java.io.PushbackInputStream", e);
        }
    }
}