package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link CoreOperation} class, focusing on its contract
 * and behavior when used with invalid arguments.
 */
public class CoreOperationTest {

    /**
     * Tests that calling compute() on a CoreOperation with null arguments
     * and a null EvalContext throws a NullPointerException.
     * <p>
     * This behavior is tested via the {@link NameAttributeTest} class, a concrete
     * subclass of {@link CoreOperationCompare}, which in turn extends {@link CoreOperation}.
     * The exception is expected because the underlying implementation in
     * {@link CoreOperationCompare} does not perform a null-check on its arguments
     * before using them.
     */
    @Test
    public void computeWithNullArgumentsAndContextShouldThrowNullPointerException() {
        // Arrange: Create an operation with null arguments. Since CoreOperation is
        // abstract, we must use a concrete subclass for instantiation.
        CoreOperation operationWithNullArgs = new NameAttributeTest(null, null);
        EvalContext nullContext = null;

        // Act & Assert
        try {
            operationWithNullArgs.compute(nullContext);
            fail("Expected a NullPointerException because the operation was initialized with null arguments.");
        } catch (NullPointerException e) {
            // This is the expected outcome.
            // We can also assert that the exception has no message, which is typical
            // for a direct null dereference and consistent with the original test's findings.
            assertNull("The NullPointerException should not have a message.", e.getMessage());
        }
    }
}