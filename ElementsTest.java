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
 * Tests for ElementList.
 * 
 * This test suite verifies the functionality of the Elements class, which provides
 * a list of Element objects with methods that act on every element in the list.
 * 
 * Author: Jonathan Hedley, jonathan@hedley.net
 */
public class ElementsTest {

    @Test
    public void testFilterByClassAndTag() {
        String html = "<p>Excl</p><div class='headline'><p>Hello</p><p>There</p></div><div class='headline'><h1>Headline</h1></div>";
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(".headline").select("p");

        assertEquals(2, elements.size());
        assertEquals("Hello", elements.get(0).text());
        assertEquals("There", elements.get(1).text());
    }

    @Test
    public void testAttributesManipulation() {
        String html = "<p title='foo'></p><p title='bar'></p><p class='foo'></p><p class='bar'></p>";
        Document doc = Jsoup.parse(html);
        Elements withTitle = doc.select("p[title]");

        assertEquals(2, withTitle.size());
        assertTrue(withTitle.hasAttr("title"));
        assertFalse(withTitle.hasAttr("class"));
        assertEquals("foo", withTitle.attr("title"));

        withTitle.removeAttr("title");
        assertEquals(2, withTitle.size()); // existing Elements are not reevaluated
        assertEquals(0, doc.select("p[title]").size());

        Elements ps = doc.select("p").attr("style", "classy");
        assertEquals(4, ps.size());
        assertEquals("classy", ps.last().attr("style"));
        assertEquals("bar", ps.last().attr("class"));
    }

    @Test
    public void testHasAttribute() {
        Document doc = Jsoup.parse("<p title='foo'></p><p title='bar'></p><p class='foo'></p><p class='bar'></p>");
        Elements ps = doc.select("p");

        assertTrue(ps.hasAttr("class"));
        assertFalse(ps.hasAttr("style"));
    }

    @Test
    public void testHasAbsoluteAttribute() {
        Document doc = Jsoup.parse("<a id='1' href='/foo'>One</a> <a id='2' href='https://jsoup.org'>Two</a>");
        Elements one = doc.select("#1");
        Elements two = doc.select("#2");
        Elements both = doc.select("a");

        assertFalse(one.hasAttr("abs:href"));
        assertTrue(two.hasAttr("abs:href"));
        assertTrue(both.hasAttr("abs:href")); // hits on #2
    }

    @Test
    public void testGetAttribute() {
        Document doc = Jsoup.parse("<p title='foo'></p><p title='bar'></p><p class='foo'></p><p class='bar'></p>");
        String classVal = doc.select("p").attr("class");

        assertEquals("foo", classVal);
    }

    @Test
    public void testGetAbsoluteAttribute() {
        Document doc = Jsoup.parse("<a id='1' href='/foo'>One</a> <a id='2' href='https://jsoup.org'>Two</a>");
        Elements one = doc.select("#1");
        Elements two = doc.select("#2");
        Elements both = doc.select("a");

        assertEquals("", one.attr("abs:href"));
        assertEquals("https://jsoup.org", two.attr("abs:href"));
        assertEquals("https://jsoup.org", both.attr("abs:href"));
    }

    @Test
    public void testClassManipulation() {
        Document doc = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p></div>");
        Elements els = doc.select("p");

        assertTrue(els.hasClass("red"));
        assertFalse(els.hasClass("blue"));
        els.addClass("blue");
        els.removeClass("yellow");
        els.toggleClass("mellow");

        assertEquals("blue", els.get(0).className());
        assertEquals("red green blue mellow", els.get(1).className());
    }

    @Test
    public void testHasClassCaseInsensitive() {
        Elements els = Jsoup.parse("<p Class='One'>One <p class='Two'>Two <p CLASS='THREE'>THREE").select("p");
        Element one = els.get(0);
        Element two = els.get(1);
        Element thr = els.get(2);

        assertTrue(one.hasClass("One"));
        assertTrue(one.hasClass("ONE"));

        assertTrue(two.hasClass("TWO"));
        assertTrue(two.hasClass("Two"));

        assertTrue(thr.hasClass("ThreE"));
        assertTrue(thr.hasClass("three"));
    }

