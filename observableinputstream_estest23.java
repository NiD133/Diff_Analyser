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

public class ObservableInputStream_ESTestTest23 extends ObservableInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        Enumeration<ObjectInputStream> enumeration0 = (Enumeration<ObjectInputStream>) mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration0).hasMoreElements();
        SequenceInputStream sequenceInputStream0 = new SequenceInputStream(enumeration0);
        ObservableInputStream.Observer[] observableInputStream_ObserverArray0 = new ObservableInputStream.Observer[1];
        TimestampedObserver timestampedObserver0 = new TimestampedObserver();
        observableInputStream_ObserverArray0[0] = (ObservableInputStream.Observer) timestampedObserver0;
        ObservableInputStream observableInputStream0 = new ObservableInputStream(sequenceInputStream0, observableInputStream_ObserverArray0);
        MockIOException mockIOException0 = new MockIOException("");
        try {
            observableInputStream0.noteError(mockIOException0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // 1 exception(s): [org.evosuite.runtime.mock.java.lang.MockThrowable: MockIOException #0: ]
            //
            verifyException("org.apache.commons.io.IOExceptionList", e);
        }
    }
}
