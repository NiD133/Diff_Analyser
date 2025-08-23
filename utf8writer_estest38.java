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

public class UTF8Writer_ESTestTest38 extends UTF8Writer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        StreamReadConstraints streamReadConstraints0 = StreamReadConstraints.defaults();
        StreamWriteConstraints streamWriteConstraints0 = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler0 = new BufferRecycler();
        Object object0 = new Object();
        ContentReference contentReference0 = ContentReference.rawReference(true, object0);
        IOContext iOContext0 = new IOContext(streamReadConstraints0, streamWriteConstraints0, errorReportConfiguration0, bufferRecycler0, contentReference0, false);
        ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream();
        UTF8Writer uTF8Writer0 = new UTF8Writer(iOContext0, byteArrayOutputStream0);
        uTF8Writer0.write(2048);
        assertEquals((-56613888), UTF8Writer.SURROGATE_BASE);
    }
}
