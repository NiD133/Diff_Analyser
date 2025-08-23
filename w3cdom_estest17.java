package org.jsoup.helper;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * This test class contains tests for the W3CDom helper class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class W3CDom_ESTestTest17 extends W3CDom_ESTest_scaffolding {

    /**
     * Verifies that attempting to convert a Jsoup Element containing a circular reference
     * (i.e., an element that is its own child) results in a StackOverflowError.
     * <p>
     * The conversion process in {@link W3CDom} involves a recursive traversal of the DOM tree.
     * A cyclic structure should lead to infinite recursion, which is correctly handled by the JVM
     * throwing a StackOverflowError. This test ensures that such a malformed structure
     * does not cause the application to hang or fail silently.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void fromJsoupWithCircularReferenceThrowsStackOverflowError() {
        // Arrange: Create a W3CDom converter and a Jsoup element.
        W3CDom w3cDom = new W3CDom();
        Element elementWithCircularReference = new Element("div");

        // Create a circular reference by making the element its own child.
        // This creates a malformed DOM structure that will cause infinite recursion.
        elementWithCircularReference.prependChild(elementWithCircularReference);

        // Act: Attempt to convert the element with the circular reference.
        // This action is expected to cause infinite recursion and throw a StackOverflowError.
        w3cDom.fromJsoup(elementWithCircularReference);

        // Assert: The test is successful if a StackOverflowError is thrown, as specified
        // by the 'expected' attribute of the @Test annotation.
    }
}