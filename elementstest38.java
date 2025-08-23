package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class Elements_ESTestTest38 extends Elements_ESTest_scaffolding {

    /**
     * Tests that modifications to the DOM through one Elements object are reflected
     * in another Elements object that points to the same underlying nodes.
     *
     * This test verifies that after prepending a new element to the parent of the
     * <head> tag, its sibling index is correctly updated in a separate selection.
     */
    @Test(timeout = 4000)
    public void testSiblingIndexIsUpdatedAfterDomModification() throws Throwable {
        // Arrange: Create a document and select a specific element.
        Document doc = Document.createShell("<m-2,eXTA:N5y7");
        Elements allElements = doc.getAllElements(); // Contains [<html>, <head>, <body>]

        // The prev() method finds the immediate previous sibling for each element.
        // For the list [<html>, <head>, <body>], only <body> has a previous
        // element sibling, which is <head>.
        Elements prevElements = allElements.prev(); // Contains just [<head>]

        // Act: Modify the DOM by prepending a new element to all elements.
        // The key modification happens when a new node is prepended to <html>,
        // which is the parent of <head>.
        allElements.prepend("<m-2,eXTA:N5y7");

        // Assert: Verify that the change in the DOM is reflected in the original selection.
        Element headElement = prevElements.first();

        // The selection should still contain the single <head> element.
        assertEquals("Selection should still contain one element", 1, prevElements.size());

        // The sibling index of <head> should be updated because a new element
        // was inserted before it inside <html>. The original test expects an index of 2,
        // which implies the prepended string might be parsed into multiple nodes
        // in the test environment.
        assertEquals("Sibling index of <head> should be updated after prepend", 2, headElement.siblingIndex());
    }
}