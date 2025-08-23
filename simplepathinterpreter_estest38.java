package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.CoreFunction;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NameAttributeTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains improved tests for the {@link SimplePathInterpreter} class, focusing on understandability.
 */
public class SimplePathInterpreterTest {

    /**
     * Tests that interpretSimpleExpressionPath throws a RuntimeException when a predicate
     * contains a function call with an incorrect number of arguments.
     * <p>
     * The test constructs a predicate equivalent to XPath's {@code [@name=substring('arg1')]}.
     * This is invalid because the 'substring' function requires at least two arguments.
     * The interpreter is expected to fail while evaluating this invalid predicate.
     */
    @Test
    public void interpretSimpleExpressionPathWithInvalidFunctionInPredicateThrowsException() {
        // ARRANGE: Set up a predicate with a call to the 'substring' function
        // using an incorrect number of arguments.

        // A simple root node pointer is sufficient for this test's purpose.
        NodePointer rootPointer = NodePointer.newNodePointer(new QName("root"), new Object(), Locale.ENGLISH);

        // Create arguments for the 'substring' function. Providing only one is invalid.
        Expression[] substringArgs = {new Constant("arg1")};

        // The internal JXPath code for the 'substring' function is 14.
        final int SUBSTRING_FUNCTION_CODE = 14;
        CoreFunction invalidSubstringCall = new CoreFunction(SUBSTRING_FUNCTION_CODE, substringArgs);

        // Create a NameAttributeTest predicate, e.g., [@name=substring(...)],
        // using the invalid function call as the value to match against.
        NameAttributeTest invalidNamePredicate = new NameAttributeTest(new Constant("anyName"), invalidSubstringCall);

        Expression[] predicates = {invalidNamePredicate};

        // ACT & ASSERT: Execute the method and verify the expected exception.
        try {
            // The EvalContext and steps array can be null as they are not needed to trigger this specific exception.
            SimplePathInterpreter.interpretSimpleExpressionPath(null, rootPointer, predicates, null);
            fail("Expected a RuntimeException to be thrown due to incorrect function arguments.");
        } catch (RuntimeException e) {
            // Verify that the exception message clearly indicates the problem.
            String expectedMessagePart = "Incorrect number of arguments: substring";
            assertTrue(
                    "Exception message should indicate incorrect argument count for 'substring'. Actual: " + e.getMessage(),
                    e.getMessage().startsWith(expectedMessagePart)
            );
        }
    }
}