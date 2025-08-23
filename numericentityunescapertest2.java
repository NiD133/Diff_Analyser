package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

public class NumericEntityUnescaperTestTest2 extends AbstractLangTest {

    @Test
    void testSupplementaryUnescaping() {
        final NumericEntityUnescaper neu = new NumericEntityUnescaper();
        final String input = "&#68642;";
        final String expected = "\uD803\uDC22";
        final String result = neu.translate(input);
        assertEquals(expected, result, "Failed to unescape numeric entities supplementary characters");
    }
}
