package org.jsoup.parser;

import org.junit.Test;

// Note: The original test class name "Tokeniser_ESTestTest18" and its scaffolding
// are preserved as per the prompt's context. In a real-world scenario,
// this test would be part of a comprehensive "TokeniserTest" class.
public class Tokeniser_ESTestTest18 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that the Tokeniser constructor throws a NullPointerException when passed an
     * uninitialized TreeBuilder. This occurs because the constructor attempts to access
     * the builder's 'parser' field, which is null in a newly created TreeBuilder instance.
     */
    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerExceptionForUninitializedTreeBuilder() {
        // An HtmlTreeBuilder created with its default constructor is not fully initialized;
        // it lacks a reader and a parser.
        HtmlTreeBuilder uninitializedTreeBuilder = new HtmlTreeBuilder();

        // Attempting to create a Tokeniser with this uninitialized builder should fail
        // with an NPE, as it tries to access `builder.parser.getErrors()`.
        new Tokeniser(uninitializedTreeBuilder);
    }
}