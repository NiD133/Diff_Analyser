package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Elements class, which provides a list of Element nodes with methods that act on every element in the list.
 * These tests ensure the correct functionality of element selection, manipulation, and traversal.
 */
public class ElementsTest {

    @Test
    public void testFilterByClassAndTag() {
        String html = "<p>Excl</p><div class='headline'><p>Hello</p><p>There</p></div><div class='headline'><h1>Headline</h1></div>";
        Document doc = Jsoup.parse(html);
        Elements paragraphsInHeadline = doc.select(".headline").select("p");

        assertEquals(2, paragraphsInHeadline.size());
        assertEquals("Hello", paragraphsInHeadline.get(0).text());
        assertEquals("There", paragraphsInHeadline.get(1).text());
    }

    @Test
    public void testAttributesManipulation() {
        String html = "<p title='foo'><p title='bar'><p class='foo'><p class='bar'>";
        Document doc = Jsoup.parse(html);
        Elements elementsWithTitle = doc.select("p[title]");

        assertEquals(2, elementsWithTitle.size());
        assertTrue(elementsWithTitle.hasAttr("title"));
        assertFalse(elementsWithTitle.hasAttr("class"));
        assertEquals("foo", elementsWithTitle.attr("title"));

        elementsWithTitle.removeAttr("title");
        assertEquals(2, elementsWithTitle.size()); // Elements are not reevaluated after attribute removal
        assertEquals(0, doc.select("p[title]").size());

        Elements allParagraphs = doc.select("p").attr("style", "classy");
        assertEquals(4, allParagraphs.size());
        assertEquals("classy", allParagraphs.last().attr("style"));
        assertEquals("bar", allParagraphs.last().attr("class"));
    }

    @Test
    public void testHasAttribute() {
        Document doc = Jsoup.parse("<p title='foo'><p title='bar'><p class='foo'><p class='bar'>");
        Elements paragraphs = doc.select("p");

        assertTrue(paragraphs.hasAttr("class"));
        assertFalse(paragraphs.hasAttr("style"));
    }

    @Test
    public void testHasAbsoluteAttribute() {
        Document doc = Jsoup.parse("<a id='1' href='/foo'>One</a> <a id='2' href='https://jsoup.org'>Two</a>");
        Elements firstLink = doc.select("#1");
        Elements secondLink = doc.select("#2");
        Elements allLinks = doc.select("a");

        assertFalse(firstLink.hasAttr("abs:href"));
        assertTrue(secondLink.hasAttr("abs:href"));
        assertTrue(allLinks.hasAttr("abs:href")); // At least one element has the attribute
    }

    @Test
    public void testGetAttribute() {
        Document doc = Jsoup.parse("<p title='foo'><p title='bar'><p class='foo'><p class='bar'>");
        String classValue = doc.select("p").attr("class");

        assertEquals("foo", classValue);
    }

    @Test
    public void testGetAbsoluteAttribute() {
        Document doc = Jsoup.parse("<a id='1' href='/foo'>One</a> <a id='2' href='https://jsoup.org'>Two</a>");
        Elements firstLink = doc.select("#1");
        Elements secondLink = doc.select("#2");
        Elements allLinks = doc.select("a");

        assertEquals("", firstLink.attr("abs:href"));
        assertEquals("https://jsoup.org", secondLink.attr("abs:href"));
        assertEquals("https://jsoup.org", allLinks.attr("abs:href"));
    }

    @Test
    public void testClassManipulation() {
        Document doc = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p>");
        Elements paragraphs = doc.select("p");

        assertTrue(paragraphs.hasClass("red"));
        assertFalse(paragraphs.hasClass("blue"));

        paragraphs.addClass("blue");
        paragraphs.removeClass("yellow");
        paragraphs.toggleClass("mellow");

        assertEquals("blue", paragraphs.get(0).className());
        assertEquals("red green blue mellow", paragraphs.get(1).className());
    }

    @Test
    public void testHasClassCaseInsensitive() {
        Elements paragraphs = Jsoup.parse("<p Class='One'>One <p class='Two'>Two <p CLASS='THREE'>THREE").select("p");

        assertTrue(paragraphs.get(0).hasClass("One"));
        assertTrue(paragraphs.get(0).hasClass("ONE"));

        assertTrue(paragraphs.get(1).hasClass("TWO"));
        assertTrue(paragraphs.get(1).hasClass("Two"));

        assertTrue(paragraphs.get(2).hasClass("ThreE"));
        assertTrue(paragraphs.get(2).hasClass("three"));
    }

