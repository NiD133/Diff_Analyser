/*
 * Copyright (C) 2021 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link ToNumberPolicy}.
 */
public class ToNumberPolicyTest {

    @Test
    public void longOrDoublePolicy_withInvalidNumberString_shouldThrowJsonParseException() throws IOException {
        ToNumberPolicy policy = ToNumberPolicy.LONG_OR_DOUBLE;
        JsonReader reader = new JsonReader(new StringReader("\"not-a-number\""));

        try {
            policy.readNumber(reader);
            fail("Expected a JsonParseException to be thrown for non-numeric input.");
        } catch (JsonParseException expected) {
            assertEquals("Cannot parse not-a-number; at path $", expected.getMessage());
        }
    }

    @Test
    public void bigDecimalPolicy_withInvalidNumberString_shouldThrowJsonParseException() throws IOException {
        ToNumberPolicy policy = ToNumberPolicy.BIG_DECIMAL;
        JsonReader reader = new JsonReader(new StringReader("\"not-a-number\""));

        try {
            policy.readNumber(reader);
            fail("Expected a JsonParseException to be thrown for non-numeric input.");
        } catch (JsonParseException expected) {
            assertEquals("Cannot parse not-a-number; at path $", expected.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void doublePolicy_withNullReader_shouldThrowNullPointerException() throws IOException {
        ToNumberPolicy.DOUBLE.readNumber(null);
    }

    @Test(expected = NullPointerException.class)
    public void lazilyParsedNumberPolicy_withNullReader_shouldThrowNullPointerException() throws IOException {
        ToNumberPolicy.LAZILY_PARSED_NUMBER.readNumber(null);
    }

    @Test(expected = NullPointerException.class)
    public void longOrDoublePolicy_withNullReader_shouldThrowNullPointerException() throws IOException {
        ToNumberPolicy.LONG_OR_DOUBLE.readNumber(null);
    }

    @Test(expected = NullPointerException.class)
    public void bigDecimalPolicy_withNullReader_shouldThrowNullPointerException() throws IOException {
        ToNumberPolicy.BIG_DECIMAL.readNumber(null);
    }
}