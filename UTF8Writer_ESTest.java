package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.UTF8Writer;
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

/**
 * Test suite for the UTF8Writer class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class UTF8Writer_ESTest extends UTF8Writer_ESTest_scaffolding {

    /**
     * Test for illegal surrogate description.
     */
    @Test(timeout = 4000)
    public void testIllegalSurrogateDesc() throws Throwable {
        String description = UTF8Writer.illegalSurrogateDesc(56319);
        assertEquals("Unmatched first part of surrogate pair (0xdbff)", description);
    }

    /**
     * Test for illegal surrogate exception.
     */
    @Test(timeout = 4000)
    public void testIllegalSurrogateException() throws Throwable {
        try {
            UTF8Writer.illegalSurrogate(1114111);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.fasterxml.jackson.core.io.UTF8Writer", e);
        }
    }

    /**
     * Test for converting surrogate.
     */
    @Test(timeout = 4000)
    public void testConvertSurrogate() throws Throwable {
        IOContext context = createDefaultIOContext(false);
        UTF8Writer writer = new UTF8Writer(context, null);
        int unicodePoint = writer.convertSurrogate(56320);
        assertEquals((-56557568), unicodePoint);
    }

    /**
     * Test for appending CharBuffer.
     */
    @Test(timeout = 4000)
    public void testAppendCharBuffer() throws Throwable {
        IOContext context = createDefaultIOContext(true);
        CharBuffer charBuffer = CharBuffer.wrap(new char[]{'\u0080', '\u0000'});
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        UTF8Writer writer = new UTF8Writer(context, pipedOutputStream);
        writer.append(charBuffer);
        assertEquals((-56613888), UTF8Writer.SURROGATE_BASE);
    }

    // Additional tests...

    /**
     * Helper method to create a default IOContext.
     */
    private IOContext createDefaultIOContext(boolean managedResource) {
        StreamReadConstraints readConstraints = StreamReadConstraints.defaults();
        StreamWriteConstraints writeConstraints = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorConfig = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentReference = ContentReference.unknown();
        return new IOContext(readConstraints, writeConstraints, errorConfig, bufferRecycler, contentReference, managedResource);
    }
}