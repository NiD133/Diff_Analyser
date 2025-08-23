package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

public class LookupTranslatorTestTest1 extends AbstractLangTest {

    @Test
    void testBasicLookup() throws IOException {
        final LookupTranslator lt = new LookupTranslator(new CharSequence[][] { { "one", "two" } });
        final StringWriter out = new StringWriter();
        final int result = lt.translate("one", 0, out);
        assertEquals(3, result, "Incorrect code point consumption");
        assertEquals("two", out.toString(), "Incorrect value");
    }
}
