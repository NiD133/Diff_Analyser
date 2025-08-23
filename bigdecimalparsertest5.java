package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import ch.randelshofer.fastdoubleparser.JavaBigDecimalParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BigDecimalParserTestTest5 extends com.fasterxml.jackson.core.JUnit5TestBase {

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
    void issueDatabind4694() {
        final String str = "-11000.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        final BigDecimal expected = new BigDecimal(str);
        assertEquals(expected, JavaBigDecimalParser.parseBigDecimal(str));
        assertEquals(expected, BigDecimalParser.parse(str));
        assertEquals(expected, BigDecimalParser.parseWithFastParser(str));
        final char[] arr = str.toCharArray();
        assertEquals(expected, BigDecimalParser.parse(arr, 0, arr.length));
        assertEquals(expected, BigDecimalParser.parseWithFastParser(arr, 0, arr.length));
    }
}
