package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for the {@link Elements} class, focusing on verifying the behavior of its methods
 * for selecting, manipulating, and traversing a list of elements.
 */
public class ElementsTest {

    // --- Selector and Filtering Tests ---

    @Test
    public void selectShouldFilterElements() {
        // Arrange
        String html = "<p>Excluded</p><div class=headline><p>Hello</p><p>There</p></div><div class=headline><h1>Headline</h1></div>";
        Document doc = Jsoup.parse(html);
        Elements headlines = doc.select(".headline");

        // Act
        Elements paragraphsInHeadlines = headlines.select("p");

        // Assert
        assertEquals(2, paragraphsInHeadlines.size());
        assertEquals("Hello", paragraphsInHeadlines.get(0).text());
        assertEquals("There", paragraphsInHeadlines.get(1).text());
    }

    @Test
    public void notShouldFilterOutElementsMatchingQuery() {
        // Arrange
        String html = "<div id=1><p>One</p></div> <div id=2><p><span>Two</span></p></div>";
        Document doc = Jsoup.parse(html);
        Elements divs = doc.select("div");

        // Act
        Elements div1 = divs.not(":has(p > span)");
        Elements div2 = divs.not("#1");

        // Assert
        assertEquals(1, div1.size());
        assertEquals("1", div1.first().id());
        assertEquals(1, div2.size());
        assertEquals("2", div2.first().id());
    }

    @Test
    public void eqShouldReturnNewElementsWithElementAtIndex() {
        // Arrange
        String html = "<p>Hello</p><p>there</p><p>world</p>";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        Elements secondParagraph = paragraphs.eq(1);

        // Assert
        assertEquals("there", secondParagraph.text());
        assertEquals("there", paragraphs.get(1).text());
    }

    @Test
    public void isShouldReturnTrueIfAnyElementMatchesQuery() {
        // Arrange
        String html = "<p>Hello</p><p title=foo>there</p><p>world</p>";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act & Assert
        assertTrue(paragraphs.is("[title=foo]"));
        assertFalse(paragraphs.is("[title=bar]"));
    }

    @Test
    void selectFirstShouldReturnFirstMatchingElement() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");

        // Act
        Element span = doc.children().selectFirst("span");

