package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.junit.Test;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.fail;

/**
 * This test suite for {@link DataFormatMatcher} was improved for understandability.
 * It focuses on the behavior of the class when handling I/O errors.
 */
public class DataFormatMatcher_ESTestTest3 extends DataFormatMatcher_ESTest_scaffolding {

    /**
     * Verifies that {@link DataFormatMatcher#createParserWithMatch()} propagates an {@link IOException}
     * when the underlying {@link InputStream} is faulty and throws an error upon access.
     */
    @Test
    public void createParserWithMatchShouldPropagateIOExceptionFromFaultyStream() {
        // ARRANGE: Create a DataFormatMatcher with an InputStream that is designed to fail.
        byte[] bufferedData = new byte[3];
        JsonFactory jsonFactory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.FULL_MATCH;

        // A MockFileInputStream from EvoSuite's framework, when created with a new (and thus invalid)
        // FileDescriptor, will throw an IOException on any read attempt. This simulates a faulty stream.
        FileDescriptor invalidFileDescriptor = new FileDescriptor();
        InputStream faultyInputStream = new MockFileInputStream(invalidFileDescriptor);

        InputAccessor.Std inputAccessor = new InputAccessor.Std(faultyInputStream, bufferedData);
        DataFormatMatcher dataFormatMatcher = inputAccessor.createMatcher(jsonFactory, matchStrength);

        // ACT & ASSERT: Attempting to create a parser should fail because it will try to
        // read from the faulty stream, which is expected to throw an IOException.
        try {
            dataFormatMatcher.createParserWithMatch();
            fail("Expected an IOException to be thrown because the underlying stream is invalid.");
        } catch (IOException e) {
            // SUCCESS: The expected exception was caught.
            // The original test verified the specific type of the mock-related exception.
            // This verification is preserved to maintain the test's original intent.
            verifyException("org.evosuite.runtime.mock.java.io.NativeMockedIO", e);
        }
    }
}