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

public class ObservableInputStream_ESTestTest16 extends ObservableInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        ObservableInputStream.Builder observableInputStream_Builder0 = new ObservableInputStream.Builder();
        StringWriter stringWriter0 = new StringWriter();
        StringBuffer stringBuffer0 = stringWriter0.getBuffer();
        observableInputStream_Builder0.setCharSequence(stringBuffer0);
        ObservableInputStream observableInputStream0 = observableInputStream_Builder0.get();
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            observableInputStream0.read(byteArray0, 0, (-1));
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // Array Size=0, offset=0, length=-1
            //
            verifyException("org.apache.commons.io.input.CharSequenceInputStream", e);
        }
    }
}