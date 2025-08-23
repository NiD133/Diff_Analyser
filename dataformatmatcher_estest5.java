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

public class DataFormatMatcher_ESTestTest5 extends DataFormatMatcher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        byte[] byteArray0 = new byte[1];
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0);
        JsonFactory jsonFactory0 = new JsonFactory();
        MatchStrength matchStrength0 = MatchStrength.INCONCLUSIVE;
        DataFormatMatcher dataFormatMatcher0 = null;
        try {
            dataFormatMatcher0 = new DataFormatMatcher(byteArrayInputStream0, (byte[]) null, '\"', '\"', jsonFactory0, matchStrength0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.core.format.DataFormatMatcher", e);
        }
    }
}
