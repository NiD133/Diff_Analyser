package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.junit.Test;

/**
 * This test class has been improved for understandability.
 * The original test was auto-generated and lacked clarity.
 *
 * This version focuses on providing a clear, descriptive test case for the
 * {@link CodeAttribute#setAttributeName(CPUTF8)} method.
 */
// The original class name and scaffolding are kept for context.
public class CodeAttribute_ESTestTest24 extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that the static {@code setAttributeName} method can be called with a
     * null argument without throwing an exception.
     *
     * This test ensures that the static state of the CodeAttribute class can be
     * set to null. The primary assertion is implicit: the method call should not fail.
     */
    @Test(timeout = 4000)
    public void setAttributeNameShouldAcceptNullArgument() {
        // The purpose of this test is to ensure that passing null to the static
        // setAttributeName method does not cause a NullPointerException or any other exception.
        // The test's success is determined by its completion without throwing an exception.
        CodeAttribute.setAttributeName(null);
    }
}