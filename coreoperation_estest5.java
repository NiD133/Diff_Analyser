package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

/**
 * Contains tests for the abstract class {@link CoreOperation}.
 * These tests verify the behavior of concrete methods implemented in the abstract base class.
 */
public class CoreOperationTest {

    /**
     * Tests that {@link CoreOperation#toString()} throws a NullPointerException
     * when the operation is constructed with null arguments.
     * <p>
     * The {@code toString()} method in the abstract {@code CoreOperation} class
     * is expected to access its internal expression arguments. If these arguments
     * are null, a {@code NullPointerException} should be thrown, indicating
     * that the operation was not properly initialized.
     */
    @Test(expected = NullPointerException.class)
    public void toString_withNullArguments_throwsNullPointerException() {
        // Since CoreOperation is abstract, we use a concrete subclass to test its
        // non-abstract methods. NameAttributeTest is a simple two-argument operation.
        // We pass null for its expression arguments to trigger the condition under test.
        NameAttributeTest operationWithNullArgs = new NameAttributeTest(null, null);

        // This call is expected to throw a NullPointerException because the
        // toString() implementation in CoreOperation attempts to dereference its null arguments.
        operationWithNullArgs.toString();
    }
}