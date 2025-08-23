package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.filter.FilteringGeneratorDelegate;
import com.fasterxml.jackson.core.filter.TokenFilter;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import com.fasterxml.jackson.core.json.WriterBasedJsonGenerator;
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

public class DefaultIndenter_ESTestTest9 extends DefaultIndenter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        DefaultIndenter defaultIndenter0 = new DefaultIndenter();
        EvoSuiteFile evoSuiteFile0 = new EvoSuiteFile("\n/JSON");
        FileSystemHandling.shouldThrowIOException(evoSuiteFile0);
        JsonFactory jsonFactory0 = new JsonFactory();
        MockFile mockFile0 = new MockFile("\n", "JSON");
        JsonEncoding jsonEncoding0 = JsonEncoding.UTF32_LE;
        JsonGenerator jsonGenerator0 = jsonFactory0.createGenerator((File) mockFile0, jsonEncoding0);
        try {
            defaultIndenter0.SYSTEM_LINEFEED_INSTANCE.writeIndentation(jsonGenerator0, 56319);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Simulated IOException
            //
            verifyException("org.evosuite.runtime.vfs.VirtualFileSystem", e);
        }
    }
}
