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

public class UTF8Writer_ESTestTest2 extends UTF8Writer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        try {
            UTF8Writer.illegalSurrogate(1114111);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Unmatched second part of surrogate pair (0x10ffff)
            //
            verifyException("com.fasterxml.jackson.core.io.UTF8Writer", e);
        }
    }
}
