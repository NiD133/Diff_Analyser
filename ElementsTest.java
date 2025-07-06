package org.jsoup.select;

import org.jsoup.Jsoup;
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
 * Tests for ElementList (Elements class).  Focuses on testing the core functionality
 * of the Elements class, which provides methods for working with a list of Elements.
 * Covers filtering, attribute manipulation, text extraction, HTML manipulation, and DOM traversal.
 */
public class ElementsTest {

    @Test
    void filterElementsBasedOnSelector() {
        // Arrange
        String html = "<p>Excl</p><div class=headline><p>Hello</p><p>There</p></div><div class=headline><h1>Headline</h1></div>";
        Document doc = Jsoup.parse(html);

        // Act
        Elements headlinesParagraphs = doc.select(".headline").select("p");

        // Assert
        assertEquals(2, headlinesParagraphs.size(), "Should have 2 paragraphs inside headline divs.");
        assertEquals("Hello", headlinesParagraphs.get(0).text(), "First paragraph should contain 'Hello'.");
        assertEquals("There", headlinesParagraphs.get(1).text(), "Second paragraph should contain 'There'.");
    }

    @Test
    void manipulateElementAttributes() {
        // Arrange
        String html = "<p title=foo><p title=bar><p class=foo><p class=bar>";
        Document doc = Jsoup.parse(html);

        // Act
        Elements elementsWithTitle = doc.select("p[title]");

        // Assert
        assertEquals(2, elementsWithTitle.size(), "Should have 2 elements with title attribute.");
        assertTrue(elementsWithTitle.hasAttr("title"), "Should have title attribute.");
        assertFalse(elementsWithTitle.hasAttr("class"), "Should not have class attribute.");
        assertEquals("foo", elementsWithTitle.attr("title"), "Title attribute of first element should be 'foo'.");

        // Act
        elementsWithTitle.removeAttr("title");
        assertEquals(2, elementsWithTitle.size(), "Size should remain the same after removing attribute."); // Existing Elements are not reevaluated
        assertEquals(0, doc.select("p[title]").size(), "Should be no elements with title after removing.");

        // Act
        Elements allParagraphs = doc.select("p").attr("style", "classy");

        // Assert
        assertEquals(4, allParagraphs.size(), "Should have 4 paragraphs.");
        assertEquals("classy", allParagraphs.last().attr("style"), "Last element should have style 'classy'.");
        assertEquals("bar", allParagraphs.last().attr("class"), "Last element should still have its original class.");
    }

    @Test
    void checkElementAttributeExistence() {
        // Arrange
        Document doc = Jsoup.parse("<p title=foo><p title=bar><p class=foo><p class=bar>");
        Elements paragraphs = doc.select("p");

        // Assert
        assertTrue(paragraphs.hasAttr("class"), "Should have class attribute.");
        assertFalse(paragraphs.hasAttr("style"), "Should not have style attribute.");
    }

    @Test
    void checkAbsoluteAttributeExistence() {
        // Arrange
        Document doc = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");
        Elements elementOne = doc.select("#1");
        Elements elementTwo = doc.select("#2");
        Elements bothElements = doc.select("a");

        // Assert
        assertFalse(elementOne.hasAttr("abs:href"), "First element should not have absolute href.");
        assertTrue(elementTwo.hasAttr("abs:href"), "Second element should have absolute href.");
        assertTrue(bothElements.hasAttr("abs:href"), "Combined elements should have absolute href (due to #2).");
    }

    @Test
    void getAttributeValue() {
        // Arrange
        Document doc = Jsoup.parse("<p title=foo><p title=bar><p class=foo><p class=bar>");

        // Act
        String classValue = doc.select("p").attr("class");

        // Assert
        assertEquals("foo", classValue, "Should return the class of the first matched element.");
    }

    @Test
    void getAbsoluteAttributeValue() {
        // Arrange
        Document doc = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");
        Elements elementOne = doc.select("#1");
        Elements elementTwo = doc.select("#2");
        Elements bothElements = doc.select("a");

        // Assert
        assertEquals("", elementOne.attr("abs:href"), "First element should have empty absolute href.");
        assertEquals("https://jsoup.org", elementTwo.attr("abs:href"), "Second element should have absolute href.");
        assertEquals("https://jsoup.org", bothElements.attr("abs:href"), "Combined elements should have absolute href (due to #2).");
    }

