package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.Strictness;
import com.google.gson.ToNumberPolicy;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for testing different ToNumberPolicy strategies in Gson.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class ToNumberPolicy_ESTest extends ToNumberPolicy_ESTest_scaffolding {

    /**
     * Test LONG_OR_DOUBLE policy with malformed JSON input.
     * Expects a RuntimeException due to parsing failure.
     */
    @Test(timeout = 4000)
    public void testLongOrDoubleWithMalformedJson() throws Throwable {
        String malformedJson = "\"*.\"{/"; // Malformed JSON input
        JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));
        ToNumberPolicy policy = ToNumberPolicy.LONG_OR_DOUBLE;

        try {
            policy.readNumber(jsonReader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.ToNumberPolicy$3", e);
        }
    }

    /**
     * Test BIG_DECIMAL policy with malformed JSON input in lenient mode.
     * Expects a RuntimeException due to parsing failure.
     */
    @Test(timeout = 4000)
    public void testBigDecimalWithMalformedJsonLenient() throws Throwable {
        String malformedJson = "p(ppj%8[r\u0000>Md]"; // Malformed JSON input
        JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));
        jsonReader.setStrictness(Strictness.LENIENT);
        ToNumberPolicy policy = ToNumberPolicy.BIG_DECIMAL;

        try {
            policy.readNumber(jsonReader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.ToNumberPolicy$4", e);
        }
    }

    /**
     * Test LONG_OR_DOUBLE policy with another malformed JSON input in lenient mode.
     * Expects a RuntimeException due to parsing failure.
     */
    @Test(timeout = 4000)
    public void testLongOrDoubleWithAnotherMalformedJsonLenient() throws Throwable {
        String malformedJson = ".a&;-m"; // Malformed JSON input
        JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));
        jsonReader.setStrictness(Strictness.LENIENT);
        ToNumberPolicy policy = ToNumberPolicy.LONG_OR_DOUBLE;

        try {
            policy.readNumber(jsonReader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.ToNumberPolicy$3", e);
        }
    }

    /**
     * Test LONG_OR_DOUBLE policy with same malformed JSON input as test1 in lenient mode.
     * Expects a RuntimeException due to parsing failure.
     */
    @Test(timeout = 4000)
    public void testLongOrDoubleWithSameMalformedJsonLenient() throws Throwable {
        String malformedJson = "p(ppj%8[r\u0000>Md]"; // Malformed JSON input
        JsonReader jsonReader = new JsonReader(new StringReader(malformedJson));
        jsonReader.setStrictness(Strictness.LENIENT);
        ToNumberPolicy policy = ToNumberPolicy.LONG_OR_DOUBLE;

        try {
            policy.readNumber(jsonReader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("com.google.gson.ToNumberPolicy$3", e);
        }
    }

    /**
     * Test LAZILY_PARSED_NUMBER policy with null JsonReader.
     * Expects a NullPointerException due to null input.
     */
    @Test(timeout = 4000)
    public void testLazilyParsedNumberWithNullReader() throws Throwable {
        ToNumberPolicy policy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

        try {
            policy.readNumber(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.ToNumberPolicy$2", e);
        }
    }

    /**
     * Test DOUBLE policy with null JsonReader.
     * Expects a NullPointerException due to null input.
     */
    @Test(timeout = 4000)
    public void testDoubleWithNullReader() throws Throwable {
        ToNumberPolicy policy = ToNumberPolicy.DOUBLE;

        try {
            policy.readNumber(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.ToNumberPolicy$1", e);
        }
    }
}