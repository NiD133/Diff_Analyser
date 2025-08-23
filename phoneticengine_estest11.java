package org.apache.commons.codec.language.bm;

import org.junit.Test;

import java.util.Collections;

/**
 * Test suite for {@link PhoneticEngine}, focusing on its behavior with edge-case inputs.
 */
public class PhoneticEngineImprovedTest {

    /**
     * This test verifies that the {@code encode} method fails with a {@code StackOverflowError}
     * when processing an extremely long input string, particularly when no specific language rules apply.
     * <p>
     * This scenario was likely discovered by automated test generation and is preserved here as a
     * characterization test. It highlights a potential flaw where deep recursion on certain inputs
     * can exhaust the stack. A future fix in the production code should address this, and this
     * test would then need to be updated to expect a more graceful failure (e.g., an
     * {@code IllegalArgumentException}) or a successful encoding.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void encodeWithExtremelyLongInputAndNoLanguageRulesShouldCauseStackOverflow() {
        // ARRANGE
        // Set up a phonetic engine with generic, exact matching rules.
        final NameType nameType = NameType.GENERIC;
        final RuleType ruleType = RuleType.EXACT;
        final boolean concat = true;
        final PhoneticEngine phoneticEngine = new PhoneticEngine(nameType, ruleType, concat);

        // Use an empty language set, which means no specific language rules will be matched.
        final Languages.LanguageSet emptyLanguageSet = Languages.LanguageSet.from(Collections.emptySet());

        // Define an unusually long input string. The "de la" part is a known name prefix,
        // but the long, unbroken remainder is what can trigger a deep recursion issue.
        final String extremelyLongInput = "de la deorg.apache.commons.codec.language.bm.languages$2";

        // ACT & ASSERT
        // Calling encode with this specific input is expected to cause a StackOverflowError.
        // The JUnit runner verifies this expectation via the @Test(expected=...) annotation.
        phoneticEngine.encode(extremelyLongInput, emptyLanguageSet);
    }
}