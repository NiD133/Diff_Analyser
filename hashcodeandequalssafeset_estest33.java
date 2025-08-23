package org.mockito.internal.util.collections;

import org.junit.Test;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 */
// The runner and scaffolding inheritance are preserved from the original generated test structure.
@org.junit.runner.RunWith(org.evosuite.runtime.EvoRunner.class)
@org.evosuite.runtime.EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true)
public class HashCodeAndEqualsSafeSet_ESTestTest33 extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    /**
     * Verifies that the clone() method is not supported and correctly
     * throws a CloneNotSupportedException, as specified by its contract.
     */
    @Test(expected = CloneNotSupportedException.class)
    public void cloneShouldThrowCloneNotSupportedException() throws CloneNotSupportedException {
        // Arrange: Create a set instance. The contents are not relevant for this test.
        Object[] elements = {new Object()};
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(elements);

        // Act: Attempt to clone the set.
        // Assert: The @Test(expected=...) annotation asserts that a
        // CloneNotSupportedException is thrown. The test will fail if any other
        // exception is thrown or if no exception is thrown at all.
        safeSet.clone();
    }
}