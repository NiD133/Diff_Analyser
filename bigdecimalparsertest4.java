package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BigDecimalParserTestTest4 extends com.fasterxml.jackson.core.JUnit5TestBase {

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
    void longValidStringFastParse() {
        String num = genLongValidString(500);
        final BigDecimal EXP = new BigDecimal(num);
        // Parse from String first, then char[]
        assertEquals(EXP, BigDecimalParser.parseWithFastParser(num));
        assertEquals(EXP, BigDecimalParser.parseWithFastParser(num.toCharArray(), 0, num.length()));
    }
}