    @Test
    public void testGetText() {
        String html = "<div><p>Hello</p><p>there</p><p>world</p></div>";
        Document doc = Jsoup.parse(html);

        assertEquals("Hello there world", doc.select("div > *").text());
    }

    @Test
    public void testHasText() {
        Document doc = Jsoup.parse("<div><p>Hello</p></div><div><p></p></div>");
        Elements divs = doc.select("div");

        assertTrue(divs.hasText());
        assertFalse(doc.select("div + div").hasText());
    }

    @Test
    public void testGetHtml() {
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
    public void testSetHtml() {
        Document doc = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements ps = doc.select("p");

        ps.prepend("<b>Bold</b>").append("<i>Ital</i>");
        assertEquals("<p><b>Bold</b>Two<i>Ital</i></p>", TextUtil.stripNewlines(ps.get(1).outerHtml()));

        ps.html("<span>Gone</span>");
        assertEquals("<p><span>Gone</span></p>", TextUtil.stripNewlines(ps.get(1).outerHtml()));
    }

    @Test
    public void testGetAndSetValue() {
        Document doc = Jsoup.parse("<input value='one' /><textarea>two</textarea>");
        Elements els = doc.select("input, textarea");

        assertEquals(2, els.size());
        assertEquals("one", els.val());
        assertEquals("two", els.last().val());

        els.val("three");
        assertEquals("three", els.first().val());
        assertEquals("three", els.last().val());
        assertEquals("<textarea>three</textarea>", els.last().outerHtml());
    }

    @Test
    public void testInsertBefore() {
        Document doc = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");
        doc.select("a").before("<span>foo</span>");

        assertEquals("<p>This <span>foo</span><a>is</a> <span>foo</span><a>jsoup</a>.</p>", TextUtil.stripNewlines(doc.body().html()));
    }

    @Test
    public void testInsertAfter() {
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
    public void testWrapDivElements() {
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
    public void testSelectByIndex() {
        String html = "<p>Hello<p>there<p>world";
        Document doc = Jsoup.parse(html);

        assertEquals("there", doc.select("p").eq(1).text());
        assertEquals("there", doc.select("p").get(1).text());
    }

    @Test
    public void testIsElement() {
        String html = "<p>Hello<p title='foo'>there<p>world";
        Document doc = Jsoup.parse(html);
        Elements ps = doc.select("p");

        assertTrue(ps.is("[title=foo]"));
        assertFalse(ps.is("[title=bar]"));
    }

    @Test
    public void testGetParents() {
        Document doc = Jsoup.parse("<div><p>Hello</p></div><p>There</p>");
        Elements parents = doc.select("p").parents();

        assertEquals(3, parents.size());
        assertEquals("div", parents.get(0).tagName());
        assertEquals("body", parents.get(1).tagName());
        assertEquals("html", parents.get(2).tagName());
    }

    @Test
    public void testNotSelector() {
        Document doc = Jsoup.parse("<div id='1'><p>One</p></div> <div id='2'><p><span>Two</span></p></div>");
        Elements div1 = doc.select("div").not(":has(p > span)");

        assertEquals(1, div1.size());
        assertEquals("1", div1.first().id());

        Elements div2 = doc.select("div").not("#1");
        assertEquals(1, div2.size());
        assertEquals("2", div2.first().id());
    }

    @Test
    public void testSetTagName() {
        Document doc = Jsoup.parse("<p>Hello <i>there</i> <i>now</i></p>");
        doc.select("i").tagName("em");

        assertEquals("<p>Hello <em>there</em> <em>now</em></p>", doc.body().html());
    }

    @Test
    public void testTraverseNodes() {
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
        Elements els = doc.select("form, div");

        assertEquals(3, els.size());

        List<FormElement> forms = els.forms();
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

        List<Comment> comments1 = doc.select("p.two").comments();
        assertEquals(1, comments1.size());
        assertEquals(" comment3 ", comments1.get(0).getData());
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

        // check if they're live
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
        Elements els = doc.getElementsByClass("tab-nav");

        assertEquals(1, els.size());
        assertEquals("Check", els.text());
    }

    @Test
    public void testSiblings() {
        Document doc = Jsoup.parse("<div><