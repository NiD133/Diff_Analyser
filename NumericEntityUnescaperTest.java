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
 * 
 * NumericEntityUnescaper translates XML numeric entities (like &#65; or &#x41;) 
 * back to their corresponding characters.
 */
@Deprecated
class NumericEntityUnescaperTest extends AbstractLangTest {

    // Test data constants for better maintainability
    private static final String SAMPLE_TEXT_PREFIX = "Test ";
    private static final String HEX_ENTITY_48_INCOMPLETE = "&#x30";
    private static final String EXPECTED_CHAR_48 = "\u0030"; // Character '0'
    private static final String SUPPLEMENTARY_ENTITY = "&#68642;";
    private static final String SUPPLEMENTARY_CHAR = "\uD803\uDC22"; // Unicode supplementary character

    @Test
    void testIgnoresIncompleteEntitiesAtEndOfString() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test various incomplete entity patterns at string end
        assertEntityIgnored(unescaper, "Test &", "single ampersand at end");
        assertEntityIgnored(unescaper, "Test &#", "incomplete decimal entity at end");
        assertEntityIgnored(unescaper, "Test &#x", "incomplete hex entity (lowercase) at end");
        assertEntityIgnored(unescaper, "Test &#X", "incomplete hex entity (uppercase) at end");
    }

    @Test
    void testUnescapesSupplementaryUnicodeCharacters() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        String actualResult = unescaper.translate(SUPPLEMENTARY_ENTITY);
        
        assertEquals(SUPPLEMENTARY_CHAR, actualResult, 
            "Should correctly unescape numeric entities representing supplementary Unicode characters");
    }

    @Test
    void testHandlesEntitiesMissingSemicolon() {
        String inputWithMissingSemicolon = SAMPLE_TEXT_PREFIX + HEX_ENTITY_48_INCOMPLETE + " not test";
        String expectedWhenParsed = SAMPLE_TEXT_PREFIX + EXPECTED_CHAR_48 + " not test";

        testSemicolonOptionalBehavior(inputWithMissingSemicolon, expectedWhenParsed);
        testDefaultIgnoreBehavior(inputWithMissingSemicolon);
        testErrorOnMissingSemicolonBehavior(inputWithMissingSemicolon);
    }

    private void testSemicolonOptionalBehavior(String input, String expected) {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(
            NumericEntityUnescaper.OPTION.semiColonOptional);
        
        String result = unescaper.translate(input);
        
        assertEquals(expected, result, 
            "With semiColonOptional, should parse entities even when semicolon is missing");
    }

    private void testDefaultIgnoreBehavior(String input) {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        String result = unescaper.translate(input);
        
        assertEquals(input, result, 
            "By default, should ignore entities with missing semicolons (leave unchanged)");
    }

    private void testErrorOnMissingSemicolonBehavior(String input) {
        final NumericEntityUnescaper strictUnescaper = new NumericEntityUnescaper(
            NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        
        assertIllegalArgumentException(() -> strictUnescaper.translate(input),
            "With errorIfNoSemiColon option, should throw exception when semicolon is missing");
    }

    /**
     * Helper method to assert that an entity pattern is ignored (left unchanged).
     */
    private void assertEntityIgnored(NumericEntityUnescaper unescaper, String input, String scenario) {
        String result = unescaper.translate(input);
        assertEquals(input, result, "Should ignore " + scenario);
    }
}