package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElementsTestTest48 {

    @Test
    public void removeIfWithPredicateReadingFromSameList() {
        // This test verifies that `Elements.removeIf` does not throw a ConcurrentModificationException
        // when the predicate reads from the same list that is being modified.
        // A naive implementation using a standard iterator would fail.

        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p><p>Four</p>");
        Elements elements = doc.select("p");
        assertEquals(4, elements.size(), "Initial count of p elements should be 4.");

        // This predicate is designed to be self-referential. It reads from the `elements` list
        // while that same list is being iterated over and modified by `removeIf`.
        // It will always evaluate to true for any element in the list.
        Predicate<Element> predicate = el -> elements.contains(el);

        // Act
        boolean wasModified = elements.removeIf(predicate);

        // Assert
        assertTrue(wasModified, "removeIf should return true as elements were removed.");
        assertEquals(0, elements.size(), "All elements should have been removed from the list.");
        assertEquals("", doc.body().html(), "The elements should also be removed from the DOM.");
    }
}