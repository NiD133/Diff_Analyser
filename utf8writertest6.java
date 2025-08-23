package com.fasterxml.jackson.core.io;

import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UTF8WriterTestTest6 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private IOContext _ioContext() {
        return testIOContext();
    }

    // @since 2.17
    @Test
    void surrogateConversion() {
        for (int first = UTF8Writer.SURR1_FIRST; first <= UTF8Writer.SURR1_LAST; first++) {
            for (int second = UTF8Writer.SURR2_FIRST; second <= UTF8Writer.SURR2_LAST; second++) {
                int expected = 0x10000 + ((first - UTF8Writer.SURR1_FIRST) << 10) + (second - UTF8Writer.SURR2_FIRST);
                int actual = (first << 10) + second + UTF8Writer.SURROGATE_BASE;
                if (expected != actual) {
                    fail("Mismatch on: " + Integer.toHexString(first) + " " + Integer.toHexString(second) + "; expected: " + expected + ", actual: " + actual);
                }
            }
        }
    }
}
