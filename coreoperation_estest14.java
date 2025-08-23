package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

/**
 * Contains tests for the abstract class {@link CoreOperation} and its subclasses.
 */
public class CoreOperationTest {

    /**
     * Tests that calling computeValue on a CoreOperation with a null EvalContext
     * throws a NullPointerException.
     *
     * This behavior is fundamental, as most operations require a valid context
     * to access data or functions. We test this using a CoreOperationUnion,
     * which delegates computation to its arguments. The union is set up with a
     * CoreFunction argument, which is known to dereference the context and thus
     * trigger the expected NPE.
     */
    @Test(expected = NullPointerException.class)
    public void computeValueWithNullContextThrowsNullPointerException() {
        // Arrange: Create a union operation with an argument that requires a valid context.
        // A CoreFunction is a simple choice, as it will attempt to access the context
        // to resolve the function, causing an NPE if the context is null.
        Expression[] arguments = {new CoreFunction(0, new Expression[0])};
        CoreOperation unionOperation = new CoreOperationUnion(arguments);

        // Act: Attempt to compute the value with a null context.
        // This is expected to throw a NullPointerException.
        unionOperation.computeValue(null);
    }
}