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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.apache.commons.lang3.text.translate.LookupTranslator}.
 */
@Deprecated
class LookupTranslatorTest extends AbstractLangTest {

    private static final String INPUT_TEXT = "one";
    private static final String EXPECTED_TRANSLATION = "two";
    private static final int EXPECTED_CHARACTERS_CONSUMED = 3;
    private static final int START_INDEX = 0;

    @Test
    void testBasicLookup_ShouldTranslateStringUsingLookupTable() throws IOException {
        // Given: A translator with a simple lookup mapping "one" -> "two"
        final CharSequence[][] lookupMapping = { { INPUT_TEXT, EXPECTED_TRANSLATION } };
        final LookupTranslator translator = new LookupTranslator(lookupMapping);
        final StringWriter outputWriter = new StringWriter();
        
        // When: Translating the input text starting at index 0
        final int charactersConsumed = translator.translate(INPUT_TEXT, START_INDEX, outputWriter);
        
        // Then: The translator should consume 3 characters and output "two"
        assertEquals(EXPECTED_CHARACTERS_CONSUMED, charactersConsumed, 
                    "Should consume all 3 characters of 'one'");
        assertEquals(EXPECTED_TRANSLATION, outputWriter.toString(), 
                    "Should translate 'one' to 'two'");
    }

    /**
     * Tests fix for LANG-882: LookupTranslator should work with StringBuffer objects.
     * This ensures that the translator properly handles CharSequence implementations
     * other than String by converting keys to String for HashMap compatibility.
     */
    @Test
    void testStringBufferCompatibility_ShouldHandleStringBufferInputAndMapping() throws IOException {
        // Given: A translator with StringBuffer objects in the lookup mapping
        final CharSequence[][] lookupMapping = { 
            { new StringBuffer(INPUT_TEXT), new StringBuffer(EXPECTED_TRANSLATION) } 
        };
        final LookupTranslator translator = new LookupTranslator(lookupMapping);
        final StringWriter outputWriter = new StringWriter();
        
        // When: Translating a StringBuffer input
        final StringBuffer inputBuffer = new StringBuffer(INPUT_TEXT);
        final int charactersConsumed = translator.translate(inputBuffer, START_INDEX, outputWriter);
        
        // Then: The translator should work the same as with String objects
        assertEquals(EXPECTED_CHARACTERS_CONSUMED, charactersConsumed, 
                    "Should consume all 3 characters of StringBuffer 'one'");
        assertEquals(EXPECTED_TRANSLATION, outputWriter.toString(), 
                    "Should translate StringBuffer 'one' to 'two'");
    }
}