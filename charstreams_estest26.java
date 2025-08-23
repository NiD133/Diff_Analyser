package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.MalformedInputException;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileReader;
import org.evosuite.runtime.mock.java.io.MockFileWriter;
import org.evosuite.runtime.mock.java.io.MockPrintWriter;
import org.junit.runner.RunWith;

public class CharStreams_ESTestTest26 extends CharStreams_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        FileDescriptor fileDescriptor0 = new FileDescriptor();
        MockFileReader mockFileReader0 = new MockFileReader(fileDescriptor0);
        BufferedReader bufferedReader0 = new BufferedReader(mockFileReader0, 1);
        MockFileWriter mockFileWriter0 = new MockFileWriter("][!>ptM+tm]l");
        // Undeclared exception!
        try {
            CharStreams.copy(bufferedReader0, mockFileWriter0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.evosuite.runtime.mock.java.io.MockFileReader", e);
        }
    }
}
