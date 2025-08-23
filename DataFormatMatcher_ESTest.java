package com.fasterxml.jackson.core.format;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.format.DataFormatMatcher;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.format.MatchStrength;
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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class DataFormatMatcher_ESTest extends DataFormatMatcher_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCreateMatcherWithSolidMatch() throws Throwable {
        PipedInputStream pipedInputStream = new PipedInputStream();
        byte[] buffer = new byte[6];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(pipedInputStream, buffer);
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength expectedMatchStrength = MatchStrength.SOLID_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, expectedMatchStrength);
        JsonFactory matchedFactory = matcher.getMatch();

        assertNotNull(matchedFactory);
        assertEquals(expectedMatchStrength, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void testCreateParserWithArrayIndexOutOfBoundsException() throws Throwable {
        byte[] buffer = new byte[6];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.SOLID_MATCH;

        DataFormatMatcher matcher = new DataFormatMatcher(byteArrayInputStream, buffer, 2, 1, jsonFactory, matchStrength);

        try {
            matcher.createParserWithMatch();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCreateParserWithIOException() throws Throwable {
        byte[] buffer = new byte[3];
        JsonFactory jsonFactory = new JsonFactory();
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        InputAccessor.Std inputAccessor = new InputAccessor.Std(mockFileInputStream, buffer);
        MatchStrength matchStrength = MatchStrength.FULL_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, matchStrength);

        try {
            matcher.createParserWithMatch();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
            verifyException("org.evosuite.runtime.mock.java.io.NativeMockedIO", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateParserWithCharConversionException() throws Throwable {
        byte[] buffer = new byte[9];
        buffer[2] = (byte) (-83);
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        ObjectCodec objectCodec = mock(ObjectCodec.class, new ViolatedAssumptionAnswer());
        JsonFactory jsonFactory = new JsonFactory(objectCodec);
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, matchStrength);

        try {
            matcher.createParserWithMatch();
            fail("Expecting exception: CharConversionException");
        } catch (CharConversionException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.json.ByteSourceJsonBootstrapper", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateMatcherWithNullPointerException() throws Throwable {
        byte[] buffer = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.INCONCLUSIVE;

        try {
            DataFormatMatcher matcher = new DataFormatMatcher(byteArrayInputStream, null, '\"', '\"', jsonFactory, matchStrength);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.format.DataFormatMatcher", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetDataStreamWithWeakMatch() throws Throwable {
        byte[] buffer = new byte[9];
        JsonFactory jsonFactory = new JsonFactory();
        FileDescriptor fileDescriptor = new FileDescriptor();
        MockFileInputStream mockFileInputStream = new MockFileInputStream(fileDescriptor);
        InputAccessor.Std inputAccessor = new InputAccessor.Std(mockFileInputStream, buffer);
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, matchStrength);
        matcher.getDataStream();

        assertTrue(matcher.hasMatch());
        assertEquals(matchStrength, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void testHasMatchWithEmptyBuffer() throws Throwable {
        byte[] buffer = new byte[0];
        JsonFactory jsonFactory = new JsonFactory();
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, matchStrength);
        boolean hasMatch = matcher.hasMatch();

        assertEquals(matchStrength, matcher.getMatchStrength());
        assertTrue(hasMatch);
    }

    @Test(timeout = 4000)
    public void testNoMatchWithNullJsonFactory() throws Throwable {
        byte[] buffer = new byte[0];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.NO_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(null, matchStrength);
        matcher.hasMatch();

        assertEquals(matchStrength, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void testGetMatchWithInconclusiveMatchStrength() throws Throwable {
        byte[] buffer = new byte[6];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.INCONCLUSIVE;

        DataFormatMatcher matcher = inputAccessor.createMatcher(null, matchStrength);
        JsonFactory matchedFactory = matcher.getMatch();

        assertNull(matchedFactory);
    }

    @Test(timeout = 4000)
    public void testGetDataStreamWithWeakMatchAndAvailableBytes() throws Throwable {
        byte[] buffer = new byte[12];
        JsonFactory jsonFactory = new JsonFactory();
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, matchStrength);
        InputStream dataStream = matcher.getDataStream();

        assertEquals(12, dataStream.available());
        assertEquals(matchStrength, matcher.getMatchStrength());
        assertTrue(matcher.hasMatch());
    }

    @Test(timeout = 4000)
    public void testCreateParserWithWeakMatch() throws Throwable {
        byte[] buffer = new byte[9];
        JsonFactory jsonFactory = new JsonFactory();
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, matchStrength);
        JsonParser jsonParser = matcher.createParserWithMatch();

        assertNotNull(jsonParser);
        assertEquals(matchStrength, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void testCreateParserWithNoMatch() throws Throwable {
        byte[] buffer = new byte[0];
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.NO_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(null, matchStrength);
        matcher.createParserWithMatch();

        assertEquals(matchStrength, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void testGetMatchStrengthWithNullMatchStrength() throws Throwable {
        byte[] buffer = new byte[9];
        JsonFactory jsonFactory = new JsonFactory();
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, null);
        matcher.getMatchStrength();

        assertTrue(matcher.hasMatch());
    }

    @Test(timeout = 4000)
    public void testGetMatchStrengthWithNoMatch() throws Throwable {
        byte[] buffer = new byte[14];
        JsonFactory jsonFactory = new JsonFactory();
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.NO_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, matchStrength);
        MatchStrength actualMatchStrength = matcher.getMatchStrength();

        assertEquals(matchStrength, actualMatchStrength);
        assertTrue(matcher.hasMatch());
    }

    @Test(timeout = 4000)
    public void testGetMatchedFormatNameWithInconclusiveMatch() throws Throwable {
        byte[] buffer = new byte[41];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        MatchStrength matchStrength = MatchStrength.INCONCLUSIVE;

        DataFormatMatcher matcher = new DataFormatMatcher(byteArrayInputStream, buffer, 0, 0, null, matchStrength);
        String matchedFormatName = matcher.getMatchedFormatName();

        assertNull(matchedFormatName);
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithZeroLengthBuffer() throws Throwable {
        byte[] buffer = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.INCONCLUSIVE;

        try {
            DataFormatMatcher matcher = new DataFormatMatcher(byteArrayInputStream, buffer, '\"', '\"', jsonFactory, matchStrength);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.format.DataFormatMatcher", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionWithNegativeLength() throws Throwable {
        byte[] buffer = new byte[9];
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        try {
            DataFormatMatcher matcher = new DataFormatMatcher(null, buffer, '\"', -1442, jsonFactory, matchStrength);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("com.fasterxml.jackson.core.format.DataFormatMatcher", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetMatchedFormatNameWithSolidMatch() throws Throwable {
        byte[] buffer = new byte[4];
        JsonFactory jsonFactory = new JsonFactory();
        InputAccessor.Std inputAccessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.SOLID_MATCH;

        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, matchStrength);
        String matchedFormatName = matcher.getMatchedFormatName();

        assertNotNull(matchedFormatName);
        assertEquals(matchStrength, matcher.getMatchStrength());
    }
}