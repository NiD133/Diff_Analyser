package org.jfree.data.general;

import org.junit.Test;

/**
 * This test suite focuses on the behavior of the DefaultPieDataset class,
 * particularly its handling of invalid arguments.
 */
// The original test class name and inheritance from a scaffolding class are preserved.
// For a new test suite, a more conventional name would be DefaultPieDatasetTest.
public class DefaultPieDataset_ESTestTest19 extends DefaultPieDataset_ESTest_scaffolding {

    /**
     * Verifies that the getIndex() method throws an IllegalArgumentException
     * when a null key is provided, as specified by the method's contract.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getIndex_whenKeyIsNull_throwsIllegalArgumentException() {
        // Arrange: Create an empty dataset.
        DefaultPieDataset<Integer> dataset = new DefaultPieDataset<>();

        // Act: Attempt to get the index of a null key.
        // The @Test annotation asserts that this action must throw an
        // IllegalArgumentException for the test to pass.
        dataset.getIndex(null);
    }
}