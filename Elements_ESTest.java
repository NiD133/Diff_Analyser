package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * A refactored, understandable test suite for the org.jsoup.select.Elements class.
 * This suite focuses on clear, behavior-driven tests with descriptive names and a consistent structure.
 */
public class Elements_RefactoredTest {

    private final String html = "<div><p class='foo'>Hello</p><p class='foo bar'>There</p><span>now</span></div>";

    // --- Attribute and Class Methods ---

    @Test
    public void attr_getsAttributeFromFirstElement() {
        // Arrange
        String htmlWithIds = "<div><p id='p1'>One</p><p id='p2'>Two</p></div>";
        Elements paragraphs = Jsoup.parse(htmlWithIds).select("p");

        // Act & Assert
        assertEquals("p1", paragraphs.attr("id"));
    }

    @Test
    public void attr_returnsEmptyStringIfNoElementHasAttribute() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act & Assert
        assertEquals("", paragraphs.attr("id"));
    }

    @Test
    public void attr_setsAttributeOnAllElements() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act
        paragraphs.attr("lang", "en");

        // Assert
        for (Element p : paragraphs) {
            assertEquals("en", p.attr("lang"));
        }
    }

    @Test
    public void hasAttr_returnsTrueIfAnyElementHasAttribute() {
        // Arrange
        String htmlWithIds = "<div><p>One</p><p id='p2'>Two</p></div>";
        Elements paragraphs = Jsoup.parse(htmlWithIds).select("p");

        // Act & Assert
        assertTrue(paragraphs.hasAttr("id"));
    }

    @Test
    public void hasAttr_returnsFalseIfNoElementHasAttribute() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act & Assert
        assertFalse(paragraphs.hasAttr("id"));
    }

    @Test
    public void addClass_addsClassToAllElements() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.addClass("baz");

        // Assert
        assertTrue(doc.select(".foo.bar.baz").hasText());
        assertTrue(doc.select(".foo.baz").first().hasText());
    }

    @Test
    public void removeClass_removesClassFromAllElements() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.removeClass("foo");

        // Assert
        assertFalse(paragraphs.get(0).hasClass("foo"));
        assertTrue(paragraphs.get(1).hasClass("bar")); // Other classes remain
        assertFalse(paragraphs.get(1).hasClass("foo"));
    }

    @Test
    public void toggleClass_addsAndRemovesClassFromElements() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.toggleClass("foo"); // Should remove 'foo' from all
        paragraphs.toggleClass("newClass"); // Should add 'newClass' to all

        // Assert
        for (Element p : paragraphs) {
            assertFalse(p.hasClass("foo"));
            assertTrue(p.hasClass("newClass"));
        }
    }

    @Test
    public void hasClass_returnsTrueIfAnyElementHasClass() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act & Assert
        assertTrue(paragraphs.hasClass("bar"));
    }

    @Test
    public void val_getsValueFromFirstElement() {
        // Arrange
        String formHtml = "<form><input name='name' value='Jsoup'></form>";
        Elements inputs = Jsoup.parse(formHtml).select("input");

        // Act & Assert
        assertEquals("Jsoup", inputs.val());
    }

    @Test
    public void val_setsValueOnAllElements() {
        // Arrange
        String formHtml = "<form><input name='name' value='Jsoup'><input name='email'></form>";
        Elements inputs = Jsoup.parse(formHtml).select("input");

        // Act
        inputs.val("test");

        // Assert
        for (Element input : inputs) {
            assertEquals("test", input.val());
        }
    }

    // --- HTML, Text, and Content Methods ---

    @Test
    public void text_returnsCombinedTextOfAllElements() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act
        String combinedText = paragraphs.text();

        // Assert
        assertEquals("Hello There", combinedText);
    }

    @Test
    public void hasText_returnsTrueIfAnyElementHasText() {
        // Arrange
        String htmlWithEmptyP = "<div><p></p><p>Not empty</p></div>";
        Elements paragraphs = Jsoup.parse(htmlWithEmptyP).select("p");

        // Act & Assert
        assertTrue(paragraphs.hasText());
    }

    @Test
    public void hasText_returnsFalseIfAllElementsAreEmpty() {
        // Arrange
        String htmlWithEmptyP = "<div><p></p><p>  </p></div>"; // Whitespace is not text
        Elements paragraphs = Jsoup.parse(htmlWithEmptyP).select("p");

        // Act & Assert
        assertFalse(paragraphs.hasText());
    }

    @Test
    public void html_returnsCombinedInnerHtmlOfAllElements() {
        // Arrange
        String complexHtml = "<div><p><b>One</b></p><p><i>Two</i></p></div>";
        Elements paragraphs = Jsoup.parse(complexHtml).select("p");

        // Act
        String innerHtml = paragraphs.html();

        // Assert
        assertEquals("<b>One</b><i>Two</i>", innerHtml);
    }

    @Test
    public void outerHtml_returnsConcatenatedOuterHtmlOfAllElements() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act
        String outerHtml = paragraphs.outerHtml();

        // Assert
        assertEquals("<p class=\"foo\">Hello</p><p class=\"foo bar\">There</p>", outerHtml);
    }

    @Test
    public void prepend_addsHtmlToStartOfEachElement() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.prepend("<span>Prefix </span>");

        // Assert
        assertEquals("<span>Prefix </span>Hello", paragraphs.first().html());
        assertEquals("<span>Prefix </span>There", paragraphs.last().html());
    }

    @Test
    public void append_addsHtmlToEndOfEachElement() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.append("<span> Suffix</span>");

        // Assert
        assertEquals("Hello<span> Suffix</span>", paragraphs.first().html());
        assertEquals("There<span> Suffix</span>", paragraphs.last().html());
    }

    @Test
    public void before_insertsHtmlBeforeEachElement() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.before("<hr>");

        // Assert
        assertEquals(2, doc.select("hr").size());
        assertEquals("hr", doc.select("p").first().previousElementSibling().tagName());
    }

    @Test
    public void after_insertsHtmlAfterEachElement() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.after("<br>");

        // Assert
        assertEquals(2, doc.select("br").size());
        assertEquals("br", doc.select("p").first().nextElementSibling().tagName());
    }

    // --- DOM Manipulation ---

    @Test
    public void wrap_wrapsEachElementIndividually() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.wrap("<div class='wrapper'></div>");

        // Assert
        assertEquals(2, doc.select("div.wrapper").size());
        assertEquals("Hello", doc.select("div.wrapper > p").first().text());
    }

    @Test
    public void unwrap_removesWrappingElementAndKeepsChildren() {
        // Arrange
        Document doc = Jsoup.parse("<body><div><p>Hello</p></div><div><p>There</p></div></body>");
        Elements divs = doc.select("div");

        // Act
        divs.unwrap();

        // Assert
        assertEquals(0, doc.select("div").size());
        assertEquals(2, doc.select("p").size());
        assertEquals("<body>\n <p>Hello</p>\n <p>There</p>\n</body>", doc.body().outerHtml());
    }

    @Test
    public void empty_removesAllChildrenFromEachElement() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.empty();

        // Assert
        assertEquals("", paragraphs.first().html());
        assertEquals("", paragraphs.last().html());
    }

    @Test
    public void remove_removesEachElementFromDom() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        Elements removed = paragraphs.remove();

        // Assert
        assertEquals(0, doc.select("p").size());
        assertEquals(2, removed.size()); // Elements are still in the Elements object
    }

    // --- Filtering and Traversing ---

    @Test
    public void select_findsMatchingElementsWithinTheCurrentSet() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act
        Elements barParagraphs = paragraphs.select(".bar");

        // Assert
        assertEquals(1, barParagraphs.size());
        assertEquals("There", barParagraphs.text());
    }

    @Test
    public void not_removesElementsMatchingQuery() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act
        Elements notBar = paragraphs.not(".bar");

        // Assert
        assertEquals(1, notBar.size());
        assertEquals("Hello", notBar.text());
    }

    @Test
    public void eq_getsNthElementAsNewElementsObject() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act
        Elements secondParagraph = paragraphs.eq(1);

        // Assert
        assertEquals(1, secondParagraph.size());
        assertEquals("There", secondParagraph.text());
    }

    @Test
    public void is_returnsTrueIfAnyElementMatchesQuery() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act & Assert
        assertTrue(paragraphs.is(".bar"));
        assertFalse(paragraphs.is("span"));
    }

    @Test
    public void first_and_last_getCorrectElements() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");

        // Act
        Element first = paragraphs.first();
        Element last = paragraphs.last();

        // Assert
        assertNotNull(first);
        assertNotNull(last);
        assertEquals("Hello", first.text());
        assertEquals("There", last.text());
    }

    @Test
    public void parents_getsAllUniqueParents() {
        // Arrange
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        Elements parents = paragraphs.parents();

        // Assert
        // Parents are: div, body, html
        assertEquals(3, parents.size());
        assertEquals("div", parents.first().tagName());
        assertEquals("html", parents.last().tagName());
    }
    
    @Test
    public void traverse_visitsEachNode() {
        // Arrange
        Elements paragraphs = Jsoup.parse(html).select("p");
        AtomicInteger nodeCount = new AtomicInteger(0);
        
        // Act
        paragraphs.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                nodeCount.incrementAndGet();
            }
        });
        
        // Assert
        // 2 <p> elements + 2 TextNodes = 4 nodes
        assertEquals(4, nodeCount.get());
    }

    // --- Sibling Methods ---

    @Test
    public void next_getsImmediateNextSiblingOfEachElement() {
        // Arrange
        String siblingHtml = "<div><p>One</p><span>Two</span><p>Three</p></div>";
        Elements paragraphs = Jsoup.parse(siblingHtml).select("p");

        // Act
        Elements nextSiblings = paragraphs.next();

        // Assert
        assertEquals(1, nextSiblings.size());
        assertEquals("span", nextSiblings.first().tagName());
        assertEquals("Two", nextSiblings.text());
    }

    @Test
    public void prev_getsImmediatePreviousSiblingOfEachElement() {
        // Arrange
        String siblingHtml = "<div><span>One</span><p>Two</p><span>Three</span><p>Four</p></div>";
        Elements paragraphs = Jsoup.parse(siblingHtml).select("p");

        // Act
        Elements prevSiblings = paragraphs.prev();

        // Assert
        assertEquals(2, prevSiblings.size());
        assertEquals("One", prevSiblings.first().text());
        assertEquals("Three", prevSiblings.last().text());
    }

    // --- Exception Handling ---

    @Test(expected = IllegalArgumentException.class)
    public void expectFirst_withNonMatchingQuery_throwsException() {
        // Arrange
        Elements elements = Jsoup.parse(html).select("p");

        // Act
        elements.expectFirst("blockquote"); // Should throw
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrap_withEmptyString_throwsException() {
        // Arrange
        Elements elements = Jsoup.parse(html).select("p");

        // Act
        elements.wrap(""); // Should throw
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_withOutOfBoundsIndex_throwsException() {
        // Arrange
        Elements elements = Jsoup.parse(html).select("p"); // size is 2

        // Act
        elements.get(5); // Should throw
    }
    
    // --- Node Type Extraction ---
    
    @Test
    public void textNodes_returnsOnlyTextNodes() {
        // Arrange
        String mixedContent = "<div>Text 1<p>Text 2</p></div>";
        Elements divs = Jsoup.parse(mixedContent).select("div");
        
        // Act
        List<TextNode> textNodes = divs.textNodes();
        
        // Assert
        assertEquals(1, textNodes.size());
        assertEquals("Text 1", textNodes.get(0).text());
    }
}