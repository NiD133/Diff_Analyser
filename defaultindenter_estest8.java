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

public class DefaultIndenter_ESTestTest8 extends DefaultIndenter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        DefaultIndenter defaultIndenter0 = new DefaultIndenter("<", "=");
        StreamReadConstraints streamReadConstraints0 = StreamReadConstraints.defaults();
        StreamWriteConstraints streamWriteConstraints0 = StreamWriteConstraints.defaults();
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        BufferRecycler bufferRecycler0 = new BufferRecycler();
        ContentReference contentReference0 = ContentReference.construct(true, (Object) "=", errorReportConfiguration0);
        IOContext iOContext0 = new IOContext(streamReadConstraints0, streamWriteConstraints0, errorReportConfiguration0, bufferRecycler0, contentReference0, true);
        byte[] byteArray0 = new byte[4];
        ByteArrayBuilder byteArrayBuilder0 = ByteArrayBuilder.fromInitial(byteArray0, 0);
        UTF8JsonGenerator uTF8JsonGenerator0 = new UTF8JsonGenerator(iOContext0, 2, (ObjectCodec) null, byteArrayBuilder0, '\'', byteArray0, 8, true);
        // Undeclared exception!
        try {
            defaultIndenter0.SYSTEM_LINEFEED_INSTANCE.writeIndentation(uTF8JsonGenerator0, 16);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
}
