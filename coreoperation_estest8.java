package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the abstract class {@link CoreOperation}, focusing on
 * its core contract and behavior in edge cases.
 */
public class CoreOperationTest {

    /**
     * Verifies that calling compute() on a CoreOperation with an empty argument array
     * throws an ArrayIndexOutOfBoundsException.
     * <p>
     * This test simulates an invalid state by manually replacing the arguments of a
     * concrete CoreOperation subclass. The superclass, CoreOperationCompare, is expected
     * to fail because it directly accesses arguments by index without checking if they exist.
     */
    @Test
    public void computeWithNoArgumentsShouldThrowException() {
        // Arrange: Create a concrete CoreOperation instance in an invalid state.
        // We use NameAttributeTest as it's a simple subclass of CoreOperation.
        // The initial constructor arguments are irrelevant since we overwrite the 'args' field.
        NameAttributeTest operationWithNoArguments = new NameAttributeTest(null, null);

        // Manually set the internal 'args' array to be empty. This is the specific
        // invalid condition we want to test.
        operationWithNoArguments.args = new Expression[0];

        // Act & Assert: Verify that calling compute() triggers the expected exception.
        // The compute() method in the CoreOperationCompare superclass assumes at least
        // one argument is present, so it should fail with an out-of-bounds exception.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            // The EvalContext can be null because the exception is thrown before it is used.
            operationWithNoArguments.compute(null);
        });
    }
}