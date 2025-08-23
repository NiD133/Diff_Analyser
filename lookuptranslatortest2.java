package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

public class LookupTranslatorTestTest2 extends AbstractLangTest {

    // Tests: https://issues.apache.org/jira/browse/LANG-882
    @Test
    void testLang882() throws IOException {
        final LookupTranslator lt = new LookupTranslator(new CharSequence[][] { { new StringBuffer("one"), new StringBuffer("two") } });
        final StringWriter out = new StringWriter();
        final int result = lt.translate(new StringBuffer("one"), 0, out);
        assertEquals(3, result, "Incorrect code point consumption");
        assertEquals("two", out.toString(), "Incorrect value");
    }
}
