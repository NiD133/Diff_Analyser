package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeVisitor;

/**
 * Test suite for the Elements class, which represents a list of HTML elements
 * and provides methods to manipulate them collectively.
 */
public class ElementsTest {

    // Test data constants
    private static final String SIMPLE_HTML = "<html><head></head><body></body></html>";
    private static final String HTML_WITH_TEXT = "Hello World";
    private static final String INVALID_CSS_SELECTOR = ",P<";
    private static final String EMPTY_STRING = "";
    private static final String TEST_CLASS_NAME = "test-class";
    private static final String TEST_ATTRIBUTE = "test-attr";
    private static final String TEST_VALUE = "test-value";

    // Helper method to create a simple document
    private Document createSimpleDocument() {
        return Document.createShell(SIMPLE_HTML);
    }

    // Helper method to create document with text content
    private Document createDocumentWithText(String text) {
        return Parser.parseBodyFragment(text, "");
    }

    // ========== Constructor Tests ==========

    @Test
    public void constructor_withNullElementArray_shouldThrowNullPointerException() {
        try {
            new Elements((Element[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void constructor_withNullCollection_shouldThrowNullPointerException() {
        try {
            new Elements((Collection<Element>) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void constructor_withNegativeCapacity_shouldThrowIllegalArgumentException() {
        try {
            new Elements(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal Capacity: -1", e.getMessage());
        }
    }

    @Test
    public void constructor_withValidCapacity_shouldCreateEmptyElements() {
        Elements elements = new Elements(10);
        assertTrue("Elements should be empty", elements.isEmpty());
    }

    @Test
    public void constructor_withExistingCollection_shouldCopyElements() {
        Document doc = createSimpleDocument();
        Elements original = doc.getAllElements();
        Elements copy = new Elements(original);
        
        assertEquals("Size should match", original.size(), copy.size());
        assertNotSame("Should be different instances", original, copy);
    }

    // ========== Basic List Operations ==========

    @Test
    public void eq_withValidIndex_shouldReturnSingleElementList() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Elements result = elements.eq(0);
        
        assertEquals("Should contain exactly one element", 1, result.size());
        assertFalse("Should not equal original", result.equals(elements));
    }

    @Test
    public void eq_withIndexOutOfBounds_shouldReturnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.eq(100);
        
        assertTrue("Should be empty", result.isEmpty());
    }

    @Test
    public void first_withEmptyElements_shouldReturnNull() {
        Elements elements = new Elements();
        Element first = elements.first();
        
        assertNull("Should return null for empty elements", first);
    }

    @Test
    public void first_withElements_shouldReturnFirstElement() {
        Document doc = createDocumentWithText(HTML_WITH_TEXT);
        Elements elements = doc.getAllElements();
        Element first = elements.first();
        
        assertNotNull("Should return first element", first);
        assertSame("Should be the document itself", doc, first);
    }

    @Test
    public void last_withEmptyElements_shouldReturnNull() {
        Elements elements = new Elements();
        Element last = elements.last();
        
        assertNull("Should return null for empty elements", last);
    }

    @Test
    public void last_withElements_shouldReturnLastElement() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Element last = elements.last();
        
        assertNotNull("Should return last element", last);
        assertTrue("Last element should be body", last.tagName().equals("body"));
    }

    // ========== Text and Content Operations ==========

    @Test
    public void text_withEmptyElements_shouldReturnEmptyString() {
        Elements elements = new Elements();
        String text = elements.text();
        
        assertEquals("Should return empty string", EMPTY_STRING, text);
    }

    @Test
    public void text_withElementsContainingText_shouldReturnCombinedText() {
        Document doc = createDocumentWithText(HTML_WITH_TEXT);
        Elements elements = doc.getAllElements();
        String text = elements.text();
        
        assertTrue("Should contain the text content", text.contains(HTML_WITH_TEXT));
    }

    @Test
    public void hasText_withEmptyElements_shouldReturnFalse() {
        Elements elements = new Elements();
        boolean hasText = elements.hasText();
        
        assertFalse("Empty elements should not have text", hasText);
    }

    @Test
    public void hasText_withTextContent_shouldReturnTrue() {
        Document doc = createDocumentWithText(HTML_WITH_TEXT);
        Elements elements = doc.getAllElements();
        boolean hasText = elements.hasText();
        
        assertTrue("Elements with text should return true", hasText);
    }

    @Test
    public void eachText_withEmptyElements_shouldReturnEmptyList() {
        Elements elements = new Elements();
        List<String> textList = elements.eachText();
        
        assertTrue("Should return empty list", textList.isEmpty());
    }

    @Test
    public void eachText_withTextContent_shouldReturnTextList() {
        Document doc = createDocumentWithText(HTML_WITH_TEXT);
        Elements elements = doc.getAllElements();
        List<String> textList = elements.eachText();
        
        assertFalse("Should not be empty", textList.isEmpty());
        assertTrue("Should contain expected text", textList.contains(HTML_WITH_TEXT));
    }

    // ========== HTML Operations ==========

    @Test
    public void html_withEmptyElements_shouldReturnEmptyString() {
        Elements elements = new Elements();
        String html = elements.html();
        
        assertEquals("Should return empty string", EMPTY_STRING, html);
    }

    @Test
    public void html_withElements_shouldReturnCombinedHtml() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        String html = elements.html();
        
        assertFalse("Should not be empty", html.isEmpty());
        assertTrue("Should contain HTML content", html.contains("head") || html.contains("body"));
    }

    @Test
    public void html_withNullValue_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.html(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void html_withValidValue_shouldSetHtmlContent() {
        Document doc = createDocumentWithText(EMPTY_STRING);
        Elements elements = doc.getAllElements();
        Elements result = elements.html(HTML_WITH_TEXT);
        
        assertSame("Should return same instance", elements, result);
    }

    // ========== Attribute Operations ==========

    @Test
    public void attr_withNullKey_shouldThrowIllegalArgumentException() {
        Elements elements = new Elements();
        
        try {
            elements.attr(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void attr_withEmptyElements_shouldReturnEmptyString() {
        Elements elements = new Elements();
        String value = elements.attr(TEST_ATTRIBUTE);
        
        assertEquals("Should return empty string", EMPTY_STRING, value);
    }

    @Test
    public void attr_withValidKeyAndValue_shouldSetAttribute() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Elements result = elements.attr(TEST_ATTRIBUTE, TEST_VALUE);
        
        assertSame("Should return same instance", elements, result);
        assertEquals("Should have set the attribute", TEST_VALUE, elements.attr(TEST_ATTRIBUTE));
    }

    @Test
    public void hasAttr_withNullKey_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.hasAttr(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void hasAttr_withNonExistentAttribute_shouldReturnFalse() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        boolean hasAttr = elements.hasAttr("non-existent");
        
        assertFalse("Should return false for non-existent attribute", hasAttr);
    }

    @Test
    public void hasAttr_withExistingAttribute_shouldReturnTrue() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        elements.attr(TEST_ATTRIBUTE, TEST_VALUE);
        boolean hasAttr = elements.hasAttr(TEST_ATTRIBUTE);
        
        assertTrue("Should return true for existing attribute", hasAttr);
    }

    @Test
    public void removeAttr_withNullKey_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.removeAttr(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void removeAttr_withValidKey_shouldRemoveAttribute() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        elements.attr(TEST_ATTRIBUTE, TEST_VALUE);
        Elements result = elements.removeAttr(TEST_ATTRIBUTE);
        
        assertSame("Should return same instance", elements, result);
        assertFalse("Attribute should be removed", elements.hasAttr(TEST_ATTRIBUTE));
    }

    @Test
    public void eachAttr_withNullKey_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.children();
        
        try {
            elements.eachAttr(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void eachAttr_withValidKey_shouldReturnAttributeValues() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        elements.attr(TEST_ATTRIBUTE, TEST_VALUE);
        List<String> values = elements.eachAttr(TEST_ATTRIBUTE);
        
        assertFalse("Should not be empty", values.isEmpty());
        assertTrue("Should contain the test value", values.contains(TEST_VALUE));
    }

    // ========== CSS Class Operations ==========

    @Test
    public void addClass_withNullClassName_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.addClass(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void addClass_withValidClassName_shouldAddClass() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Elements result = elements.addClass(TEST_CLASS_NAME);
        
        assertSame("Should return same instance", elements, result);
    }

    @Test
    public void addClass_withEmptyElements_shouldReturnSameInstance() {
        Elements elements = new Elements();
        Elements result = elements.addClass(TEST_CLASS_NAME);
        
        assertSame("Should return same instance", elements, result);
    }

    @Test
    public void removeClass_withNullClassName_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.removeClass(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void removeClass_withValidClassName_shouldRemoveClass() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Elements result = elements.removeClass(TEST_CLASS_NAME);
        
        assertSame("Should return same instance", elements, result);
    }

    @Test
    public void toggleClass_withNullClassName_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.toggleClass(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void toggleClass_withValidClassName_shouldToggleClass() {
        Document doc = createDocumentWithText(EMPTY_STRING);
        Elements elements = doc.getAllElements();
        Elements result = elements.toggleClass(TEST_CLASS_NAME);
        
        assertFalse("Should not be empty", result.isEmpty());
    }

    @Test
    public void hasClass_withNullClassName_shouldThrowNullPointerException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.hasClass(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void hasClass_withNonExistentClass_shouldReturnFalse() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        boolean hasClass = elements.hasClass("non-existent-class");
        
        assertFalse("Should return false for non-existent class", hasClass);
    }

    // ========== Form Value Operations ==========

    @Test
    public void val_withEmptyElements_shouldReturnEmptyString() {
        Elements elements = new Elements();
        String value = elements.val();
        
        assertEquals("Should return empty string", EMPTY_STRING, value);
    }

    @Test
    public void val_withElementsContainingNullValues_shouldThrowNullPointerException() {
        Elements elements = new Elements();
        elements.add(null);
        
        try {
            elements.val();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void val_withValidValue_shouldSetValue() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        String testValue = TEST_VALUE;
        elements.val(testValue);
        String retrievedValue = elements.val();
        
        assertEquals("Should return the set value", testValue, retrievedValue);
    }

    @Test
    public void val_withNullElementsInList_shouldThrowNullPointerException() {
        Elements elements = new Elements();
        elements.add(null);
        
        try {
            elements.val(TEST_VALUE);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== CSS Selector Operations ==========

    @Test
    public void select_withEmptyQuery_shouldThrowIllegalArgumentException() {
        Elements elements = new Elements();
        
        try {
            elements.select(EMPTY_STRING);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String must not be empty", e.getMessage());
        }
    }

    @Test
    public void select_withInvalidQuery_shouldThrowIllegalStateException() {
        Elements elements = new Elements();
        
        try {
            elements.select(INVALID_CSS_SELECTOR);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue("Should mention parse error", e.getMessage().contains("Could not parse query"));
        }
    }

    @Test
    public void select_withValidQuery_shouldReturnMatchingElements() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Elements result = elements.select("body");
        
        assertNotNull("Should return non-null result", result);
    }

    @Test
    public void selectFirst_withEmptyQuery_shouldThrowIllegalArgumentException() {
        Elements elements = new Elements();
        
        try {
            elements.selectFirst(EMPTY_STRING);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String must not be empty", e.getMessage());
        }
    }

    @Test
    public void selectFirst_withInvalidQuery_shouldThrowIllegalStateException() {
        Elements elements = new Elements();
        
        try {
            elements.selectFirst(INVALID_CSS_SELECTOR);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue("Should mention parse error", e.getMessage().contains("Could not parse query"));
        }
    }

    @Test
    public void selectFirst_withNoMatches_shouldReturnNull() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Element result = elements.selectFirst("nonexistent");
        
        assertNull("Should return null when no matches", result);
    }

    @Test
    public void expectFirst_withNoMatches_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.expectFirst("nonexistent");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue("Should mention no elements matched", 
                      e.getMessage().contains("No elements matched the query"));
        }
    }

    // ========== Element Manipulation Operations ==========

    @Test
    public void append_withNullHtml_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.append(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void append_withValidHtml_shouldAppendContent() {
        Document doc = createDocumentWithText(EMPTY_STRING);
        Elements elements = doc.getAllElements();
        Elements result = elements.append(HTML_WITH_TEXT);
        
        assertFalse("Should not be empty", result.isEmpty());
    }

    @Test
    public void append_withNullElementsInList_shouldThrowNullPointerException() {
        Elements elements = new Elements(new Element[5]);
        
        try {
            elements.append("test");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void prepend_withNullHtml_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.prepend(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void prepend_withValidHtml_shouldPrependContent() {
        Elements elements = new Elements();
        Elements result = elements.prepend(HTML_WITH_TEXT);
        
        assertSame("Should return same instance", elements, result);
    }

    @Test
    public void before_withValidHtml_shouldInsertBefore() {
        Elements elements = new Elements();
        Elements result = elements.before("test");
        
        assertEquals("Should return empty elements", 0, result.size());
    }

    @Test
    public void after_withValidHtml_shouldInsertAfter() {
        Elements elements = new Elements();
        Elements result = elements.after(EMPTY_STRING);
        
        assertSame("Should return same instance", elements, result);
    }

    @Test
    public void wrap_withNullHtml_shouldThrowIllegalArgumentException() {
        Elements elements = new Elements();
        
        try {
            elements.wrap(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String must not be empty", e.getMessage());
        }
    }

    @Test
    public void wrap_withValidHtml_shouldWrapElements() {
        Elements elements = new Elements();
        Elements result = elements.wrap("test");
        
        assertEquals("Should return empty elements", 0, result.size());
    }

    @Test
    public void wrap_withNullElementsInList_shouldThrowNullPointerException() {
        Elements elements = new Elements(new Element[3]);
        
        try {
            elements.wrap("test");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void unwrap_withEmptyElements_shouldReturnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.unwrap();
        
        assertEquals("Should return empty elements", 0, result.size());
    }

    @Test
    public void empty_withEmptyElements_shouldReturnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.empty();
        
        assertTrue("Should be empty", result.isEmpty());
    }

    @Test
    public void remove_withEmptyElements_shouldReturnSameInstance() {
        Elements elements = new Elements();
        Elements result = elements.remove();
        
        assertSame("Should return same instance", elements, result);
    }

    // ========== Tag Name Operations ==========

    @Test
    public void tagName_withEmptyTagName_shouldThrowIllegalArgumentException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.children();
        
        try {
            elements.tagName(EMPTY_STRING);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The 'tagName' parameter must not be empty.", e.getMessage());
        }
    }

    @Test
    public void tagName_withValidTagName_shouldChangeTagName() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Elements result = elements.tagName("div");
        
        assertSame("Should return same instance", elements, result);
    }

    @Test
    public void tagName_withNullElementsInList_shouldThrowNullPointerException() {
        Elements elements = new Elements(new Element[10]);
        
        try {
            elements.tagName("div");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Collection Operations ==========

    @Test
    public void removeAll_withNullCollection_shouldThrowNullPointerException() {
        Elements elements = new Elements();
        
        try {
            elements.removeAll(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void removeAll_withValidCollection_shouldRemoveElements() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Elements toRemove = new Elements(elements);
        boolean result = elements.removeAll(toRemove);
        
        assertTrue("Should return true when elements removed", result);
        assertEquals("Should be empty after removal", 0, elements.size());
    }

    @Test
    public void retainAll_withNullCollection_shouldThrowNullPointerException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.retainAll(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void retainAll_withEmptyCollection_shouldClearElements() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        LinkedList<Object> emptyList = new LinkedList<>();
        boolean result = elements.retainAll(emptyList);
        
        assertTrue("Should return true when elements removed", result);
        assertEquals("Should be empty after retain", 0, elements.size());
    }

    @Test
    public void retainAll_withSameCollection_shouldNotChange() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        boolean result = elements.retainAll(elements);
        
        assertFalse("Should return false when no change", result);
    }

    // ========== Utility Methods ==========

    @Test
    public void toString_withEmptyElements_shouldReturnEmptyString() {
        Elements elements = new Elements();
        String result = elements.toString();
        
        assertEquals("Should return empty string", EMPTY_STRING, result);
    }

    @Test
    public void toString_withElements_shouldReturnHtmlString() {
        Document doc = createSimpleDocument();
        Elements elements = doc.children();
        String result = elements.toString();
        
        assertTrue("Should contain HTML", result.contains("html"));
    }

    @Test
    public void clone_shouldCreateDeepCopy() {
        Document doc = createSimpleDocument();
        Elements original = doc.getAllElements();
        Elements cloned = original.clone();
        
        assertNotSame("Should be different instances", original, cloned);
        assertEquals("Should have same size", original.size(), cloned.size());
    }

    @Test
    public void asList_shouldReturnArrayList() {
        Elements elements = new Elements();
        ArrayList<Element> list = elements.asList();
        
        assertTrue("Should return empty list", list.isEmpty());
        assertNotSame("Should be different instances", elements, list);
    }

    @Test
    public void asList_withElements_shouldReturnPopulatedList() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        ArrayList<Element> list = elements.asList();
        
        assertEquals("Should have same size", elements.size(), list.size());
    }

    // ========== Child Node Operations ==========

    @Test
    public void textNodes_withEmptyElements_shouldReturnEmptyList() {
        Elements elements = new Elements();
        List<TextNode> textNodes = elements.textNodes();
        
        assertTrue("Should return empty list", textNodes.isEmpty());
    }

    @Test
    public void textNodes_withElements_shouldReturnTextNodes() {
        Document doc = createDocumentWithText("Test");
        Elements elements = doc.getAllElements();
        List<TextNode> textNodes = elements.textNodes();
        
        assertFalse("Should not be empty", textNodes.isEmpty());
    }

    @Test
    public void comments_withElements_shouldReturnCommentNodes() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        List<Comment> comments = elements.comments();
        
        assertEquals("Should return empty list", 0, comments.size());
    }

    @Test
    public void dataNodes_withElements_shouldReturnDataNodes() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        List<DataNode> dataNodes = elements.dataNodes();
        
        assertTrue("Should return empty list", dataNodes.isEmpty());
    }

    @Test
    public void forms_withElements_shouldReturnFormElements() {
        Document doc = createDocumentWithText(EMPTY_STRING);
        Elements elements = doc.getAllElements();
        List<FormElement> forms = elements.forms();
        
        assertTrue("Should return empty list", forms.isEmpty());
    }

    // ========== Traversal Operations ==========

    @Test
    public void traverse_withNullVisitor_shouldThrowIllegalArgumentException() {
        Elements elements = new Elements();
        
        try {
            elements.traverse(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void traverse_withValidVisitor_shouldTraverseElements() {
        Elements elements = new Elements();
        NodeVisitor visitor = mock(NodeVisitor.class);
        Elements result = elements.traverse(visitor);
        
        assertTrue("Should return empty elements", result.isEmpty());
    }

    @Test
    public void filter_withNullFilter_shouldThrowIllegalArgumentException() {
        Elements elements = new Elements();
        
        try {
            elements.filter(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void filter_withValidFilter_shouldFilterElements() {
        Elements elements = new Elements();
        NodeFilter filter = mock(NodeFilter.class);
        Elements result = elements.filter(filter);
        
        assertTrue("Should return empty elements", result.isEmpty());
    }

    // ========== Predicate Operations ==========

    @Test
    public void removeIf_withNullPredicate_shouldThrowNullPointerException() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        
        try {
            elements.removeIf(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void removeIf_withValidPredicate_shouldRemoveMatchingElements() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Document document = doc;
        Predicate<Object> predicate = Predicate.isEqual(document);
        boolean result = elements.removeIf(predicate);
        
        assertTrue("Should remove matching elements", result);
        assertTrue("Should be empty after removal", elements.isEmpty());
    }

    @Test
    public void removeIf_withNonMatchingPredicate_shouldNotRemoveElements() {
        Document doc = createSimpleDocument();
        Elements elements = doc.getAllElements();
        Predicate<Object> predicate = Predicate.isEqual("non-matching");
        boolean result = elements.removeIf(predicate);
        
        assertFalse("Should not remove any elements", result);
    }

    // ========== Sibling Navigation ==========

    @Test
    public void next_withEmptyElements_shouldReturnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.next();
        
        assertNotSame("Should be different instance", elements, result);
        assertTrue("Should be empty", result.isEmpty());
    }

    @Test
    public void prev_withEmptyElements_shouldReturnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.prev();
        
        assertNotSame("Should be different instance", elements, result);
        assertTrue("Should be empty", result.isEmpty());
    }

    @Test
    public void nextAll_withEmptyElements_shouldReturnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.nextAll();
        
        assertNotSame("Should be different instance", elements, result);
        assertTrue("Should be empty", result.isEmpty());
    }

    @Test
    public void prevAll_withEmptyElements_shouldReturnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.prevAll();
        
        assertNotSame("Should be different instance", elements, result);
        assertTrue("Should be empty", result.isEmpty());
    }

    @Test
    public void parents_withEmptyElements_shouldReturnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.parents();
        
        assertTrue("Should be empty", result.isEmpty());
    }
}