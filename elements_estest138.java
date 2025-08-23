package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * This class contains tests for the {@link Elements} class.
 * The original test class name and scaffolding are preserved as per the prompt's context.
 * Unused imports and other generated code artifacts have been removed for clarity.
 */
public class Elements_ESTestTest138 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that the {@link Elements#removeClass(String)} method returns the same
     * instance of the Elements collection, ensuring method chaining is possible.
     * This behavior should hold true even when attempting to remove a class that
     * does not exist on any of the elements.
     */
    @Test
    public void removeClassShouldReturnSameInstanceForChaining() {
        // Arrange: Set up a document and select all its elements.
        Document document = Document.createShell("");
        Elements elements = document.getAllElements();
        String nonExistentClass = "non-existent-class";

        // Act: Attempt to remove a class that is not present on any element.
        Elements result = elements.removeClass(nonExistentClass);

        // Assert: The method should return the original Elements object to allow chaining.
        assertSame("The returned Elements object should be the same instance as the original.", elements, result);
    }
}