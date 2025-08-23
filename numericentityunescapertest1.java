package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

public class NumericEntityUnescaperTestTest1 extends AbstractLangTest {

    @Test
    void testOutOfBounds() {
        final NumericEntityUnescaper neu = new NumericEntityUnescaper();
        assertEquals("Test &", neu.translate("Test &"), "Failed to ignore when last character is &");
        assertEquals("Test &#", neu.translate("Test &#"), "Failed to ignore when last character is &");
        assertEquals("Test &#x", neu.translate("Test &#x"), "Failed to ignore when last character is &");
        assertEquals("Test &#X", neu.translate("Test &#X"), "Failed to ignore when last character is &");
    }
}
