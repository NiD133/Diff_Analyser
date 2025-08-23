package com.fasterxml.jackson.core.io;

import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class NumberOutputTestTest1 {

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
    void intPrinting() throws Exception {
        assertIntPrint(0);
        assertIntPrint(-3);
        assertIntPrint(1234);
        assertIntPrint(-1234);
        assertIntPrint(56789);
        assertIntPrint(-56789);
        assertIntPrint(999999);
        assertIntPrint(-999999);
        assertIntPrint(1000000);
        assertIntPrint(-1000000);
        assertIntPrint(10000001);
        assertIntPrint(-10000001);
        assertIntPrint(-100000012);
        assertIntPrint(100000012);
        assertIntPrint(1999888777);
        assertIntPrint(-1999888777);
        assertIntPrint(Integer.MAX_VALUE);
        assertIntPrint(Integer.MIN_VALUE);
        Random rnd = new Random(12345L);
        for (int i = 0; i < 251000; ++i) {
            assertIntPrint(rnd.nextInt());
        }
    }
}
