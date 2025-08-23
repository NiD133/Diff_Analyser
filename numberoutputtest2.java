package com.fasterxml.jackson.core.io;

import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class NumberOutputTestTest2 {

    private void assertIntPrint(int value) {
        String exp = "" + value;
        String act = printToString(value);
        if (!exp.equals(act)) {
            assertEquals(exp, act, "Expected conversion (exp '" + exp + "', len " + exp.length() + "; act len " + act.length() + ")");
        }
        String alt = NumberOutput.toString(value);
        if (!exp.equals(alt)) {
            assertEquals(exp, act, "Expected conversion (exp '" + exp + "', len " + exp.length() + "; act len " + act.length() + ")");
        }
    }

    private void assertLongPrint(long value, int index) {
        String exp = "" + value;
        String act = printToString(value);
        if (!exp.equals(act)) {
            assertEquals(exp, act, "Expected conversion (exp '" + exp + "', len " + exp.length() + "; act len " + act.length() + "; number index " + index + ")");
        }
        String alt = NumberOutput.toString(value);
        if (!exp.equals(alt)) {
            assertEquals(exp, act, "Expected conversion (exp '" + exp + "', len " + exp.length() + "; act len " + act.length() + "; number index " + index + ")");
        }
    }

    private String printToString(int value) {
        char[] buffer = new char[12];
        int offset = NumberOutput.outputInt(value, buffer, 0);
        return new String(buffer, 0, offset);
    }

    private String printToString(long value) {
        char[] buffer = new char[22];
        int offset = NumberOutput.outputLong(value, buffer, 0);
        return new String(buffer, 0, offset);
    }

    @Test
    void longPrinting() throws Exception {
        // First, let's just cover couple of edge cases
        assertLongPrint(0L, 0);
        assertLongPrint(1L, 0);
        assertLongPrint(-1L, 0);
        assertLongPrint(Long.MAX_VALUE, 0);
        assertLongPrint(Long.MIN_VALUE, 0);
        assertLongPrint(Long.MAX_VALUE - 1L, 0);
        assertLongPrint(Long.MIN_VALUE + 1L, 0);
        Random rnd = new Random(12345L);
        // Bigger value space, need more iterations for long
        for (int i = 0; i < 678000; ++i) {
            long l = ((long) rnd.nextInt() << 32) | rnd.nextInt();
            assertLongPrint(l, i);
        }
    }
}