    @Test
    public void testGetTextContent() {
        String html = "<div><p>Hello<p>there<p>world</div>";
        Document doc = Jsoup.parse(html);

        assertEquals("Hello there world", doc.select("div > *").text());
    }

    @Test
    public void testHasTextContent() {
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p></p></div>");
        Elements divs = doc.select("div");

        assertTrue(divs.hasText());
        assertFalse(doc.select("div + div").hasText());
    }

    @Test
    public void testGetInnerHtml() {
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p>There</p></div>");
        Elements divs = doc.select("div");

        assertEquals("<p>Hello</p>\n<p>There</p>", divs.html());
    }

    @Test
    public void testGetOuterHtml() {
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p>There</p></div>");
        Elements divs = doc.select("div");

        assertEquals("<div><p>Hello</p></div><div><p>There</p></div>", TextUtil.stripNewlines(divs.outerHtml()));
    }

    @Test
    public void testSetInnerHtml() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements paragraphs = doc.select("p");

        paragraphs.prepend("<b>Bold</b>").append("<i>Ital</i>");
        assertEquals("<p><b>Bold</b>Two<i>Ital</i></p>", TextUtil.stripNewlines(paragraphs.get(1).outerHtml()));

        paragraphs.html("<span>Gone</span>");
        assertEquals("<p><span>Gone</span></p>", TextUtil.stripNewlines(paragraphs.get(1).outerHtml()));
    }

    @Test
    public void testGetAndSetValue() {
        Document doc = Jsoup.parse("<input value='one' /><textarea>two</textarea>");
        Elements inputsAndTextareas = doc.select("input, textarea");

        assertEquals(2, inputsAndTextareas.size());
        assertEquals("one", inputsAndTextareas.val());
        assertEquals("two", inputsAndTextareas.last().val());

        inputsAndTextareas.val("three");
        assertEquals("three", inputsAndTextareas.first().val());
        assertEquals("three", inputsAndTextareas.last().val());
        assertEquals("<textarea>three</textarea>", inputsAndTextareas.last().outerHtml());
    }

    @Test
    public void testInsertHtmlBefore() {
        Document doc = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");
        doc.select("a").before("<span>foo</span>");

        assertEquals("<p>This <span>foo</span><a>is</a> <span>foo</span><a>jsoup</a>.</p>", TextUtil.stripNewlines(doc.body().html()));
    }

    @Test
    public void testInsertHtmlAfter() {
        Document doc = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");
        doc.select("a").after("<span>foo</span>");

        assertEquals("<p>This <a>is</a><span>foo</span> <a>jsoup</a><span>foo</span>.</p>", TextUtil.stripNewlines(doc.body().html()));
    }

    @Test
    public void testWrapElements() {
        String html = "<p><b>This</b> is <b>jsoup</b></p>";
        Document doc = Jsoup.parse(html);
        doc.select("b").wrap("<i></i>");

        assertEquals("<p><i><b>This</b></i> is <i><b>jsoup</b></i></p>", doc.body().html());
    }

    @Test
    public void testWrapElementsWithDiv() {
        String html = "<p><b>This</b> is <b>jsoup</b>.</p> <p>How do you like it?</p>";
        Document doc = Jsoup.parse(html);
        doc.select("p").wrap("<div></div>");

        assertEquals("<div>\n <p><b>This</b> is <b>jsoup</b>.</p>\n</div>\n<div>\n <p>How do you like it?</p>\n</div>", doc.body().html());
    }

    @Test
    public void testUnwrapElements() {
        String html = "<div><font>One</font> <font><a href=\"/\">Two</a></font></div>";
        Document doc = Jsoup.parse(html);
        doc.select("font").unwrap();

        assertEquals("<div>\n One <a href=\"/\">Two</a>\n</div>", doc.body().html());
    }

    @Test
    public void testUnwrapParagraphs() {
        String html = "<p><a>One</a> Two</p> Three <i>Four</i> <p>Fix <i>Six</i></p>";
        Document doc = Jsoup.parse(html);
        doc.select("p").unwrap();

        assertEquals("<a>One</a> Two Three <i>Four</i> Fix <i>Six</i>", TextUtil.stripNewlines(doc.body().html()));
    }

    @Test
    public void testUnwrapKeepsSpace() {
        String html = "<p>One <span>two</span> <span>three</span> four</p>";
        Document doc = Jsoup.parse(html);
        doc.select("span").unwrap();

        assertEquals("<p>One two three four</p>", doc.body().html());
    }

    @Test
    public void testEmptyElements() {
        Document doc = Jsoup.parse("<div><p>Hello <b>there</b></p> <p>now!</p></div>");
        doc.outputSettings().prettyPrint(false);

        doc.select("p").empty();
        assertEquals("<div><p></p> <p></p></div>", doc.body().html());
    }

    @Test
    public void testRemoveElements() {
        Document doc = Jsoup.parse("<div><p>Hello <b>there</b></p> jsoup <p>now!</p></div>");
        doc.outputSettings().prettyPrint(false);

        doc.select("p").remove();
        assertEquals("<div> jsoup </div>", doc.body().html());
    }

    @Test
    public void testSelectElementByIndex() {
        String html = "<p>Hello<p>there<p>world";
        Document doc = Jsoup.parse(html);

        assertEquals("there", doc.select("p").eq(1).text());
        assertEquals("there", doc.select("p").get(1).text());
    }

    @Test
    public void testIsElementMatchingQuery() {
        String html = "<p>Hello<p title='foo'>there<p>world";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        assertTrue(paragraphs.is("[title=foo]"));
        assertFalse(paragraphs.is("[title=bar]"));
    }

    @Test
    public void testGetParentElements() {
        Document doc = Jsoup.parse("<div><p>Hello</p></div><p>There</p>");
        Elements parents = doc.select("p").parents();

        assertEquals(3, parents.size());
        assertEquals("div", parents.get(0).tagName());
        assertEquals("body", parents.get(1).tagName());
        assertEquals("html", parents.get(2).tagName());
    }

    @Test
    public void testExcludeElementsFromSelection() {
        Document doc = Jsoup.parse("<div id='1'><p>One</p></div> <div id='2'><p><span>Two</span></p></div>");

        Elements divWithoutSpan = doc.select("div").not(":has(p > span)");
        assertEquals(1, divWithoutSpan.size());
        assertEquals("1", divWithoutSpan.first().id());

        Elements divWithoutId1 = doc.select("div").not("#1");
        assertEquals(1, divWithoutId1.size());
        assertEquals("2", divWithoutId1.first().id());
    }

    @Test
    public void testSetTagName() {
        Document doc = Jsoup.parse("<p>Hello <i>there</i> <i>now</i></p>");
        doc.select("i").tagName("em");

        assertEquals("<p>Hello <em>there</em> <em>now</em></p>", doc.body().html());
    }

    @Test
    public void testTraverseElements() {
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div>There</div>");
        final StringBuilder accum = new StringBuilder();

        doc.select("div").traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                accum.append("<").append(node.nodeName()).append(">");
            }

            @Override
            public void tail(Node node, int depth) {
                accum.append("</").append(node.nodeName()).append(">");
            }
        });

        assertEquals("<div><p><#text></#text></p></div><div><#text></#text></div>", accum.toString());
    }

    @Test
    public void testGetForms() {
        Document doc = Jsoup.parse("<form id='1'><input name='q'></form><div /><form id='2'><input name='f'></form>");
        Elements elements = doc.select("form, div");

        assertEquals(3, elements.size());

        List<FormElement> forms = elements.forms();
        assertEquals(2, forms.size());
        assertNotNull(forms.get(0));
        assertNotNull(forms.get(1));
        assertEquals("1", forms.get(0).id());
        assertEquals("2", forms.get(1).id());
    }

    @Test
    public void testGetComments() {
        Document doc = Jsoup.parse("<!-- comment1 --><p><!-- comment2 --><p class='two'><!-- comment3 -->");
        List<Comment> comments = doc.select("p").comments();

        assertEquals(2, comments.size());
        assertEquals(" comment2 ", comments.get(0).getData());
        assertEquals(" comment3 ", comments.get(1).getData());

        List<Comment> commentsInClassTwo = doc.select("p.two").comments();
        assertEquals(1, commentsInClassTwo.size());
        assertEquals(" comment3 ", commentsInClassTwo.get(0).getData());
    }

    @Test
    public void testGetTextNodes() {
        Document doc = Jsoup.parse("One<p>Two<a>Three</a><p>Four</p>Five");
        List<TextNode> textNodes = doc.select("p").textNodes();

        assertEquals(2, textNodes.size());
        assertEquals("Two", textNodes.get(0).text());
        assertEquals("Four", textNodes.get(1).text());
    }

    @Test
    public void testGetDataNodes() {
        Document doc = Jsoup.parse("<p>One</p><script>Two</script><style>Three</style>");
        List<DataNode> dataNodes = doc.select("p, script, style").dataNodes();

        assertEquals(2, dataNodes.size());
        assertEquals("Two", dataNodes.get(0).getWholeData());
        assertEquals("Three", dataNodes.get(1).getWholeData());

        doc = Jsoup.parse("<head><script type='application/json'><crux></script><script src='foo'>Blah</script>");
        Elements script = doc.select("script[type=application/json]");
        List<DataNode> scriptNode = script.dataNodes();

        assertEquals(1, scriptNode.size());
        DataNode dataNode = scriptNode.get(0);
        assertEquals("<crux>", dataNode.getWholeData());

        // Check if they're live
        dataNode.setWholeData("<cromulent>");
        assertEquals("<script type=\"application/json\"><cromulent></script>", script.outerHtml());
    }

    @Test
    public void testNodesEmpty() {
        Document doc = Jsoup.parse("<p>");
        assertEquals(0, doc.select("form").textNodes().size());
    }

    @Test
    public void testClassWithHyphen() {
        Document doc = Jsoup.parse("<p class='tab-nav'>Check</p>");
        Elements elements = doc.getElementsByClass("tab-nav");

        assertEquals(1, elements.size());
        assertEquals("Check", elements.text());
    }

    @Test
    public void testSiblings() {
        Document doc = Jsoup.parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12</div>");
        Elements selectedParagraphs = doc.select("p:eq(3)"); // Selects p4 and p10

        assertEquals(2, selectedParagraphs.size());

        Elements nextSiblings = selectedParagraphs.next();
        assertEquals(2, nextSiblings.size());
        assertEquals("5", nextSiblings.first().text());
        assertEquals("11", nextSiblings.last().text());

        assertEquals(0, selectedParagraphs.next("p:contains(6)").size());
        Elements nextFiltered = selectedParagraphs.next("p:contains(5)");
        assertEquals(1, nextFiltered.size());
        assertEquals("5", nextFiltered.first().text());

        Elements allNextSiblings = selectedParagraphs.nextAll();
        assertEquals(4, allNextSiblings.size());
        assertEquals("5", allNextSiblings.first().text());
        assertEquals("12", allNextSiblings.last().text());

        Elements allNextFiltered = selectedParagraphs.nextAll("p:contains(6)");
        assertEquals(1, allNextFiltered.size());
        assertEquals("6", allNextFiltered.first().text());

        Elements previousSiblings = selectedParagraphs.prev();
        assertEquals(2, previousSiblings.size());
        assertEquals("3", previousSiblings.first().text());
        assertEquals("9", previousSiblings.last().text());

        assertEquals(0, selectedParagraphs.prev("p:contains(1)").size());
        Elements previousFiltered = selectedParagraphs.prev("p:contains(3)");
        assertEquals(1, previousFiltered.size());
        assertEquals("3", previousFiltered.first().text());

        Elements allPreviousSiblings = selectedParagraphs.prevAll();
        assertEquals(6, allPreviousSiblings.size());
        assertEquals("3", allPreviousSiblings.first().text());
        assertEquals("7", allPreviousSiblings.last().text());

        Elements allPreviousFiltered = selectedParagraphs.prevAll("p:contains(1)");
        assertEquals(1, allPreviousFiltered.size());
        assertEquals("1", allPreviousFiltered.first().text());
    }

    @Test
    public void testEachText() {
        Document doc = Jsoup.parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12<p></p></div>");
        List<String> divText = doc.select("div").eachText();

        assertEquals(2, divText.size());
        assertEquals("1 2 3 4 5 6", divText.get(0));
        assertEquals("7 8 9 10 11 12", divText.get(1));

        List<String> paragraphText = doc.select("p").eachText();
        Elements paragraphs = doc.select("p");

        assertEquals(13, paragraphs.size());
        assertEquals(12, paragraphText.size()); // Last paragraph doesn't have text
        assertEquals("1", paragraphText.get(0));
        assertEquals("2", paragraphText.get(1));
        assertEquals("5", paragraphText.get(4));
        assertEquals("7", paragraphText.get(6));
        assertEquals("12", paragraphText.get(11));
    }

    @Test
    public void testEachAttribute() {
        Document doc = Jsoup.parse(
            "<div><a href='/foo'>1</a><a href='http://example.com/bar'>2</a><a href=''>3</a><a>4</a>",
            "http://example.com");

        List<String> hrefAttributes = doc.select("a").eachAttr("href");
        assertEquals(3, hrefAttributes.size());
        assertEquals("/foo", hrefAttributes.get(0));
        assertEquals("http://example.com/bar", hrefAttributes.get(1));
        assertEquals("", hrefAttributes.get(2));
        assertEquals(4, doc.select("a").size());

        List<String> absoluteAttributes = doc.select("a").eachAttr("abs:href");
        assertEquals(3, absoluteAttributes.size());
        assertEquals("http://example.com/foo", absoluteAttributes.get(0));
        assertEquals("http://example.com/bar", absoluteAttributes.get(1));
        assertEquals("http://example.com", absoluteAttributes.get(2));
    }

    @Test
    public void testSetElementByIndex() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");
        Element newParagraph = doc.createElement("p").text("New").attr("id", "new");

        Elements paragraphs = doc.select("p");
        Element oldParagraph = paragraphs.set(1, newParagraph);

        assertSame(oldParagraph, paragraphs.get(1));
        assertSame(newParagraph, paragraphs.get(1)); // Replaced in list
        assertEquals("<p>One</p>\n<p id=\"new\">New</p>\n<p>Three</p>", doc.body().html()); // Replaced in DOM
    }

    @Test
    public void testRemoveElementByIndex() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");

        Elements paragraphs = doc.select("p");
        Element removedParagraph = paragraphs.remove(1);

        assertEquals(2, paragraphs.size()); // Removed from list
        assertFalse(paragraphs.contains(removedParagraph));
        assertEquals("<p>One</p>\n<p>Three</p>", doc.body().html()); // Removed from DOM
    }

    @Test
    public void testRemoveElementByObject() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");

        Elements paragraphs = doc.select("p");
        Element paragraphToRemove = paragraphs.get(1);

        assertTrue(paragraphs.contains(paragraphToRemove));
        boolean removed = paragraphs.remove(paragraphToRemove);

        assertTrue(removed);
        assertEquals(2, paragraphs.size()); // Removed from list
        assertFalse(paragraphs.contains(paragraphToRemove));
        assertEquals("<p>One</p>\n<p>Three</p>", doc.body().html()); // Removed from DOM
    }

    @Test
    public void testRemoveElementObjectNoops() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");
        String originalHtml = doc.html();
        Element newParagraph = doc.createElement("p").text("New");

        Elements paragraphs = doc.select("p");
        int originalSize = paragraphs.size();

        assertFalse(paragraphs.remove(newParagraph));
        assertFalse(paragraphs.remove(newParagraph.childNodes()));
        assertEquals(originalHtml, doc.html());
        assertEquals(originalSize, paragraphs.size());
    }

    @Test
    public void testClearElements() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><div>Three</div>");
        Elements paragraphs = doc.select("p");

        assertEquals(2, paragraphs.size());
        paragraphs.clear();
        assertEquals(0, paragraphs.size());

        assertEquals(0, doc.select("p").size());
    }

    @Test
    public void testRemoveAllElements() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
        Elements paragraphs = doc.select("p");

        assertEquals(4, paragraphs.size());
        Elements midParagraphs = doc.select("p:gt(0):lt(3)"); // Two and Three

        assertEquals(2, midParagraphs.size());
        boolean removed = paragraphs.removeAll(midParagraphs);

        assertEquals(2, paragraphs.size());
        assertTrue(removed);
        assertEquals(2, midParagraphs.size());

        Elements divs = doc.select("div");
        assertEquals(1, divs.size());
        assertFalse(paragraphs.removeAll(divs));
        assertEquals(2, paragraphs.size());

        assertEquals("<p>One</p>\n<p>Four</p>\n<div>Div</div>", doc.body().html());
    }

    @Test
    public void testRetainAllElements() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
        Elements paragraphs = doc.select("p");

        assertEquals(4, paragraphs.size());
        Elements midParagraphs = doc.select("p:gt(0):lt(3)"); // Two and Three

        assertEquals(2, midParagraphs.size());
        boolean retained = paragraphs.retainAll(midParagraphs);

        assertEquals(2, paragraphs.size());
        assertTrue(retained);
        assertEquals(2, midParagraphs.size());

        assertEquals("<p>Two</p>\n<p>Three</p>\n<div>Div</div>", doc.body().html());

        Elements paragraphsAgain = doc.select("p");
        assertFalse(midParagraphs.retainAll(paragraphsAgain));

        assertEquals("<p>Two</p>\n<p>Three</p>\n<div>Div</div>", doc.body().html());
    }

    @Test
    public void testIteratorRemovesFromDom() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements paragraphs = doc.select("p");

        assertEquals(4, paragraphs.size());
        for (Iterator<Element> iterator = paragraphs.iterator(); iterator.hasNext(); ) {
            Element paragraph = iterator.next();
            if (paragraph.text().contains("Two")) {
                iterator.remove();
            }
        }

        assertEquals(3, paragraphs.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
    }

    @Test
    public void testRemoveIf() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements paragraphs = doc.select("p");

        assertEquals(4, paragraphs.size());
        boolean removed = paragraphs.removeIf(paragraph -> paragraph.text().contains("Two"));

        assertTrue(removed);
        assertEquals(3, paragraphs.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());

        assertFalse(paragraphs.removeIf(paragraph -> paragraph.text().contains("Five")));
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
    }

    @Test
    public void testRemoveIfSupportsConcurrentRead() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements paragraphs = doc.select("p");

        assertEquals(4, paragraphs.size());
        boolean removed = paragraphs.removeIf(paragraph -> paragraphs.contains(paragraph));

        assertTrue(removed);
        assertEquals(0, paragraphs.size());
        assertEquals("", doc.body().html());
    }

    @Test
    public void testReplaceAll() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements paragraphs = doc.select("p");

        assertEquals(4, paragraphs.size());
        paragraphs.replaceAll(paragraph -> {
            Element div = doc.createElement("div");
            div.text(paragraph.text());
            return div;
        });

        // Check Elements
        for (Element paragraph : paragraphs) {
            assertEquals("div", paragraph.tagName());
        }

        // Check DOM
        assertEquals("<div>One</div><div>Two</div><div>Three</div><div>Four</div>", TextUtil.normalizeSpaces(doc.body().html()));
    }

    @Test
    public void testSelectFirst() {
        Document doc = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");
        Element span = doc.children().selectFirst("span");

        assertNotNull(span);
        assertEquals("Jsoup", span.text());
    }

    @Test
    public void testSelectFirstNullOnNoMatch() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Element span = doc.children().selectFirst("span");

        assertNull(span);
    }

    @Test
    public void testExpectFirst() {
        Document doc = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");
        Element span = doc.children().expectFirst("span");

        assertNotNull(span);
        assertEquals("Jsoup", span.text());
    }

    @Test
    public void testExpectFirstThrowsOnNoMatch() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");

        boolean threwException = false;
        try {
            Element span = doc.children().expectFirst("span");
        } catch (IllegalArgumentException e) {
            threwException = true;
            assertEquals("No elements matched the query 'span' in the elements.", e.getMessage());
        }

        assertTrue(threwException);
    }

    @Test
    public void testSelectFirstFromPreviousSelect() {
        Document doc = Jsoup.parse("<div><p>One</p></div><div><p><span>Two</span></p></div><div><p><span>Three</span></p></div>");
        Elements divs = doc.select("div");

        assertEquals(3, divs.size());

        Element span = divs.selectFirst("p span");
        assertNotNull(span);
        assertEquals("Two", span.text());

        // Test roots
        assertNotNull(span.selectFirst("span")); // Reselect self
        assertNull(span.selectFirst(">span")); // No span>span

        assertNotNull(divs.selectFirst("div")); // Reselect self, similar to element.select
        assertNull(divs.selectFirst(">div")); // No div>div
    }
}