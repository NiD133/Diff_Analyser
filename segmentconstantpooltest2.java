package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class SegmentConstantPoolTestTest2 {

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
    void testMatchSpecificPoolEntryIndex_SingleArray() {
        final MockSegmentConstantPool mockInstance = new MockSegmentConstantPool();
        // Elements should be found at the proper position.
        assertEquals(0, mockInstance.matchSpecificPoolEntryIndex(testClassArray, "Object", 0));
        assertEquals(1, mockInstance.matchSpecificPoolEntryIndex(testClassArray, "Object", 1));
        assertEquals(2, mockInstance.matchSpecificPoolEntryIndex(testClassArray, "java/lang/String", 0));
        assertEquals(3, mockInstance.matchSpecificPoolEntryIndex(testClassArray, "java/lang/String", 1));
        assertEquals(4, mockInstance.matchSpecificPoolEntryIndex(testClassArray, "Object", 2));
        assertEquals(5, mockInstance.matchSpecificPoolEntryIndex(testClassArray, "Other", 0));
        // Elements that don't exist shouldn't be found
        assertEquals(-1, mockInstance.matchSpecificPoolEntryIndex(testClassArray, "NotThere", 0));
        // Elements that exist but don't have the requisite number
        // of hits shouldn't be found.
        assertEquals(-1, mockInstance.matchSpecificPoolEntryIndex(testClassArray, "java/lang/String", 2));
    }
}
