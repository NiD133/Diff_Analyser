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

public class DefaultIndenter_ESTestTest10 extends DefaultIndenter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        DefaultIndenter defaultIndenter0 = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        JsonFactory jsonFactory0 = new JsonFactory();
        MockFile mockFile0 = new MockFile("\n", "\n");
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream(mockFile0, false);
        MockPrintStream mockPrintStream0 = new MockPrintStream(mockFileOutputStream0, true);
        JsonGenerator jsonGenerator0 = jsonFactory0.createGenerator((OutputStream) mockPrintStream0);
        TokenFilter tokenFilter0 = TokenFilter.INCLUDE_ALL;
        TokenFilter.Inclusion tokenFilter_Inclusion0 = TokenFilter.Inclusion.ONLY_INCLUDE_ALL;
        FilteringGeneratorDelegate filteringGeneratorDelegate0 = new FilteringGeneratorDelegate(jsonGenerator0, tokenFilter0, tokenFilter_Inclusion0, false);
        try {
            defaultIndenter0.writeIndentation(filteringGeneratorDelegate0, 2147483645);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Invalid 'offset' (0) and/or 'len' (-6) arguments for `char[]` of length 32
            //
            verifyException("com.fasterxml.jackson.core.JsonGenerator", e);
        }
    }
}
