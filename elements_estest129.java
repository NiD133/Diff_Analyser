package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    public void eachTextReturnsTextOfElementsThatHaveTextContent() {
        // Arrange
        // Jsoup.parseBodyFragment creates a full HTML document for the given fragment.
        // The resulting structure is: <html><head></head><body>Bb,Y6</body></html>
        String htmlFragment = "Bb,Y6";
        Document doc = Jsoup.parseBodyFragment(htmlFragment);

        // doc.getAllElements() returns a list of all elements: [<html>, <head>, <body>].
        Elements elements = doc.getAllElements();

        // Act
        // The eachText() method extracts the text content from each element in the list.
        List<String> texts = elements.eachText();

        // Assert
        // The method should only include text from elements that actually have text.
        // - <html>'s text content is "Bb,Y6".
        // - <head>'s text content is empty and is therefore excluded from the result.
        // - <body>'s text content is "Bb,Y6".
        List<String> expectedTexts = List.of("Bb,Y6", "Bb,Y6");
        assertEquals(expectedTexts, texts);
    }
}