    @Test
    void manipulateElementClasses() {
        // Arrange
        Document doc = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p>");
        Elements paragraphs = doc.select("p");

        // Assert
        assertTrue(paragraphs.hasClass("red"), "Should have class 'red'.");
        assertFalse(paragraphs.hasClass("blue"), "Should not have class 'blue'.");

        // Act
        paragraphs.addClass("blue");
        paragraphs.removeClass("yellow");
        paragraphs.toggleClass("mellow");

        // Assert
        assertEquals("blue", paragraphs.get(0).className(), "First element should have class 'blue'.");
        assertEquals("red green blue mellow", paragraphs.get(1).className(), "Second element should have updated classes.");
    }

    @Test
    void checkElementClassCaseInsensitive() {
        // Arrange
        Elements elements = Jsoup.parse("<p Class=One>One <p class=Two>Two <p CLASS=THREE>THREE").select("p");
        Element elementOne = elements.get(0);
        Element elementTwo = elements.get(1);
        Element elementThree = elements.get(2);

        // Assert
        assertTrue(elementOne.hasClass("One"), "Should have class 'One'.");
        assertTrue(elementOne.hasClass("ONE"), "Should have class 'ONE' (case-insensitive).");

        assertTrue(elementTwo.hasClass("TWO"), "Should have class 'TWO'.");
        assertTrue(elementTwo.hasClass("Two"), "Should have class 'Two' (case-insensitive).");

        assertTrue(elementThree.hasClass("ThreE"), "Should have class 'ThreE' (case-insensitive).");
        assertTrue(elementThree.hasClass("three"), "Should have class 'three' (case-insensitive).");
    }

    @Test
    void concatenateTextOfSelectedElements() {
        // Arrange
        String html = "<div><p>Hello<p>there<p>world</div>";
        Document doc = Jsoup.parse(html);

        // Act
        String combinedText = doc.select("div > *").text();

        // Assert
        assertEquals("Hello there world", combinedText, "Should combine text of all child elements.");
    }

    @Test
    void checkIfAnyElementHasText() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p></p></div>");

