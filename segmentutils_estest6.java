package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import org.evosuite.runtime.EvoRunner;
import org.junit.runner.RunWith;

// The EvoSuite runner and scaffolding are kept to maintain the existing test execution environment.
@RunWith(EvoRunner.class)
public class SegmentUtils_ESTestTest6 extends SegmentUtils_ESTest_scaffolding {

    /**
     * Tests that countInvokeInterfaceArgs throws a NullPointerException when the
     * method descriptor string is null.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void countInvokeInterfaceArgsShouldThrowNullPointerExceptionForNullDescriptor() {
        // This call is expected to throw a NullPointerException, which is verified
        // by the 'expected' attribute of the @Test annotation.
        SegmentUtils.countInvokeInterfaceArgs(null);
    }
}