        // Assert
        assertNotNull(span);
        assertEquals("Jsoup", span.text());
    }

    @Test
    void selectFirstShouldReturnNullOnNoMatch() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");

        // Act
        Element span = doc.children().selectFirst("span");

        // Assert
        assertNull(span);
    }

    @Test
    void expectFirstShouldReturnFirstMatchingElement() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");

        // Act
        Element span = doc.children().expectFirst("span");

        // Assert
        assertNotNull(span);
        assertEquals("Jsoup", span.text());
    }

    @Test
    void expectFirstShouldThrowExceptionOnNoMatch() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");

        // Act & Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            doc.children().expectFirst("span");
        });
        assertEquals("No elements matched the query 'span' in the elements.", e.getMessage());
    }

    @Test
    void selectFirstShouldFindMatchesWithinPreviouslySelectedElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>One</p></div><div><p><span>Two</span></p></div><div><p><span>Three</span></p></div>");
        Elements divs = doc.select("div");
        assertEquals(3, divs.size());

        // Act
        Element span = divs.selectFirst("p span");

        // Assert
        assertNotNull(span);
        assertEquals("Two", span.text());
        assertNotNull(span.selectFirst("span"), "Should re-select self");
        assertNull(span.selectFirst(">span"), "Should not find children of self");
        assertNotNull(divs.selectFirst("div"), "Should re-select self from list");
        assertNull(divs.selectFirst(">div"), "Should not find children of self in list");
    }

    // --- Attribute Manipulation Tests ---

    @Test
    public void hasAttrShouldCheckForAttributePresence() {
        // Arrange
        Document doc = Jsoup.parse("<p title=foo><p title=bar><p class=foo><p class=bar>");
        Elements elementsWithTitle = doc.select("p[title]");
        Elements allParagraphs = doc.select("p");

        // Act & Assert
        assertTrue(elementsWithTitle.hasAttr("title"));
        assertFalse(elementsWithTitle.hasAttr("class"));
        assertTrue(allParagraphs.hasAttr("class"));
        assertFalse(allParagraphs.hasAttr("style"));
    }

    @Test
    public void hasAttrShouldWorkWithAbsoluteAttributeKeys() {
        // Arrange
        Document doc = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");
        Elements linkOne = doc.select("#1");
        Elements linkTwo = doc.select("#2");
        Elements bothLinks = doc.select("a");

        // Act & Assert
        assertFalse(linkOne.hasAttr("abs:href"));
        assertTrue(linkTwo.hasAttr("abs:href"));
        assertTrue(bothLinks.hasAttr("abs:href"), "Should be true as one element has the attribute");
    }

    @Test
    public void attrShouldGetValueFromFirstElement() {
        // Arrange
        String html = "<p title=foo><p title=bar><p class=foo><p class=bar>";
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("p");

        // Act
        String classVal = elements.attr("class");
        String titleVal = elements.attr("title");

        // Assert
        assertEquals("foo", classVal);
        assertEquals("foo", titleVal);
    }

    @Test
    public void attrShouldGetAbsoluteUrlWithAbsKey() {
        // Arrange
        Document doc = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");
        Elements linkOne = doc.select("#1");
        Elements linkTwo = doc.select("#2");
        Elements bothLinks = doc.select("a");

        // Act & Assert
        assertEquals("", linkOne.attr("abs:href"));
        assertEquals("https://jsoup.org", linkTwo.attr("abs:href"));
        assertEquals("https://jsoup.org", bothLinks.attr("abs:href"));
    }

    @Test
    public void attrShouldSetValueOnAllElements() {
        // Arrange
        String html = "<p title=foo><p title=bar><p class=foo><p class=bar>";
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("p");

        // Act
        elements.attr("style", "classy");

        // Assert
        assertEquals(4, elements.size());
        for (Element el : elements) {
            assertEquals("classy", el.attr("style"));
        }
        assertEquals("classy", elements.last().attr("style"));
        assertEquals("bar", elements.last().attr("class"));
    }

    @Test
    public void removeAttrShouldRemoveAttributeFromAllElements() {
        // Arrange
        String html = "<p title=foo><p title=bar>";
        Document doc = Jsoup.parse(html);
        Elements elementsWithTitle = doc.select("p[title]");
        assertEquals(2, elementsWithTitle.size(), "Pre-condition: two elements with title");

        // Act
        elementsWithTitle.removeAttr("title");

        // Assert
        assertEquals(2, elementsWithTitle.size(), "Elements list is not re-evaluated after modification");
        assertEquals(0, doc.select("p[title]").size(), "Attribute should be removed from the DOM");
    }

    @Test
    public void eachAttrShouldReturnAttributeOfEachElement() {
        // Arrange
        Document doc = Jsoup.parse(
                "<div><a href='/foo'>1</a><a href='http://example.com/bar'>2</a><a href=''>3</a><a>4</a>",
                "http://example.com");
        Elements links = doc.select("a");

        // Act
        List<String> hrefAttrs = links.eachAttr("href");
        List<String> absAttrs = links.eachAttr("abs:href");

        // Assert
        assertEquals(List.of("/foo", "http://example.com/bar", ""), hrefAttrs);
        assertEquals(List.of("http://example.com/foo", "http://example.com/bar", "http://example.com"), absAttrs);
    }

    // --- Class Manipulation Tests ---

    @Test
    public void hasClassShouldReturnTrueIfAnyElementHasTheClass() {
        // Arrange
        Document doc = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p>");
        Elements paragraphs = doc.select("p");

        // Act & Assert
        assertTrue(paragraphs.hasClass("red"));
        assertFalse(paragraphs.hasClass("blue"));
    }

    @Test
    public void hasClassShouldBeCaseInsensitive() {
        // Arrange
        Elements paragraphs = Jsoup.parse("<p Class=One>One <p class=Two>Two <p CLASS=THREE>THREE").select("p");
        Element one = paragraphs.get(0);
        Element two = paragraphs.get(1);
        Element three = paragraphs.get(2);

        // Act & Assert
        assertTrue(one.hasClass("One"));
        assertTrue(one.hasClass("ONE"));
        assertTrue(two.hasClass("TWO"));
        assertTrue(two.hasClass("Two"));
        assertTrue(three.hasClass("ThreE"));
        assertTrue(three.hasClass("three"));
    }

    @Test
    public void getElementsByClassShouldSupportHyphens() {
        // Arrange
        Document doc = Jsoup.parse("<p class='tab-nav'>Check</p>");

        // Act
        Elements els = doc.getElementsByClass("tab-nav");

        // Assert
        assertEquals(1, els.size());
        assertEquals("Check", els.text());
    }

    @Test
    public void addClassShouldAddClassToAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p>");
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.addClass("blue");

        // Assert
        assertEquals("mellow yellow blue", paragraphs.get(0).className());
        assertEquals("red green blue", paragraphs.get(1).className());
    }

    @Test
    public void removeClassShouldRemoveClassFromAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p>");
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.removeClass("yellow");

        // Assert
        assertEquals("mellow", paragraphs.get(0).className());
        assertEquals("red green", paragraphs.get(1).className());
    }

    @Test
    public void toggleClassShouldToggleClassesOnAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p>");
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.toggleClass("mellow"); // remove from first, add to second
        paragraphs.toggleClass("red"); // remove from second, add to first

        // Assert
        assertEquals("yellow red", paragraphs.get(0).className());
        assertEquals("green mellow", paragraphs.get(1).className());
    }

    // --- HTML, Text, and Value Content Tests ---

    @Test
    public void textShouldReturnCombinedTextOfAllElements() {
        // Arrange
        String html = "<div><p>Hello</p><p>there</p><p>world</p></div>";
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("div > *");

        // Act
        String combinedText = elements.text();

        // Assert
        assertEquals("Hello there world", combinedText);
    }

    @Test
    public void hasTextShouldReturnTrueIfAnyElementHasText() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p></p></div>");
        Elements divs = doc.select("div");
        Elements emptyDiv = doc.select("div + div");

        // Act & Assert
        assertTrue(divs.hasText());
        assertFalse(emptyDiv.hasText());
    }

    @Test
    public void eachTextShouldReturnTextOfEachElement() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>1</p><p>2</p></div><div><p>3</p><p>4</p><p></p></div>");
        Elements paragraphs = doc.select("p");
        assertEquals(5, paragraphs.size());

        // Act
        List<String> pText = paragraphs.eachText();

        // Assert
        assertEquals(4, pText.size(), "Should not include elements without text");
        assertEquals(List.of("1", "2", "3", "4"), pText);
    }

    @Test
    public void htmlShouldReturnCombinedInnerHtmlOfAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p>There</p></div>");
        Elements divs = doc.select("div");

        // Act
        String innerHtml = divs.html();

        // Assert
        assertEquals("<p>Hello</p>\n<p>There</p>", innerHtml);
    }

    @Test
    public void outerHtmlShouldReturnCombinedOuterHtmlOfAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p>There</p></div>");
        Elements divs = doc.select("div");

        // Act
        String outerHtml = divs.outerHtml();

        // Assert
        assertEquals("<div><p>Hello</p></div><div><p>There</p></div>", TextUtil.stripNewlines(outerHtml));
    }

    @Test
    public void tagNameShouldRenameAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<p>Hello <i>there</i> <i>now</i></p>");
        Elements italics = doc.select("i");

        // Act
        italics.tagName("em");

        // Assert
        assertEquals("<p>Hello <em>there</em> <em>now</em></p>", doc.body().html());
    }

    @Test
    public void htmlShouldReplaceInnerHtmlOfEachElement() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p>");
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.html("<span>Gone</span>");

        // Assert
        assertEquals("<p><span>Gone</span></p>", TextUtil.stripNewlines(paragraphs.get(0).outerHtml()));
        assertEquals("<p><span>Gone</span></p>", TextUtil.stripNewlines(paragraphs.get(1).outerHtml()));
    }

    @Test
    public void prependShouldAddHtmlToStartOfEachElement() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p>");
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.prepend("<b>Bold</b> ");

        // Assert
        assertEquals("<p><b>Bold</b> One</p>", TextUtil.stripNewlines(paragraphs.get(0).outerHtml()));
        assertEquals("<p><b>Bold</b> Two</p>", TextUtil.stripNewlines(paragraphs.get(1).outerHtml()));
    }

    @Test
    public void appendShouldAddHtmlToEndOfEachElement() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p>");
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.append(" <i>Ital</i>");

        // Assert
        assertEquals("<p>One <i>Ital</i></p>", TextUtil.stripNewlines(paragraphs.get(0).outerHtml()));
        assertEquals("<p>Two <i>Ital</i></p>", TextUtil.stripNewlines(paragraphs.get(1).outerHtml()));
    }

    @Test
    public void beforeShouldInsertHtmlBeforeEachElement() {
        // Arrange
        Document doc = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");
        Elements links = doc.select("a");

        // Act
        links.before("<span>foo</span>");

        // Assert
        assertEquals("<p>This <span>foo</span><a>is</a> <span>foo</span><a>jsoup</a>.</p>", TextUtil.stripNewlines(doc.body().html()));
    }

    @Test
    public void afterShouldInsertHtmlAfterEachElement() {
        // Arrange
        Document doc = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");
        Elements links = doc.select("a");

        // Act
        links.after("<span>foo</span>");

        // Assert
        assertEquals("<p>This <a>is</a><span>foo</span> <a>jsoup</a><span>foo</span>.</p>", TextUtil.stripNewlines(doc.body().html()));
    }

    @Test
    public void valShouldGetValueFromFirstElement() {
        // Arrange
        Document doc = Jsoup.parse("<input value='one' /><textarea>two</textarea>");
        Elements formElements = doc.select("input, textarea");

        // Act
        String val = formElements.val();

        // Assert
        assertEquals("one", val);
        assertEquals("two", formElements.last().val());
    }

    @Test
    public void valShouldSetValueOnAllElements() {
        // Arrange
        Document doc = Jsoup.parse("<input value='one' /><textarea>two</textarea>");
        Elements formElements = doc.select("input, textarea");

        // Act
        formElements.val("three");

        // Assert
        assertEquals("three", formElements.first().val());
        assertEquals("three", formElements.last().val());
        assertEquals("<textarea>three</textarea>", formElements.last().outerHtml());
    }

    // --- DOM Manipulation Tests (Wrap, Unwrap, Empty, Remove) ---

    @Test
    public void wrapShouldWrapHtmlAroundEachElement() {
        // Arrange
        String html = "<p><b>This</b> is <b>jsoup</b></p>";
        Document doc = Jsoup.parse(html);
        Elements boldTags = doc.select("b");

        // Act
        boldTags.wrap("<i></i>");

        // Assert
        assertEquals("<p><i><b>This</b></i> is <i><b>jsoup</b></i></p>", doc.body().html());
    }

    @Test
    public void wrapShouldCorrectlyWrapBlockElements() {
        // Arrange
        String html = "<p><b>This</b> is <b>jsoup</b>.</p> <p>How do you like it?</p>";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.wrap("<div></div>");

        // Assert
        assertEquals(
                "<div>\n <p><b>This</b> is <b>jsoup</b>.</p>\n</div>\n<div>\n <p>How do you like it?</p>\n</div>",
                doc.body().html());
    }

    @Test
    public void unwrapShouldRemoveParentsAndKeepChildren() {
        // Arrange
        String html = "<div><font>One</font> <font><a href=\"/\">Two</a></font></div>";
        Document doc = Jsoup.parse(html);
        Elements fontTags = doc.select("font");

        // Act
        fontTags.unwrap();

        // Assert
        assertEquals("<div>\n One <a href=\"/\">Two</a>\n</div>", doc.body().html());
    }

    @Test
    public void unwrapShouldHandleMixedContent() {
        // Arrange
        String html = "<p><a>One</a> Two</p> Three <i>Four</i> <p>Five <i>Six</i></p>";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.unwrap();

        // Assert
        assertEquals("<a>One</a> Two Three <i>Four</i> Five <i>Six</i>", TextUtil.stripNewlines(doc.body().html()));
    }

    @Test
    public void unwrapShouldPreserveWhitespaceBetweenUnwrappedElements() {
        // Arrange
        String html = "<p>One <span>two</span> <span>three</span> four</p>";
        Document doc = Jsoup.parse(html);
        Elements spans = doc.select("span");

        // Act
        spans.unwrap();

        // Assert
        assertEquals("<p>One two three four</p>", doc.body().html());
    }

    @Test
    public void emptyShouldRemoveAllChildrenFromEachElement() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello <b>there</b></p> <p>now!</p></div>");
        doc.outputSettings().prettyPrint(false);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.empty();

        // Assert
        assertEquals("<div><p></p> <p></p></div>", doc.body().html());
    }

    @Test
    public void removeShouldRemoveEachElementFromDom() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello <b>there</b></p> jsoup <p>now!</p></div>");
        doc.outputSettings().prettyPrint(false);
        Elements paragraphs = doc.select("p");

        // Act
        paragraphs.remove();

        // Assert
        assertEquals("<div> jsoup </div>", doc.body().html());
    }

    // --- Traversal Tests ---

    @Test
    public void parentsShouldGetAllUniqueParents() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><p>There</p>");
        Elements paragraphs = doc.select("p");

        // Act
        Elements parents = paragraphs.parents();

        // Assert
        assertEquals(3, parents.size());
        assertEquals("div", parents.get(0).tagName());
        assertEquals("body", parents.get(1).tagName());
        assertEquals("html", parents.get(2).tagName());
    }

    @Test
    public void nextShouldGetImmediateFollowingSibling() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>1</p><p>2</p><p>3</p></div>");
        Elements p2 = doc.select("p:eq(1)");

        // Act
        Elements next = p2.next();

        // Assert
        assertEquals(1, next.size());
        assertEquals("3", next.text());
    }

    @Test
    public void nextWithSelectorShouldFilterImmediateFollowingSibling() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>1</p><p class=x>2</p><p>3</p><p class=x>4</p></div>");
        Elements p1 = doc.select("p:eq(0)");
        Elements p2 = doc.select("p:eq(1)");

        // Act
        Elements nextFromP1 = p1.next("p.x");
        Elements nextFromP2 = p2.next("p.x");

        // Assert
        assertEquals(1, nextFromP1.size());
        assertEquals("2", nextFromP1.text());
        assertEquals(0, nextFromP2.size());
    }

    @Test
    public void nextAllShouldGetAllFollowingSiblings() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>1</p><p>2</p><p>3</p></div>");
        Elements p1 = doc.select("p:eq(0)");

        // Act
        Elements nextAll = p1.nextAll();

        // Assert
        assertEquals(2, nextAll.size());
        assertEquals("2", nextAll.first().text());
        assertEquals("3", nextAll.last().text());
    }

    @Test
    public void nextAllWithSelectorShouldFilterAllFollowingSiblings() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>1</p><p class=x>2</p><p>3</p><p class=x>4</p></div>");
        Elements p1 = doc.select("p:eq(0)");

        // Act
        Elements nextAllFiltered = p1.nextAll("p.x");

        // Assert
        assertEquals(2, nextAllFiltered.size());
        assertEquals("2", nextAllFiltered.first().text());
        assertEquals("4", nextAllFiltered.last().text());
    }

    @Test
    public void prevShouldGetImmediatePrecedingSibling() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>1</p><p>2</p><p>3</p></div>");
        Elements p2 = doc.select("p:eq(1)");

        // Act
        Elements prev = p2.prev();

        // Assert
        assertEquals(1, prev.size());
        assertEquals("1", prev.text());
    }

    @Test
    public void prevWithSelectorShouldFilterImmediatePrecedingSibling() {
        // Arrange
        Document doc = Jsoup.parse("<div><p class=x>1</p><p>2</p><p class=x>3</p><p>4</p></div>");
        Elements p2 = doc.select("p:eq(1)");
        Elements p4 = doc.select("p:eq(3)");

        // Act
        Elements prevFromP2 = p2.prev(".x");
        Elements prevFromP4 = p4.prev(".x");

        // Assert
        assertEquals(1, prevFromP2.size());
        assertEquals("1", prevFromP2.text());
        assertEquals(1, prevFromP4.size());
        assertEquals("3", prevFromP4.text());
    }

    @Test
    public void prevAllShouldGetAllPrecedingSiblings() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>1</p><p>2</p><p>3</p></div>");
        Elements p3 = doc.select("p:eq(2)");

        // Act
        Elements prevAll = p3.prevAll();

        // Assert
        assertEquals(2, prevAll.size());
        assertEquals("2", prevAll.first().text());
        assertEquals("1", prevAll.last().text());
    }

    @Test
    public void prevAllWithSelectorShouldFilterAllPrecedingSiblings() {
        // Arrange
        Document doc = Jsoup.parse("<div><p class=x>1</p><p>2</p><p class=x>3</p><p>4</p></div>");
        Elements p4 = doc.select("p:eq(3)");

        // Act
        Elements prevAllFiltered = p4.prevAll(".x");

        // Assert
        assertEquals(2, prevAllFiltered.size());
        assertEquals("3", prevAllFiltered.first().text());
        assertEquals("1", prevAllFiltered.last().text());
    }

    @Test
    public void traverseShouldVisitEachNodeInElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div>There</div>");
        final StringBuilder accum = new StringBuilder();
        NodeVisitor visitor = new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                accum.append("<").append(node.nodeName()).append(">");
            }

            @Override
            public void tail(Node node, int depth) {
                accum.append("</").append(node.nodeName()).append(">");
            }
        };

        // Act
        doc.select("div").traverse(visitor);

        // Assert
        assertEquals("<div><p><#text></#text></p></div><div><#text></#text></div>", accum.toString());
    }

    // --- Node Type Extraction Tests ---

    @Test
    public void formsShouldReturnFormElements() {
        // Arrange
        Document doc = Jsoup.parse("<form id=1><input name=q></form><div /><form id=2><input name=f></form>");
        Elements formAndDivs = doc.select("form, div");
        assertEquals(3, formAndDivs.size());

        // Act
        List<FormElement> forms = formAndDivs.forms();

        // Assert
        assertEquals(2, forms.size());
        assertEquals("1", forms.get(0).id());
        assertEquals("2", forms.get(1).id());
    }

    @Test
    public void commentsShouldReturnChildComments() {
        // Arrange
        Document doc = Jsoup.parse("<!-- comment1 --><p><!-- comment2 --></p><p class=two><!-- comment3 --></p>");
        Elements paragraphs = doc.select("p");
        Elements pTwo = doc.select("p.two");

        // Act
        List<Comment> comments = paragraphs.comments();
        List<Comment> comments1 = pTwo.comments();

        // Assert
        assertEquals(2, comments.size());
        assertEquals(" comment2 ", comments.get(0).getData());
        assertEquals(" comment3 ", comments.get(1).getData());
        assertEquals(1, comments1.size());
        assertEquals(" comment3 ", comments1.get(0).getData());
    }

    @Test
    public void textNodesShouldReturnChildTextNodes() {
        // Arrange
        Document doc = Jsoup.parse("One<p>Two<a>Three</a></p><p>Four</p>Five");
        Elements paragraphs = doc.select("p");

        // Act
        List<TextNode> textNodes = paragraphs.textNodes();

        // Assert
        assertEquals(2, textNodes.size());
        assertEquals("Two", textNodes.get(0).text());
        assertEquals("Four", textNodes.get(1).text());
    }

    @Test
    public void dataNodesShouldReturnChildDataNodes() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><script>Two</script><style>Three</style>");
        Elements elements = doc.select("p, script, style");

        // Act
        List<DataNode> dataNodes = elements.dataNodes();

        // Assert
        assertEquals(2, dataNodes.size());
        assertEquals("Two", dataNodes.get(0).getWholeData());
        assertEquals("Three", dataNodes.get(1).getWholeData());
    }

    @Test
    public void dataNodesShouldBeLive() {
        // Arrange
        Document doc = Jsoup.parse("<head><script type=application/json><crux></script>");
        Elements script = doc.select("script[type=application/json]");

        // Act
        List<DataNode> scriptDataNodes = script.dataNodes();
        DataNode dataNode = scriptDataNodes.get(0);
        dataNode.setWholeData("<cromulent>");

        // Assert
        assertEquals(1, scriptDataNodes.size());
        assertEquals("<script type=\"application/json\"><cromulent></script>", script.outerHtml());
    }

    @Test
    public void childNodesMethodsShouldReturnEmptyListOnNoMatch() {
        // Arrange
        Document doc = Jsoup.parse("<p></p>");

        // Act & Assert
        assertEquals(0, doc.select("form").textNodes().size());
        assertEquals(0, doc.select("form").dataNodes().size());
        assertEquals(0, doc.select("form").comments().size());
    }

    // --- List Modification Tests ---

    @Test
    public void setByIndexShouldReplaceElementInListAndDom() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Element newP = doc.createElement("p").text("New").attr("id", "new");
        Elements paragraphs = doc.select("p");
        Element originalSecond = paragraphs.get(1);

        // Act
        Element replacedElement = paragraphs.set(1, newP);

        // Assert
        assertSame(originalSecond, replacedElement);
        assertSame(newP, paragraphs.get(1), "Element should be replaced in list");
        assertEquals("<p>One</p>\n<p id=\"new\">New</p>\n<p>Three</p>", doc.body().html(), "Element should be replaced in DOM");
    }

    @Test
    public void removeByIndexShouldRemoveElementFromListAndDom() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements paragraphs = doc.select("p");
        Element originalSecond = paragraphs.get(1);

        // Act
        Element removedElement = paragraphs.remove(1);

        // Assert
        assertSame(originalSecond, removedElement);
        assertEquals(2, paragraphs.size(), "Element should be removed from list");
        assertFalse(paragraphs.contains(originalSecond));
        assertEquals("<p>One</p>\n<p>Three</p>", doc.body().html(), "Element should be removed from DOM");
    }

    @Test
    public void removeByObjectShouldRemoveElementFromListAndDom() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements paragraphs = doc.select("p");
        Element elementToRemove = paragraphs.get(1);

        // Act
        boolean wasRemoved = paragraphs.remove(elementToRemove);

        // Assert
        assertTrue(wasRemoved);
        assertEquals(2, paragraphs.size(), "Element should be removed from list");
        assertFalse(paragraphs.contains(elementToRemove));
        assertEquals("<p>One</p>\n<p>Three</p>", doc.body().html(), "Element should be removed from DOM");
    }

    @Test
    public void removeByObjectShouldDoNothingIfElementNotPresent() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        String originalHtml = doc.html();
        Element newP = doc.createElement("p").text("New");
        Elements paragraphs = doc.select("p");
        int originalSize = paragraphs.size();

        // Act
        boolean wasRemoved = paragraphs.remove(newP);

        // Assert
        assertFalse(wasRemoved);
        assertEquals(originalSize, paragraphs.size());
        assertEquals(originalHtml, doc.html());
    }

    @Test
    public void clearShouldRemoveAllElementsFromListAndDom() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><div>Three</div>");
        Elements paragraphs = doc.select("p");
        assertEquals(2, paragraphs.size());

        // Act
        paragraphs.clear();

        // Assert
        assertEquals(0, paragraphs.size());
        assertEquals(0, doc.select("p").size(), "Elements should be removed from DOM");
    }

    @Test
    public void removeAllShouldRemoveSpecifiedElementsFromListAndDom() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p><p>Four</p><div>Div</div>");
        Elements allParagraphs = doc.select("p");
        Elements middleParagraphs = doc.select("p:gt(0):lt(3)"); // Two and Three
        assertEquals(4, allParagraphs.size());
        assertEquals(2, middleParagraphs.size());

        // Act
        boolean wasRemoved = allParagraphs.removeAll(middleParagraphs);

        // Assert
        assertTrue(wasRemoved);
        assertEquals(2, allParagraphs.size());
        assertEquals("<p>One</p>\n<p>Four</p>\n<div>Div</div>", doc.body().html());
    }

    @Test
    public void retainAllShouldKeepSpecifiedElementsAndRemoveOthers() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p><p>Four</p><div>Div</div>");
        Elements allParagraphs = doc.select("p");
        Elements middleParagraphs = doc.select("p:gt(0):lt(3)"); // Two and Three
        assertEquals(4, allParagraphs.size());
        assertEquals(2, middleParagraphs.size());

        // Act
        boolean wasChanged = allParagraphs.retainAll(middleParagraphs);

        // Assert
        assertTrue(wasChanged);
        assertEquals(2, allParagraphs.size());
        assertEquals("<p>Two</p>\n<p>Three</p>\n<div>Div</div>", doc.body().html());
    }

    @Test
    public void iteratorRemoveShouldAlsoRemoveFromDom() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p><p>Four</p>");
        Elements paragraphs = doc.select("p");
        assertEquals(4, paragraphs.size());

        // Act
        for (Iterator<Element> it = paragraphs.iterator(); it.hasNext(); ) {
            Element el = it.next();
            if (el.text().contains("Two")) {
                it.remove();
            }
        }

        // Assert
        assertEquals(3, paragraphs.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
    }

    @Test
    public void removeIfShouldRemoveElementsMatchingPredicate() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p><p>Four</p>");
        Elements paragraphs = doc.select("p");
        assertEquals(4, paragraphs.size());

        // Act
        boolean wasRemoved = paragraphs.removeIf(el -> el.text().contains("Two"));

        // Assert
        assertTrue(wasRemoved);
        assertEquals(3, paragraphs.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
        assertFalse(paragraphs.removeIf(el -> el.text().contains("Five")));
    }

    @Test
    public void removeIfShouldSupportConcurrentReadOnSameList() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p><p>Four</p>");
        Elements paragraphs = doc.select("p");
        assertEquals(4, paragraphs.size());

        // Act
        boolean wasRemoved = paragraphs.removeIf(paragraphs::contains);

        // Assert
        assertTrue(wasRemoved);
        assertEquals(0, paragraphs.size());
        assertEquals("", doc.body().html());
    }

    @Test
    public void replaceAllShouldReplaceEachElementInListAndDom() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p><p>Four</p>");
        Elements paragraphs = doc.select("p");
        assertEquals(4, paragraphs.size());

        // Act
        paragraphs.replaceAll(el -> {
            Element div = doc.createElement("div");
            div.text(el.text());
            return div;
        });

        // Assert
        for (Element p : paragraphs) {
            assertEquals("div", p.tagName());
        }
        assertEquals("<div>One</div><div>Two</div><div>Three</div><div>Four</div>", TextUtil.normalizeSpaces(doc.body().html()));
    }
}