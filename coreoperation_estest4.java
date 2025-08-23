package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CoreOperationUnion} class.
 */
public class CoreOperationUnionTest {

    /**
     * Tests that calling compute() on a CoreOperationUnion with no expressions
     * returns a UnionContext that does not require child ordering.
     * The 'isChildOrderingRequired' property is inherent to UnionContext and should be false.
     */
    @Test
    public void computeWithNoExpressionsReturnsContextNotRequiringChildOrdering() {
        // Arrange: Create a union operation with an empty array of expressions.
        Expression[] noExpressions = new Expression[0];
        CoreOperationUnion unionOperation = new CoreOperationUnion(noExpressions);

        // A simple, dummy evaluation context is sufficient for this test.
        EvalContext dummyContext = new RootContext(null, null);

        // Act: Compute the result of the union operation.
        Object result = unionOperation.compute(dummyContext);

        // Assert: The result should be a UnionContext instance that does not require child ordering.
        assertNotNull("The computed result should not be null.", result);
        assertTrue("The result should be an instance of UnionContext.", result instanceof UnionContext);

        UnionContext resultContext = (UnionContext) result;
        assertFalse("A union of zero expressions should not require child ordering.",
                    resultContext.isChildOrderingRequired());
    }
}