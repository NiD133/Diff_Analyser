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

public class UTF8Writer_ESTestTest35 extends UTF8Writer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        StreamReadConstraints streamReadConstraints0 = StreamReadConstraints.defaults();
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler0 = new BufferRecycler();
        ContentReference contentReference0 = ContentReference.rawReference(true, (Object) null);
        IOContext iOContext0 = new IOContext(streamReadConstraints0, (StreamWriteConstraints) null, errorReportConfiguration0, bufferRecycler0, contentReference0, true);
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream();
        UTF8Writer uTF8Writer0 = new UTF8Writer(iOContext0, pipedOutputStream0);
        uTF8Writer0.write(56319);
        try {
            uTF8Writer0.write("com.fasterxml.jackson.core.io.IOContext");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Broken surrogate pair: first char 0xdbff, second 0x63; illegal combination
            //
            verifyException("com.fasterxml.jackson.core.io.UTF8Writer", e);
        }
    }
}
