/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LookupTranslator}.
 *
 * Test goals:
 * - When a key is found at the given index, translate writes the mapped value and
 *   returns the number of consumed code units (the key length).
 * - Non-String CharSequence inputs and lookup entries are supported (LANG-882).
 */
@Deprecated
class LookupTranslatorTest extends AbstractLangTest {

    private static final CharSequence KEY = "one";
    private static final CharSequence VALUE = "two";
    private static final int START_INDEX = 0;
    private static final int CONSUMED_LENGTH = KEY.length(); // 3

    /**
     * Helper that asserts a single-key translation.
     */
    private void assertTranslates(final CharSequence mapKey,
                                  final CharSequence mapValue,
                                  final CharSequence input) throws IOException {
        // Arrange
        final LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { mapKey, mapValue } });
        final StringWriter sink = new StringWriter();

        // Act
        final int consumed = translator.translate(input, START_INDEX, sink);

        // Assert
        assertEquals(CONSUMED_LENGTH, consumed, "translate should consume the length of the matched key");
        assertEquals(mapValue.toString(), sink.toString(), "translate should write the mapped value");
    }

    @Test
    @DisplayName("Translates String->String and returns consumed length")
    void translatesStringInput() throws IOException {
        assertTranslates(KEY, VALUE, KEY);
    }

    // Tests: https://issues.apache.org/jira/browse/LANG-882
    @Test
    @DisplayName("Supports non-String CharSequence for both lookup entries and input (LANG-882)")
    void translatesNonStringCharSequences() throws IOException {
        final CharSequence keyBuffer = new StringBuffer(KEY);
        final CharSequence valueBuffer = new StringBuffer(VALUE);
        final CharSequence inputBuffer = new StringBuffer(KEY);

        assertTranslates(keyBuffer, valueBuffer, inputBuffer);
    }
}