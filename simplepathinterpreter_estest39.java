package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimplePathInterpreter_ESTestTest39 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Tests that when an arithmetic XPath expression operates on paths that do not
     * exist, JXPath treats their values as zero.
     */
    @Test
    public void getValueWithAdditionOfNonExistentPathsShouldReturnZero() {
        // Arrange
        // An XPath expression that adds two properties that do not exist on the root bean.
        final String xpathWithNonExistentPaths = "nonExistentProperty1 + nonExistentProperty2";

        // A simple object that serves as the context root but lacks the properties in the XPath.
        final Object rootBean = new Object();
        final JXPathContext jxpathContext = JXPathContext.newContext(rootBean);

        // JXPath is expected to convert non-existent paths (which evaluate to null)
        // to the number 0 in a numeric context. Therefore, the expression effectively
        // becomes 0.0 + 0.0.
        final double expectedValue = 0.0;

        // Act
        final Object actualValue = jxpathContext.getValue(xpathWithNonExistentPaths);

        // Assert
        assertEquals("The sum of two non-existent paths should be treated as 0.0",
                expectedValue, actualValue);
    }
}