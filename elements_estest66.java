package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import java.util.Collection;

public class Elements_ESTestTest66 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling {@link Elements#retainAll(Collection)} with a null argument
     * throws a {@link NullPointerException}. This is the expected behavior as specified
     * by the {@link java.util.Collection#retainAll(Collection)} interface, which
     * the Elements class implements.
     */
    @Test(expected = NullPointerException.class)
    public void retainAllWithNullCollectionShouldThrowNullPointerException() {
        // Arrange: Create a sample Elements collection. The specific content is not important.
        Document document = Document.createShell("");
        Elements elements = document.getAllElements();

        // Act: Call retainAll with a null collection.
        // Assert: The test expects a NullPointerException, which is handled by the
        // @Test(expected=...) annotation, causing the test to pass.
        elements.retainAll(null);
    }
}