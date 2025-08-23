package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

public class NumericEntityUnescaperTestTest3 extends AbstractLangTest {

    @Test
    void testUnfinishedEntity() {
        // parse it
        NumericEntityUnescaper neu = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        String input = "Test &#x30 not test";
        String expected = "Test \u0030 not test";
        String result = neu.translate(input);
        assertEquals(expected, result, "Failed to support unfinished entities (i.e. missing semicolon)");
        // ignore it
        neu = new NumericEntityUnescaper();
        input = "Test &#x30 not test";
        expected = input;
        result = neu.translate(input);
        assertEquals(expected, result, "Failed to ignore unfinished entities (i.e. missing semicolon)");
        // fail it
        final NumericEntityUnescaper failingNeu = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        final String failingInput = "Test &#x30 not test";
        assertIllegalArgumentException(() -> failingNeu.translate(failingInput));
    }
}
