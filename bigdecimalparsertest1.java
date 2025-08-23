package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BigDecimalParserTestTest1 extends com.fasterxml.jackson.core.JUnit5TestBase {

    static String genLongInvalidString() {
        final int len = 1500;
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append("A");
        }
        return sb.toString();
    }

    static String genLongValidString(int len) {
        final StringBuilder sb = new StringBuilder(len + 5);
        sb.append("0.");
        for (int i = 0; i < len; i++) {
            sb.append('0');
        }
        sb.append('1');
        return sb.toString();
    }

    @Test
    void longInvalidStringParse() {
        try {
            BigDecimalParser.parse(genLongInvalidString());
            fail("expected NumberFormatException");
        } catch (NumberFormatException nfe) {
            assertTrue(nfe.getMessage().startsWith("Value \"AAAAA"), "exception message starts as expected?");
            assertTrue(nfe.getMessage().contains("truncated"), "exception message value contains truncated");
        }
    }
}
