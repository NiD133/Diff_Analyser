package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.Writer;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class UTF8Writer_ESTestTest41 extends UTF8Writer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        StreamReadConstraints streamReadConstraints0 = StreamReadConstraints.defaults();
        StreamWriteConstraints streamWriteConstraints0 = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler0 = new BufferRecycler();
        ContentReference contentReference0 = ContentReference.unknown();
        IOContext iOContext0 = new IOContext(streamReadConstraints0, streamWriteConstraints0, errorReportConfiguration0, bufferRecycler0, contentReference0, true);
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream("_C14\u0007{V6YYhZLLho", false);
        UTF8Writer uTF8Writer0 = new UTF8Writer(iOContext0, mockFileOutputStream0);
        char[] charArray0 = new char[4];
        charArray0[2] = '\u008F';
        // Undeclared exception!
        try {
            uTF8Writer0.write(charArray0, 0, 6);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 4
            //
            verifyException("com.fasterxml.jackson.core.io.UTF8Writer", e);
        }
    }
}
