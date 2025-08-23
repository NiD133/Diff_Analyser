package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.filter.FilteringGeneratorDelegate;
import com.fasterxml.jackson.core.filter.TokenFilter;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import com.fasterxml.jackson.core.json.WriterBasedJsonGenerator;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class DefaultIndenter_ESTest extends DefaultIndenter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testWriteIndentationWithJsonGeneratorDelegate() throws Throwable {
        DefaultIndenter indenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        JsonFactory jsonFactory = new JsonFactory();
        MockFile mockFile = new MockFile("JSON", "a");
        JsonEncoding encoding = JsonEncoding.UTF32_BE;
        JsonGenerator jsonGenerator = jsonFactory.createGenerator((File) mockFile, encoding);
        JsonGeneratorDelegate generatorDelegate = new JsonGeneratorDelegate(jsonGenerator, true);

        indenter.writeIndentation(generatorDelegate, 0);

        assertEquals(1, generatorDelegate.getOutputBuffered());
        assertEquals(1, jsonGenerator.getOutputBuffered());
    }

    @Test(timeout = 4000)
    public void testWithLinefeedChange() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter("", "As4M!C");
        DefaultIndenter newIndenter = indenter.withLinefeed("JSON");

        assertEquals("JSON", newIndenter.getEol());
    }

    @Test(timeout = 4000)
    public void testGetIndentAndEol() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter("", "As4M!C");

        assertEquals("As4M!C", indenter.getEol());
    }

    @Test(timeout = 4000)
    public void testNullLinefeed() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter("*e/0*h`h7+", null);

        assertNull(indenter.getEol());
    }

    @Test(timeout = 4000)
    public void testEmptyLinefeed() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();
        DefaultIndenter newIndenter = indenter.withLinefeed("");

        assertEquals("", newIndenter.getEol());
    }

    @Test(timeout = 4000)
    public void testWriteIndentationWithLargeLevel() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();
        JsonFactory jsonFactory = new JsonFactory();
        MockFile mockFile = new MockFile("\n");
        JsonEncoding encoding = JsonEncoding.UTF8;
        JsonGenerator jsonGenerator = jsonFactory.createGenerator((File) mockFile, encoding);

        // Expecting an exception due to large indentation level
        try {
            indenter.SYSTEM_LINEFEED_INSTANCE.writeIndentation(jsonGenerator, 56319);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteIndentationWithNullGenerator() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();

        // Expecting a NullPointerException
        try {
            indenter.SYSTEM_LINEFEED_INSTANCE.writeIndentation(null, -110);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteIndentationWithUTF8JsonGenerator() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentReference = ContentReference.construct(true, "=", null);
        IOContext ioContext = new IOContext(null, null, null, bufferRecycler, contentReference, true);
        byte[] byteArray = new byte[4];
        ByteArrayBuilder byteArrayBuilder = ByteArrayBuilder.fromInitial(byteArray, 0);
        UTF8JsonGenerator jsonGenerator = new UTF8JsonGenerator(ioContext, 2, null, byteArrayBuilder, '\'', byteArray, 8, true);

        // Expecting an ArrayIndexOutOfBoundsException
        try {
            indenter.SYSTEM_LINEFEED_INSTANCE.writeIndentation(jsonGenerator, 16);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIOExceptionDuringIndentation() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();
        EvoSuiteFile evoSuiteFile = new EvoSuiteFile("\n/JSON");
        FileSystemHandling.shouldThrowIOException(evoSuiteFile);
        JsonFactory jsonFactory = new JsonFactory();
        MockFile mockFile = new MockFile("\n", "JSON");
        JsonEncoding encoding = JsonEncoding.UTF32_LE;
        JsonGenerator jsonGenerator = jsonFactory.createGenerator((File) mockFile, encoding);

        // Expecting an IOException
        try {
            indenter.SYSTEM_LINEFEED_INSTANCE.writeIndentation(jsonGenerator, 56319);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWriteIndentationWithFilteringGeneratorDelegate() throws Throwable {
        DefaultIndenter indenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        JsonFactory jsonFactory = new JsonFactory();
        MockFile mockFile = new MockFile("\n", "\n");
        MockFileOutputStream mockFileOutputStream = new MockFileOutputStream(mockFile, false);
        MockPrintStream mockPrintStream = new MockPrintStream(mockFileOutputStream, true);
        JsonGenerator jsonGenerator = jsonFactory.createGenerator((OutputStream) mockPrintStream);
        TokenFilter tokenFilter = TokenFilter.INCLUDE_ALL;
        TokenFilter.Inclusion inclusion = TokenFilter.Inclusion.ONLY_INCLUDE_ALL;
        FilteringGeneratorDelegate filteringGeneratorDelegate = new FilteringGeneratorDelegate(jsonGenerator, tokenFilter, inclusion, false);

        // Expecting an IOException
        try {
            indenter.writeIndentation(filteringGeneratorDelegate, Integer.MAX_VALUE - 3);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullLinefeedInWithLinefeed() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();

        // Expecting a NullPointerException
        try {
            indenter.SYSTEM_LINEFEED_INSTANCE.withLinefeed(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullIndentInWithIndent() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();

        // Expecting a NullPointerException
        try {
            indenter.withIndent(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullIndentAndLinefeedInConstructor() throws Throwable {
        // Expecting a NullPointerException
        try {
            new DefaultIndenter(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetIndent() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();

        assertEquals("\n", indenter.getEol());
    }

    @Test(timeout = 4000)
    public void testWriteIndentationWithWriterBasedJsonGenerator() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter("", "");
        DefaultIndenter newIndenter = indenter.withIndent("M");
        BufferRecycler bufferRecycler = new BufferRecycler(500, 1936);
        ContentReference contentReference = ContentReference.construct(true, indenter, 1, 16, null);
        IOContext ioContext = new IOContext(null, null, null, bufferRecycler, contentReference, true);
        StringWriter stringWriter = new StringWriter();
        WriterBasedJsonGenerator jsonGenerator = new WriterBasedJsonGenerator(ioContext, 806, null, stringWriter, 'Y');

        newIndenter.writeIndentation(jsonGenerator, 2048);

        assertEquals(2048, jsonGenerator.getOutputBuffered());
    }

    @Test(timeout = 4000)
    public void testWriteIndentationWithNegativeLevel() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter("", "As4M!C");
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator((Writer) stringWriter);

        indenter.writeIndentation(jsonGenerator, -1909562014);

        assertEquals(6, jsonGenerator.getOutputBuffered());
        assertEquals("As4M!C", indenter.getEol());
    }

    @Test(timeout = 4000)
    public void testWithIndentReturnsSameInstance() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();
        DefaultIndenter newIndenter = indenter.SYSTEM_LINEFEED_INSTANCE.withIndent("");

        assertSame(newIndenter, indenter.SYSTEM_LINEFEED_INSTANCE);
        assertEquals("\n", newIndenter.getEol());
    }

    @Test(timeout = 4000)
    public void testWithLinefeedReturnsSameInstance() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();
        DefaultIndenter newIndenter = indenter.withLinefeed("\n");

        assertSame(newIndenter, indenter);
    }

    @Test(timeout = 4000)
    public void testGetEol() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();

        assertEquals("\n", indenter.getEol());
    }

    @Test(timeout = 4000)
    public void testIsInline() throws Throwable {
        DefaultIndenter indenter = new DefaultIndenter();

        assertFalse(indenter.isInline());
        assertEquals("\n", indenter.getEol());
    }
}