package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ElementsTest {

    @Test
    public void testSelectElementsThatContainSpecificText() {
        // Given
        String html = "<p>This is some text</p><p>This is different text</p>";
        Document doc = Parser.parseBodyFragment(html, "");
        Evaluator.ContainsWholeText evaluator = new Evaluator.ContainsWholeText("some text");

        // When
        Elements selectedElements = doc.select(evaluator);

        // Then
        assertEquals(1, selectedElements.size());
        assertEquals("This is some text", selectedElements.first().text());
    }

    @Test
    public void testElementsCanBeInitializedWithInitialCapacity() {
        // When
        Elements elements = new Elements(0);

        // Then
        assertEquals(0, elements.size());
    }

    @Test
    public void testWrapElementsWithHtml() {
        // Given
        Elements elements = new Elements();

        // When
        Elements wrappedElements = elements.wrap("<div></div>");

        // Then
        assertSame(elements, wrappedElements); // Should modify the original list
    }

    @Test
    public void testSetElementValue() {
        // Given
        Elements elements = new Elements();

        // When
        Elements valueElements = elements.val("map");

        // Then
        assertSame(elements, valueElements);
    }

    @Test
    public void testGetElementValue() {
        // Given
        String html = "<input value='initial value'>";
        Document doc = Parser.parseBodyFragment(html, "");
        Elements input = doc.select("input");

        // When
        String value = input.val();

        // Then
        assertEquals("initial value", value);
    }

    @Test
    public void testUnwrapElements() {
        // Given
        String html = "<div><p>Some text</p></div>";
        Document doc = Parser.parseBodyFragment(html, "");
        Elements paragraphs = doc.select("p");

        // When
        Elements unwrappedElements = paragraphs.unwrap();

        // Then
        assertSame(paragraphs, unwrappedElements); // Should modify in place
    }

    @Test
    public void testTraverseEmptyElementsWithNodeVisitorDoesNothing() {
        // Given
        Elements elements = new Elements();
        NodeVisitor visitor = mock(NodeVisitor.class);

        // When
        Elements traversedElements = elements.traverse(visitor);

        // Then
        assertTrue(traversedElements.isEmpty());
    }

    @Test
    public void testTraverseElementsWithNodeVisitor() {
        // Given
        String html = "<p>Some text</p>";
        Document doc = Parser.parseBodyFragment(html, "");
        Elements elements = doc.select("p");
        NodeVisitor visitor = mock(NodeVisitor.class);

        // When
        Elements traversedElements = elements.traverse(visitor);

        // Then
        assertSame(elements, traversedElements); // Should modify in place
    }

    @Test
    public void testToggleClassOnEmptyElementsDoesNothing() {
        // Given
        Elements elements = new Elements();

        // When
        Elements toggledElements = elements.toggleClass("someClass");

        // Then
        assertTrue(toggledElements.isEmpty());
    }

    @Test
    public void testToStringOnEmptyElementsReturnsEmptyString() {
        // Given
        Elements elements = new Elements();

        // When
        String stringRepresentation = elements.toString();

        // Then
        assertEquals("", stringRepresentation);
    }

    @Test
    public void testGetTextNodesFromEmptyElementsReturnsEmptyList() {
        // Given
        Elements elements = new Elements();

        // When
        List<TextNode> textNodes = elements.textNodes();

        // Then
        assertTrue(textNodes.isEmpty());
    }

    @Test
    public void testGetTextFromElements() {
        // Given
        String html = "<p>Some text</p>";
        Document doc = Parser.parseBodyFragment(html, "");
        Elements paragraphs = doc.select("p");

        // When
        String text = paragraphs.text();

        // Then
        assertEquals("Some text", text);
    }

    @Test
    public void testSetElementAtIndex() {
        // Given
        Document doc = Parser.parseBodyFragment("<p>First</p><p>Second</p>", "");
        Elements paragraphs = doc.select("p");

        // When
        paragraphs.set(0, doc);

        // Then
        assertTrue(doc.hasParent());
    }

    @Test
    public void testSelectFirstElementWithNoMatchesReturnsNull() {
        // Given
        Document doc = Parser.parseBodyFragment("<p>First</p><p>Second</p>", "");
        Elements paragraphs = doc.select("p");

        // When
        Element selectedElement = paragraphs.selectFirst("div");

        // Then
        assertNull(selectedElement);
    }

    @Test
    public void testSelectElementsWithSelector() {
        // Given
        Elements elements = new Elements();

        // When
        Elements selectedElements = elements.select("p");

        // Then
        assertTrue(selectedElements.equals(elements));
    }

    @Test
    public void testRemoveNullClassDoesNothing() {
        // Given
        Elements elements = new Elements();

        // When
        Elements removedClassElements = elements.removeClass(null);

        // Then
        assertSame(elements, removedClassElements);
    }

    @Test
    public void testRemoveEmptyAttributeDoesNothing() {
        // Given
        Elements elements = new Elements();

        // When
        Elements removedAttributeElements = elements.removeAttr("");

        // Then
        assertSame(elements, removedAttributeElements);
    }

    @Test
    public void testRemoveAllElements() {
        // Given
        Document doc = Parser.parseBodyFragment("<p>First</p><p>Second</p><div></div>", "");
        Elements allElements = doc.getAllElements();
        Elements paragraphs = doc.select("p");

        // When
        boolean removed = allElements.removeAll(paragraphs);

        // Then
        assertTrue(removed);
        assertEquals(2, allElements.size());
    }

    @Test
    public void testRemoveElementAtIndex() {
        // Given
        Document doc = new Document("#declaration");
        Elements allElements = doc.getAllElements();

        // When
        Node removedElement = allElements.remove(0);

        // Then
        assertSame(doc, removedElement);
    }

    @Test
    public void testRemoveAllElementsFromList() {
        // Given
        Elements elements = new Elements();

        // When
        Elements removedElements = elements.remove();

        // Then
        assertEquals(0, removedElements.size());
    }

    @Test
    public void testPrevAllWithNullSelector() {
        // Given
        Document doc = Document.createShell("<html><body><p>First</p><p>Second</p></body></html>");
        Elements paragraphs = doc.select("p");

        // When
        Elements prevAllElements = paragraphs.prevAll((String) null);

        // Then
        assertNotSame(paragraphs, prevAllElements);
    }

    @Test
    public void testPrevAll() {
        // Given
        Document doc = Parser.parseBodyFragment("<p>First</p><p>Second</p>", "");
        Elements paragraphs = doc.getElementsByIndexLessThan(201);

        // When
        Elements prevAllElements = paragraphs.prevAll();

        // Then
        assertNotSame(paragraphs, prevAllElements);
    }

    @Test
    public void testPrevWithNoSiblingsReturnsEmptyList() {
        // Given
        Elements elements = new Elements();

        // When
        Elements prevElements = elements.prev();

        // Then
        assertTrue(prevElements.isEmpty());
    }

    @Test
    public void testPrependToElements() {
        // Given
        Elements elements = new Elements();

        // When
        Elements prependedElements = elements.prepend("some text");

        // Then
        assertEquals(0, prependedElements.size());
    }

    @Test
    public void testParentsOfElements() {
        // Given
        Document doc = Parser.parseBodyFragment("<p><span>Some text</span></p>", "");
        Elements span = doc.select("span");

        // When
        Elements parents = span.parents();

        // Then
        assertEquals(2, parents.size()); // p and body
    }

    @Test
    public void testOuterHtml() {
        // Given
        String html = "<p>Some text</p>";
        Document doc = Parser.parseBodyFragment(html, "");
        Elements paragraphs = doc.select("p");

        // When
        String outerHtml = paragraphs.outerHtml();

        // Then
        assertEquals("<p>Some text</p>", outerHtml);
    }

    @Test
    public void testNotWithMatchingSelector() {
        // Given
        Document doc = Parser.parseBodyFragment("<div><p class='match'>One</p><p>Two</p></div>", "");
        Elements paragraphs = doc.select("p");

        // When
        Elements notMatching = paragraphs.not(".match");

        // Then
        assertEquals(1, notMatching.size());
        assertEquals("Two", notMatching.first().text());
    }

    @Test
    public void testNextAllWithNullSelector() {
        // Given
        Document doc = Parser.parse("|", "");
        Elements allElements = doc.getAllElements();

        // When
        Elements nextAllElements = allElements.nextAll((String) null);

        // Then
        assertNotSame(allElements, nextAllElements);
    }

    @Test
    public void testNextAll() {
        // Given
        Elements elements = new Elements();

        // When
        Elements nextAllElements = elements.nextAll();

        // Then
        assertTrue(nextAllElements.equals(elements));
    }

    @Test
    public void testLastElementInList() {
        // Given
        Document document = new Document(" ", " ");
        Elements elements = document.getAllElements();

        // When
        Element lastElement = elements.last();

        // Then
        assertFalse(lastElement.isBlock());
    }

    @Test
    public void testEmptyElementsReturnsTheSameInstance() {
        // Given
        Document document = Parser.parse("", "");
        Elements elements = document.getAllElements();

        // When
        Elements result = elements.empty();

        // Then
        assertSame(elements, result);
    }

    @Test
    public void testCloneElements() {
        // Given
        Document document = Parser.parse("String must not be empty", "String must not be empty");
        Elements elements = document.getAllElements();

        // When
        Elements clonedElements = elements.clone();

        // Then
        assertEquals(4, clonedElements.size());
        assertNotSame(clonedElements, elements);
    }

    @Test
    public void testAfterWithString() {
        // Given
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements = document.children();

        // When
        Elements result = elements.after("org.jsoup.select.Elements");

        // Then
        assertEquals(1, result.size());
    }

    @Test
    public void testBeforeWithString() {
        // Given
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements = document.children();

        // When
        Elements result = elements.before("+locPfd1R']\"Xmtz8N");

        // Then
        assertSame(elements, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapWithEmptyStringThrowsException() {
        Elements elements0 = new Elements();
        elements0.wrap("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveClassWithNullThrowsException() {
        Document document0 = Parser.parseBodyFragment(".PK`)n7Yo}SK:._|)", ".PK`)n7Yo}SK:._|)");
        Elements elements0 = document0.getAllElements();
        elements0.removeClass(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAttrWithNullThrowsException() {
        Document document0 = Parser.parseBodyFragment("", "");
        Elements elements0 = document0.getAllElements();
        elements0.removeAttr(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrependWithNullThrowsException() {
        Tag tag0 = new Tag("E", "E");
        Element element0 = new Element(tag0, "E");
        Elements elements0 = element0.getAllElements();
        elements0.prepend(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotWithEmptyStringThrowsException() {
        Elements elements0 = new Elements();
        elements0.not("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHtmlWithNullThrowsException() {
        Document document0 = Document.createShell(") w9n8 #)g\"");
        Elements elements0 = document0.getAllElements();
        elements0.html(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasAttrWithNullThrowsException() {
        Document document0 = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements0 = document0.getAllElements();
        elements0.hasAttr(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEachAttrWithNullThrowsException() {
        Document document0 = Parser.parse("S$tsk F", "S$tsk F");
        Elements elements0 = document0.getAllElements();
        elements0.eachAttr(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAttrWithNullValueThrowsException() {
        Document document0 = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements0 = document0.children();
        elements0.attr(null, "4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttrWithNullThrowsException() {
        Document document0 = Parser.parseBodyFragment(".PK`)n7YS(G.__)", ".PK`)n7YS(G.__)");
        Elements elements0 = document0.getAllElements();
        elements0.attr(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendWithNullThrowsException() {
        Document document0 = Document.createShell(".PK`)n7Yo+S(:._|)");
        Elements elements0 = document0.getAllElements();
        elements0.append(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddClassWithNullThrowsException() {
        Document document0 = Parser.parseBodyFragment("JqY[1(<nRcx|RV", "JqY[1(<nRcx|RV");
        Elements elements0 = document0.getAllElements();
        elements0.addClass(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullThrowsException() {
        Elements elements0 = new Elements();
        elements0.set(410, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelectWithEmptyStringThrowsException() {
        Elements elements0 = new Elements();
        elements0.select("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterWithNullThrowsException() {
        Elements elements0 = new Elements();
        elements0.filter(null);
    }

    @Test
    public void testPrevReturnsCorrectElements() {
        Document document0 = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements0 = document0.getAllElements();
        Elements elements1 = elements0.prev();
        assertEquals(1, elements1.size());
    }

    @Test
    public void testIsReturnsFalseForNonMatchingQuery() {
        Document document0 = new Document("o3S|A", "o3S|A");
        Elements elements0 = document0.getAllElements();
        boolean boolean0 = elements0.is("o3S|A");
        assertFalse(boolean0);
    }

    @Test
    public void testNextReturnsCorrectElements() {
        Document document0 = Document.createShell("");
        Elements elements0 = document0.getAllElements();
        Elements elements1 = elements0.next((String) null);
        assertEquals(1, elements1.size());
    }

    @Test
    public void testDeselectObjectNotContainedInElementsReturnsFalse() {
        Elements elements0 = new Elements();
        Object object0 = new Object();
        boolean boolean0 = elements0.deselect(object0);
        assertFalse(boolean0);
    }

    @Test
    public void testNextAllReturnsCorrectElements() {
        Parser parser0 = Parser.htmlParser();
        Document document0 = parser0.parseInput("H", "http://www.w3.org/1998/Math/MathML");
        Elements elements0 = document0.getElementsByAttributeValueNot("http://www.w3.org/XML/1998/namespace", "http://www.w3.org/1998/Math/MathML");
        Elements elements1 = elements0.nextAll("H");
        assertEquals(0, elements1.size());
    }

    @Test
    public void testRetainAllWhereNoIntersectionReturnsEmptyList() {
        Document document0 = Document.createShell(".K`)n7Yo}SK._)");
        Elements elements0 = document0.getAllElements();
        Element[] elementArray0 = new Element[6];
        Elements elements1 = new Elements(elementArray0);
        boolean boolean0 = elements0.retainAll(elements1);
        assertTrue(elements0.isEmpty());
        assertTrue(boolean0);
    }

    @Test
    public void testTraverseWithVisitor() {
        NodeVisitor nodeVisitor0 = mock(NodeVisitor.class);
        Elements elements0 = new Elements();
        try {
            elements0.traverse(nodeVisitor0);
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void testReplaceAllWithUnaryOperator() {
        Document document0 = Document.createShell("String must not be empty");
        Elements elements0 = document0.getAllElements();
        try {
            elements0.replaceAll(null);
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testHasTextReturnsFalseForDocumentWithNoTextContent() {
        Document document0 = new Document("Z\"", "Z\"");
        Elements elements0 = document0.getAllElements();
        boolean hasText = elements0.hasText();
        assertFalse(hasText);
    }

    @Test
    public void testHasTextReturnsTrueForDocumentWithTextContent() {
        Document document0 = Parser.parseBodyFragment("Strng muGt dot be empty", "Strng muGt dot be empty");
        Elements elements0 = document0.getAllElements();
        boolean hasText = elements0.hasText();
        assertTrue(hasText);
    }

    @Test
    public void testClassNamesAreToggledCorrectly() {
        Document document0 = new Document("S$\tsk F", "S$\tsk F");
        Elements elements0 = document0.getAllElements();
        Elements elements1 = elements0.toggleClass("S$\tsk F");
        assertSame(elements1, elements0);
    }

    @Test
    public void testClassesAreRemovedCorrectly() {
        Document document0 = Parser.parseBodyFragment("String must nov be empty", "String must nov be empty");
        Elements elements0 = document0.getAllElements();
        Elements elements1 = elements0.removeClass("String must nov be empty");
        assertSame(elements0, elements1);
    }

    @Test
    public void testClassesAreAddedCorrectly() {
        Document document0 = new Document("/tring must not be mpty", "/tring must not be mpty");
        Elements elements0 = document0.getAllElements();
        Elements elements1 = elements0.addClass("/tring must not be mpty");
        assertEquals(1, elements1.size());
    }

    @Test
    public void testValOnEmptyStringReturnsEmptyString() {
        Elements elements0 = new Elements();
        String val = elements0.val();
        assertEquals("", val);
    }

    @Test
    public void testEachAttrReturnsCorrectList() {
        Document document0 = new Document("String must not be mpty", "String must not be mpty");
        Elements elements0 = document0.getAllElements();
        List<String> list0 = elements0.eachAttr("");
        assertFalse(list0.contains(""));
    }

    @Test
    public void testHasAttrReturnsTrueWhenAttributeExists() {
        Document document0 = Parser.parse("String must not be empty", "String must not be empty");
        Elements elements0 = document0.getAllElements();
        elements0.attr("String must not be empty", "String must not be empty");
        List<String> list0 = elements0.eachAttr("String must not be empty");
        assertTrue(list0.contains("String must not be empty"));
    }

    @Test
    public void testHasAttrReturnsFalseWhenAttributeDoesNotExist() {
        Document document0 = new Document("String muGt dot be empty");
        Elements elements0 = document0.getAllElements();
        boolean boolean0 = elements0.hasAttr("String muGt dot be empty");
        assertFalse(boolean0);
    }

    @Test
    public void testPrevWithSelector() {
        Document document0 = Parser.parseBodyFragment("*", "*");
        Elements elements0 = document0.getAllElements();
        Elements elements1 = elements0.prev("*");
        assertEquals(1, elements1.size());
    }

    @Test
    public void testLast() {
        Document document0 = new Document(" ", " ");
        Elements elements0 = document0.getAllElements();
        Element element0 = elements0.last();
        assertFalse(element0.isBlock());
    }
}