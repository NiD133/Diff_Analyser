package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite focuses on verifying the behavior of {@link CoreOperation} subclasses.
 */
public class CoreOperation_ESTestTest2 extends CoreOperation_ESTest_scaffolding {

    /**
     * Verifies that the getSymbol() method of CoreOperationLessThanOrEqual
     * returns the correct XPath symbol "<=".
     */
    @Test
    public void lessThanOrEqualOperationReturnsCorrectSymbol() {
        // Arrange
        // The constructor arguments are not relevant for testing getSymbol(),
        // so we can pass null.
        CoreOperationLessThanOrEqual lessThanOrEqualOperation = new CoreOperationLessThanOrEqual(null, null);
        String expectedSymbol = "<=";

        // Act
        String actualSymbol = lessThanOrEqualOperation.getSymbol();

        // Assert
        assertEquals("The symbol for less than or equal should be '<='", expectedSymbol, actualSymbol);
    }
}