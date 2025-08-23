package com.fasterxml.jackson.core.io;

import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class NumberOutputTestTest3 {

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
    void divBy1000Small() {
        for (int number = 0; number <= 999_999; ++number) {
            int expected = number / 1000;
            int actual = NumberOutput.divBy1000(number);
            if (expected != actual) {
                // only construct String if fail
                fail("With " + number + " should get " + expected + ", got: " + actual);
            }
        }
    }
}
