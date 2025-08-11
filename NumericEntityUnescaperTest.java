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

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.apache.commons.lang3.text.translate.NumericEntityUnescaper}.
 */
@Deprecated
class NumericEntityUnescaperTest extends AbstractLangTest {

    /**
     * Tests that incomplete numeric entities at the end of input are ignored.
     */
    @Test
    void shouldIgnoreIncompleteEntitiesAtEndOfInput() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        assertEquals("Test &", unescaper.translate("Test &"));
        assertEquals("Test &#", unescaper.translate("Test &#"));
        assertEquals("Test &#x", unescaper.translate("Test &#x"));
        assertEquals("Test &#X", unescaper.translate("Test &#X"));
    }

    /**
     * Tests that supplementary characters (those requiring surrogate pairs) are correctly unescaped.
     */
    @Test
    void shouldUnescapeSupplementaryCharacter() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "&#68642;";
        final String expected = "\uD803\uDC22";  // Supplementary character U+10C22

        final String result = unescaper.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Tests that entities without semicolons are processed when the semiColonOptional option is set.
     */
    @Test
    void shouldProcessUnfinishedEntityWhenSemicolonOptional() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        final String input = "Test &#x30 not test";
        final String expected = "Test \u0030 not test";  // '0' character

        final String result = unescaper.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Tests that entities without semicolons are ignored by default (without options).
     */
    @Test
    void shouldIgnoreUnfinishedEntityByDefault() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "Test &#x30 not test";

        final String result = unescaper.translate(input);
        assertEquals(input, result);  // Input should remain unchanged
    }

    /**
     * Tests that an exception is thrown for unfinished entities when errorIfNoSemiColon option is set.
     */
    @Test
    void shouldThrowExceptionForUnfinishedEntityWhenErrorOptionSet() {
        final NumericEntityUnescaper unescaper = 
            new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        final String input = "Test &#x30 not test";

        assertIllegalArgumentException(() -> unescaper.translate(input));
    }
}