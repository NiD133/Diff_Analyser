package org.apache.commons.codec.language.bm;

import org.junit.Test;
import java.util.Collections;

/**
 * This test class contains an improved test case for the PhoneticEngine.
 * The original test was an auto-generated test that was difficult to understand.
 */
public class PhoneticEngineImprovedTest {

    /**
     * Tests that the {@link PhoneticEngine#encode(String, Languages.LanguageSet)} method
     * throws an exception when a rule's pattern is longer than the remaining input.
     * <p>
     * This scenario can lead to a {@link StringIndexOutOfBoundsException} because the engine
     * may attempt to read beyond the bounds of the input string when looking for the
     * longest matching rule pattern.
     * </p>
     * <p>
     * This test simplifies a complex, auto-generated test case into a minimal, focused
     * example that clearly demonstrates the bug.
     * </p>
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void encodeShouldThrowIndexOutOfBoundsWhenRulePatternExceedsInputLength() {
        // ARRANGE
        // The SEPHARDIC name type and EXACT rule type are chosen as they possess rules
        // that can trigger this boundary condition with a very short input.
        final NameType nameType = NameType.SEPHARDIC;
        final RuleType ruleType = RuleType.EXACT;
        final boolean concat = false;
        final PhoneticEngine engine = new PhoneticEngine(nameType, ruleType, concat);

        // An empty language set is used, as the specific languages are not relevant to this bug.
        final Languages.LanguageSet languageSet = Languages.LanguageSet.from(Collections.emptySet());

        // The input "rp" is intentionally short. When the engine processes this input,
        // it looks for rules starting with 'r'. If a rule exists with a pattern
        // longer than "rp" (e.g., "rus"), the engine will attempt to read a substring
        // that is out of bounds. This minimal input replaces a very long and confusing
        // string from the original auto-generated test.
        final String shortInputThatTriggersError = "rp";

        // ACT & ASSERT
        // The encode method is called. The @Test(expected=...) annotation asserts that
        // a StringIndexOutOfBoundsException is thrown, making the test's purpose explicit.
        engine.encode(shortInputThatTriggersError, languageSet);
    }
}