        // Assert
        assertTrue(doc.select("div").hasText(), "First div should have text.");
        assertFalse(doc.select("div + div").hasText(), "Second div should not have text.");
    }

    @Test
    void getCombinedInnerHtmlOfElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p>There</p></div>");

        // Act
        String combinedHtml = doc.select("div").html();

        // Assert
        assertEquals("<p>Hello</p>\n<p>There</p>", combinedHtml, "Should combine inner HTML of divs.");
    }

    @Test
    void getCombinedOuterHtmlOfElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p>There</p></div>");
        Elements divs = doc.select("div");

        // Act & Assert
        assertEquals("<div><p>Hello</p></div><div><p>There</p></div>", divs.outerHtml().replaceAll("\\n", "")); // Replace newline for cross-platform compatibility
    }

    @Test
    void setInnerHtmlOfElements() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements paragraphs = doc.select("p");

        // Act & Assert
        paragraphs.prepend("<b>Bold</b>").append("<i>Ital</i>");
        assertEquals("<p><b>Bold</b>Two<i>Ital</i></p>", paragraphs.get(1).outerHtml().replaceAll("\\n", ""));

        paragraphs.html("<span>Gone</span>");
        assertEquals("<p><span>Gone</span></p>", paragraphs.get(1).outerHtml().replaceAll("\\n", ""));
    }

    @Test
    void getAndSetFormElementValue() {
        // Arrange
        Document doc = Jsoup.parse("<input value='one' /><textarea>two</textarea>");
        Elements elements = doc.select("input, textarea");

        // Assert
        assertEquals(2, elements.size(), "Should have 2 elements.");
        assertEquals("one", elements.val(), "Value of first element should be 'one'.");
        assertEquals("two", elements.last().val(), "Value of last element should be 'two'.");

        // Act
        elements.val("three");

        // Assert
        assertEquals("three", elements.first().val(), "Value of first element should be 'three'.");
        assertEquals("three", elements.last().val(), "Value of last element should be 'three'.");
        assertEquals("<textarea>three</textarea>", elements.last().outerHtml(), "Outer HTML should be updated.");
    }

    @Test
    void insertHtmlBeforeElements() {
        // Arrange
        Document doc = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");

        // Act
        doc.select("a").before("<span>foo</span>");

        // Assert
        assertEquals("<p>This <span>foo</span><a>is</a> <span>foo</span><a>jsoup</a>.</p>", doc.body().html().replaceAll("\\n", ""));
    }

    @Test
    void insertHtmlAfterElements() {
        // Arrange
        Document doc = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");

        // Act
        doc.select("a").after("<span>foo</span>");

        // Assert
        assertEquals("<p>This <a>is</a><span>foo</span> <a>jsoup</a><span>foo</span>.</p>", doc.body().html().replaceAll("\\n", ""));
    }

    @Test
    void wrapElementsWithHtml() {
        // Arrange
        String html = "<p><b>This</b> is <b>jsoup</b></p>";
        Document doc = Jsoup.parse(html);

        // Act
        doc.select("b").wrap("<i></i>");

        // Assert
        assertEquals("<p><i><b>This</b></i> is <i><b>jsoup</b></i></p>", doc.body().html());
    }

    @Test
    void wrapElementsWithDiv() {
        // Arrange
        String html = "<p><b>This</b> is <b>jsoup</b>.</p> <p>How do you like it?</p>";
        Document doc = Jsoup.parse(html);

        // Act
        doc.select("p").wrap("<div></div>");

        // Assert
        assertEquals(
            "<div>\n <p><b>This</b> is <b>jsoup</b>.</p>\n</div>\n<div>\n <p>How do you like it?</p>\n</div>",
            doc.body().html());
    }

    @Test
    void unwrapElements() {
        // Arrange
        String html = "<div><font>One</font> <font><a href=\"/\">Two</a></font></div";
        Document doc = Jsoup.parse(html);

        // Act
        doc.select("font").unwrap();

        // Assert
        assertEquals("<div>\n" +
            " One <a href=\"/\">Two</a>\n" +
            "</div>", doc.body().html());
    }

    @Test
    void unwrapParagraphs() {
        // Arrange
        String html = "<p><a>One</a> Two</p> Three <i>Four</i> <p>Fix <i>Six</i></p>";
        Document doc = Jsoup.parse(html);

        // Act
        doc.select("p").unwrap();

        // Assert
        assertEquals("<a>One</a> Two Three <i>Four</i> Fix <i>Six</i>", doc.body().html().replaceAll("\\n", ""));
    }

    @Test
    void unwrapKeepsSpace() {
        String html = "<p>One <span>two</span> <span>three</span> four</p>";
        Document doc = Jsoup.parse(html);
        doc.select("span").unwrap();
        assertEquals("<p>One two three four</p>", doc.body().html());
    }

    @Test
    void emptyElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello <b>there</b></p> <p>now!</p></div>");
        doc.outputSettings().prettyPrint(false);

        // Act
        doc.select("p").empty();

        // Assert
        assertEquals("<div><p></p> <p></p></div>", doc.body().html());
    }

    @Test
    void removeElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello <b>there</b></p> jsoup <p>now!</p></div>");
        doc.outputSettings().prettyPrint(false);

        // Act
        doc.select("p").remove();

        // Assert
        assertEquals("<div> jsoup </div>", doc.body().html());
    }

    @Test
    void selectElementAtIndex() {
        // Arrange
        String html = "<p>Hello<p>there<p>world";
        Document doc = Jsoup.parse(html);

        // Assert
        assertEquals("there", doc.select("p").eq(1).text(), "Should select element at index 1 using eq().");
        assertEquals("there", doc.select("p").get(1).text(), "Should select element at index 1 using get().");
    }

    @Test
    void checkIfElementsMatchSelector() {
        // Arrange
        String html = "<p>Hello<p title=foo>there<p>world";
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");

        // Assert
        assertTrue(paragraphs.is("[title=foo]"), "Should match elements with title=foo.");
        assertFalse(paragraphs.is("[title=bar]"), "Should not match elements with title=bar.");
    }

    @Test
    void getParentsOfElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><p>There</p>");

        // Act
        Elements parents = doc.select("p").parents();

        // Assert
        assertEquals(3, parents.size(), "Should have 3 parent elements.");
        assertEquals("div", parents.get(0).tagName(), "First parent should be div.");
        assertEquals("body", parents.get(1).tagName(), "Second parent should be body.");
        assertEquals("html", parents.get(2).tagName(), "Third parent should be html.");
    }

    @Test
    void filterElementsThatDoNotMatchSelector() {
        // Arrange
        Document doc = Jsoup.parse("<div id=1><p>One</p></div> <div id=2><p><span>Two</span></p></div>");

        // Act
        Elements div1 = doc.select("div").not(":has(p > span)");
        Elements div2 = doc.select("div").not("#1");

        // Assert
        assertEquals(1, div1.size(), "Should have 1 element.");
        assertEquals("1", div1.first().id(), "First element should have id '1'.");

        assertEquals(1, div2.size(), "Should have 1 element.");
        assertEquals("2", div2.first().id(), "First element should have id '2'.");
    }

    @Test
    void setTagNameOfElements() {
        // Arrange
        Document doc = Jsoup.parse("<p>Hello <i>there</i> <i>now</i></p>");

        // Act
        doc.select("i").tagName("em");

        // Assert
        assertEquals("<p>Hello <em>there</em> <em>now</em></p>", doc.body().html());
    }

    @Test
    void traverseElements() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div>There</div>");
        final StringBuilder accum = new StringBuilder();

        // Act
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

        // Assert
        assertEquals("<div><p><#text></#text></p></div><div><#text></#text></div>", accum.toString());
    }

    @Test
    void getFormElements() {
        // Arrange
        Document doc = Jsoup.parse("<form id=1><input name=q></form><div /><form id=2><input name=f></form>");
        Elements elements = doc.select("form, div");

        // Act
        List<FormElement> forms = elements.forms();

        // Assert
        assertEquals(3, elements.size(), "Should have 3 elements.");
        assertEquals(2, forms.size(), "Should have 2 form elements.");
        assertNotNull(forms.get(0), "First form should not be null.");
        assertNotNull(forms.get(1), "Second form should not be null.");
        assertEquals("1", forms.get(0).id(), "First form should have id '1'.");
        assertEquals("2", forms.get(1).id(), "Second form should have id '2'.");
    }

    @Test
    void getCommentNodes() {
        // Arrange
        Document doc = Jsoup.parse("<!-- comment1 --><p><!-- comment2 --><p class=two><!-- comment3 -->");

        // Act
        List<Comment> comments = doc.select("p").comments();
        List<Comment> comments1 = doc.select("p.two").comments();

        // Assert
        assertEquals(2, comments.size(), "Should have 2 comment nodes.");
        assertEquals(" comment2 ", comments.get(0).getData(), "First comment should contain ' comment2 '.");
        assertEquals(" comment3 ", comments.get(1).getData(), "Second comment should contain ' comment3 '.");

        assertEquals(1, comments1.size(), "Should have 1 comment node.");
        assertEquals(" comment3 ", comments1.get(0).getData(), "First comment should contain ' comment3 '.");
    }

    @Test
    void getTextNodes() {
        // Arrange
        Document doc = Jsoup.parse("One<p>Two<a>Three</a><p>Four</p>Five");

        // Act
        List<TextNode> textNodes = doc.select("p").textNodes();

        // Assert
        assertEquals(2, textNodes.size(), "Should have 2 text nodes.");
        assertEquals("Two", textNodes.get(0).text(), "First text node should contain 'Two'.");
        assertEquals("Four", textNodes.get(1).text(), "Second text node should contain 'Four'.");
    }

    @Test
    void getDataNodes() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p><script>Two</script><style>Three</style>");

        // Act
        List<DataNode> dataNodes = doc.select("p, script, style").dataNodes();

        // Assert
        assertEquals(2, dataNodes.size(), "Should have 2 data nodes.");
        assertEquals("Two", dataNodes.get(0).getWholeData(), "First data node should contain 'Two'.");
        assertEquals("Three", dataNodes.get(1).getWholeData(), "Second data node should contain 'Three'.");

        doc = Jsoup.parse("<head><script type=application/json><crux></script><script src=foo>Blah</script>");
        Elements script = doc.select("script[type=application/json]");
        List<DataNode> scriptNode = script.dataNodes();
        assertEquals(1, scriptNode.size());
        DataNode dataNode = scriptNode.get(0);
        assertEquals("<crux>", dataNode.getWholeData());

        // check if they're live
        dataNode.setWholeData("<cromulent>");
        assertEquals("<script type=\"application/json\"><cromulent></script>", script.outerHtml());
    }

    @Test
    void textNodesEmpty() {
        Document doc = Jsoup.parse("<p>");
        assertEquals(0, doc.select("form").textNodes().size());
    }

    @Test
    void classWithHyphen() {
        Document doc = Jsoup.parse("<p class='tab-nav'>Check</p>");
        Elements els = doc.getElementsByClass("tab-nav");
        assertEquals(1, els.size());
        assertEquals("Check", els.text());
    }

    @Test
    void siblings() {
        Document doc = Jsoup.parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12</div>");

        Elements els = doc.select("p:eq(3)"); // gets p4 and p10
        assertEquals(2, els.size());

        Elements next = els.next();
        assertEquals(2, next.size());
        assertEquals("5", next.first().text());
        assertEquals("11", next.last().text());

        assertEquals(0, els.next("p:contains(6)").size());
        final Elements nextF = els.next("p:contains(5)");
        assertEquals(1, nextF.size());
        assertEquals("5", nextF.first().text());

        Elements nextA = els.nextAll();
        assertEquals(4, nextA.size());
        assertEquals("5", nextA.first().text());
        assertEquals("12", nextA.last().text());

        Elements nextAF = els.nextAll("p:contains(6)");
        assertEquals(1, nextAF.size());
        assertEquals("6", nextAF.first().text());

        Elements prev = els.prev();
        assertEquals(2, prev.size());
        assertEquals("3", prev.first().text());
        assertEquals("9", prev.last().text());

        assertEquals(0, els.prev("p:contains(1)").size());
        final Elements prevF = els.prev("p:contains(3)");
        assertEquals(1, prevF.size());
        assertEquals("3", prevF.first().text());

        Elements prevA = els.prevAll();
        assertEquals(6, prevA.size());
        assertEquals("3", prevA.first().text());
        assertEquals("7", prevA.last().text());

        Elements prevAF = els.prevAll("p:contains(1)");
        assertEquals(1, prevAF.size());
        assertEquals("1", prevAF.first().text());
    }

    @Test
    void eachText() {
        Document doc = Jsoup.parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12<p></p></div>");
        List<String> divText = doc.select("div").eachText();
        assertEquals(2, divText.size());
        assertEquals("1 2 3 4 5 6", divText.get(0));
        assertEquals("7 8 9 10 11 12", divText.get(1));

        List<String> pText = doc.select("p").eachText();
        Elements ps = doc.select("p");
        assertEquals(13, ps.size());
        assertEquals(12, pText.size()); // not 13, as last doesn't have text
        assertEquals("1", pText.get(0));
        assertEquals("2", pText.get(1));
        assertEquals("5", pText.get(4));
        assertEquals("7", pText.get(6));
        assertEquals("12", pText.get(11));
    }

    @Test
    void eachAttr() {
        Document doc = Jsoup.parse(
            "<div><a href='/foo'>1</a><a href='http://example.com/bar'>2</a><a href=''>3</a><a>4</a>",
            "http://example.com");

        List<String> hrefAttrs = doc.select("a").eachAttr("href");
        assertEquals(3, hrefAttrs.size());
        assertEquals("/foo", hrefAttrs.get(0));
        assertEquals("http://example.com/bar", hrefAttrs.get(1));
        assertEquals("", hrefAttrs.get(2));
        assertEquals(4, doc.select("a").size());

        List<String> absAttrs = doc.select("a").eachAttr("abs:href");
        assertEquals(3, absAttrs.size());
        assertEquals(3, absAttrs.size());
        assertEquals("http://example.com/foo", absAttrs.get(0));
        assertEquals("http://example.com/bar", absAttrs.get(1));
        assertEquals("http://example.com", absAttrs.get(2));
    }

    @Test
    void setElementByIndex() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");
        Element newP = doc.createElement("p").text("New").attr("id", "new");

        Elements ps = doc.select("p");
        Element two = ps.get(1);
        Element old = ps.set(1, newP);
        assertSame(old, two);
        assertSame(newP, ps.get(1)); // replaced in list
        assertEquals("<p>One</p>\n<p id=\"new\">New</p>\n<p>Three</p>", doc.body().html()); // replaced in dom
    }

    @Test
    void removeElementByIndex() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");

        Elements ps = doc.select("p");
        Element two = ps.get(1);
        assertTrue(ps.contains(two));
        Element old = ps.remove(1);
        assertSame(old, two);

        assertEquals(2, ps.size()); // removed from list
        assertFalse(ps.contains(old));
        assertEquals("<p>One</p>\n<p>Three</p>", doc.body().html()); // removed from dom
    }

    @Test
    void removeElementByObject() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");

        Elements ps = doc.select("p");
        Element two = ps.get(1);
        assertTrue(ps.contains(two));
        boolean removed = ps.remove(two);
        assertTrue(removed);

        assertEquals(2, ps.size()); // removed from list
        assertFalse(ps.contains(two));
        assertEquals("<p>One</p>\n<p>Three</p>", doc.body().html()); // removed from dom
    }

    @Test
    void removeElementObjectNoops() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three");
        String origHtml = doc.html();
        Element newP = doc.createElement("p").text("New");

        Elements ps = doc.select("p");
        int size = ps.size();
        assertFalse(ps.remove(newP));
        assertFalse(ps.remove(newP.childNodes()));
        assertEquals(origHtml, doc.html());
        assertEquals(size, ps.size());
    }

    @Test
    void clear() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><div>Three</div>");
        Elements ps = doc.select("p");
        assertEquals(2, ps.size());
        ps.clear();
        assertEquals(0, ps.size());

        assertEquals(0, doc.select("p").size());
    }

    @Test
    void removeAll() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());
        Elements midPs = doc.select("p:gt(0):lt(3)"); //Two and Three
        assertEquals(2, midPs.size());

        boolean removed = ps.removeAll(midPs);
        assertEquals(2, ps.size());
        assertTrue(removed);
        assertEquals(2, midPs.size());

        Elements divs = doc.select("div");
        assertEquals(1, divs.size());
        assertFalse(ps.removeAll(divs));
        assertEquals(2, ps.size());

        assertEquals("<p>One</p>\n<p>Four</p>\n<div>Div</div>", doc.body().html());
    }

    @Test
    void retainAll() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());
        Elements midPs = doc.select("p:gt(0):lt(3)"); //Two and Three
        assertEquals(2, midPs.size());

        boolean removed = ps.retainAll(midPs);
        assertEquals(2, ps.size());
        assertTrue(removed);
        assertEquals(2, midPs.size());

        assertEquals("<p>Two</p>\n<p>Three</p>\n<div>Div</div>", doc.body().html());

        Elements psAgain = doc.select("p");
        assertFalse(midPs.retainAll(psAgain));

        assertEquals("<p>Two</p>\n<p>Three</p>\n<div>Div</div>", doc.body().html());
    }

    @Test
    void iteratorRemovesFromDom() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements ps = doc.select("p");

        assertEquals(4, ps.size());
        for (Iterator<Element> it = ps.iterator(); it.hasNext(); ) {
            Element el = it.next();
            if (el.text().contains("Two"))
                it.remove();
        }
        assertEquals(3, ps.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
    }

    @Test
    void removeIf() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements ps = doc.select("p");

        assertEquals(4, ps.size());
        boolean removed = ps.removeIf(el -> el.text().contains("Two"));
        assertTrue(removed);
        assertEquals(3, ps.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());

        assertFalse(ps.removeIf(el -> el.text().contains("Five")));
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", doc.body().html());
    }

    @Test
    void removeIfSupportsConcurrentRead() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());

        boolean removed = ps.removeIf(el -> ps.contains(el));
        assertTrue(removed);
        assertEquals(0, ps.size());
        assertEquals("", doc.body().html());
    }

    @Test
    void replaceAll() {
        Document doc = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements ps = doc.select("p");
        assertEquals(4, ps.size());

        ps.replaceAll(el -> {
            Element div = doc.createElement("div");
            div.text(el.text());
            return div;
        });

        // Check Elements
        for (Element p : ps) {
            assertEquals("div", p.tagName());
        }

        // check dom
        assertEquals("<div>One</div><div>Two</div><div>Three</div><div>Four</div>", TextUtil.normalizeSpaces(doc.body().html()));
    }

    @Test
    void selectFirst() {
        Document doc = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");
        Element span = doc.children().selectFirst("span");
        assertNotNull(span);
        assertEquals("Jsoup", span.text());
    }

    @Test
    void selectFirstNullOnNoMatch() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Element span = doc.children().selectFirst("span");
        assertNull(span);
    }

    @Test
    void expectFirst() {
        Document doc = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");
        Element span = doc.children().expectFirst("span");
        assertNotNull(span);
        assertEquals("Jsoup", span.text());
    }

    @Test
    void expectFirstThrowsOnNoMatch() {
        Document doc = Jsoup.parse("<p>One</p><p