package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class SegmentConstantPoolTestTest3 {

    String[] testClassArray = { "Object", "Object", "java/lang/String", "java/lang/String", "Object", "Other" };

    String[] testMethodArray = { "<init>()", "clone()", "equals()", "<init>", "isNull()", "Other" };

    public class MockSegmentConstantPool extends SegmentConstantPool {

        MockSegmentConstantPool() {
            super(new CpBands(new Segment()));
        }

        @Override
        public int matchSpecificPoolEntryIndex(final String[] classNameArray, final String desiredClassName, final int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, desiredClassName, desiredIndex);
        }

        @Override
        public int matchSpecificPoolEntryIndex(final String[] classNameArray, final String[] methodNameArray, final String desiredClassName, final String desiredMethodRegex, final int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, methodNameArray, desiredClassName, desiredMethodRegex, desiredIndex);
        }

        public boolean regexMatchesVisible(final String regexString, final String compareString) {
            return SegmentConstantPool.regexMatches(regexString, compareString);
        }
    }

    @Test
    void testRegexReplacement() {
        final MockSegmentConstantPool mockPool = new MockSegmentConstantPool();
        assertTrue(mockPool.regexMatchesVisible(".*", "anything"));
        assertTrue(mockPool.regexMatchesVisible(".*", ""));
        assertTrue(mockPool.regexMatchesVisible("^<init>.*", "<init>"));
        assertTrue(mockPool.regexMatchesVisible("^<init>.*", "<init>stuff"));
        assertFalse(mockPool.regexMatchesVisible("^<init>.*", "init>stuff"));
        assertFalse(mockPool.regexMatchesVisible("^<init>.*", "<init"));
        assertFalse(mockPool.regexMatchesVisible("^<init>.*", ""));
    }
}
