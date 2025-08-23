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

public class DataFormatMatcher_ESTestTest17 extends DataFormatMatcher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        byte[] byteArray0 = new byte[9];
        JsonFactory jsonFactory0 = new JsonFactory();
        MatchStrength matchStrength0 = MatchStrength.WEAK_MATCH;
        DataFormatMatcher dataFormatMatcher0 = null;
        try {
            dataFormatMatcher0 = new DataFormatMatcher((InputStream) null, byteArray0, '\"', (-1442), jsonFactory0, matchStrength0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Illegal start/length (34/-1442) wrt input array of 9 bytes
            //
            verifyException("com.fasterxml.jackson.core.format.DataFormatMatcher", e);
        }
    }
}