package com.fasterxml.jackson.core.format;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.junit.runner.RunWith;

public class DataFormatMatcher_ESTestTest3 extends DataFormatMatcher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        byte[] byteArray0 = new byte[3];
        JsonFactory jsonFactory0 = new JsonFactory();
        FileDescriptor fileDescriptor0 = new FileDescriptor();
        MockFileInputStream mockFileInputStream0 = new MockFileInputStream(fileDescriptor0);
        InputAccessor.Std inputAccessor_Std0 = new InputAccessor.Std(mockFileInputStream0, byteArray0);
        MatchStrength matchStrength0 = MatchStrength.FULL_MATCH;
        DataFormatMatcher dataFormatMatcher0 = inputAccessor_Std0.createMatcher(jsonFactory0, matchStrength0);
        try {
            dataFormatMatcher0.createParserWithMatch();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.evosuite.runtime.mock.java.io.NativeMockedIO", e);
        }
    }
}
