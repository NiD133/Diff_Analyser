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

    /**
     * Tests basic translation functionality with String inputs.
     */
    @Test
    void translate_StringInput_TranslatesCorrectly() throws IOException {
        // Arrange: Prepare translator with mapping and output buffer
        final LookupTranslator translator = new LookupTranslator(
            new CharSequence[][]{{"one", "two"}}
        );
        final StringWriter outputWriter = new StringWriter();

        // Act: Perform translation
        final int consumedCodePoints = translator.translate("one", 0, outputWriter);

        // Assert: Verify consumed characters and output
        assertEquals(3, consumedCodePoints, "Should consume entire input length");
        assertEquals("two", outputWriter.toString(), "Should produce correct translation");
    }

    /**
     * Tests translation with StringBuffer inputs, verifying fix for LANG-882.
     * @see <a href="https://issues.apache.org/jira/browse/LANG-882">LANG-882</a>
     */
    @Test
    void translate_StringBufferInput_HandlesDifferentCharSequenceTypes() throws IOException {
        // Arrange: Prepare translator with StringBuffer-based mapping
        final CharSequence[][] lookupTable = {
            {new StringBuffer("one"), new StringBuffer("two")}
        };
        final LookupTranslator translator = new LookupTranslator(lookupTable);
        final StringWriter outputWriter = new StringWriter();

        // Act: Perform translation with StringBuffer input
        final int consumedCodePoints = translator.translate(
            new StringBuffer("one"), 0, outputWriter
        );

        // Assert: Verify consumed characters and output
        assertEquals(3, consumedCodePoints, "Should consume entire input length");
        assertEquals("two", outputWriter.toString(), "Should produce correct translation");
    }

}