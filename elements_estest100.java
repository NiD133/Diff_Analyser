package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import java.util.function.Predicate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Elements_ESTestTest100 extends Elements_ESTest_scaffolding {

    @Test
    public void removeIfShouldReturnFalseWhenPredicateMatchesNoElements() {
        // Arrange
        Document doc = Document.createShell("");
        Elements elements = doc.getAllElements(); // Contains <html>, <head>, <body>
        int initialSize = elements.size();

        // Create a predicate that will never match an Element object.
        Predicate<Object> nonMatchingPredicate = Predicate.isEqual("a string that is not an element");

        // Act
        boolean wasModified = elements.removeIf(nonMatchingPredicate);

        // Assert
        assertFalse("The collection should not be reported as modified.", wasModified);
        assertEquals("The collection size should remain unchanged.", initialSize, elements.size());
    }
}