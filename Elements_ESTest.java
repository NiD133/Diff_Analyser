package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Readable and maintainable tests for org.jsoup.select.Elements.
 * - Clear Arrange / Act / Assert structure
 * - Avoids magic values and random input
 * - Focuses on common behavior and key edge cases
 */
public class ElementsReadableTest {

    private Document doc(String html) {
        return Jsoup.parse(html);
    }

    // ------------------------------
    // Attribute APIs
    // ------------------------------

    @Test
    public void attr_hasAttr_eachAttr_and_removeAttr_work() {
        Document doc = doc("<div id='a' data-x='1'></div><div id='b'></div>");
        Elements divs = doc.select("div");

        assertTrue(divs.hasAttr("data-x"));
        assertEquals("1", divs.attr("data-x"));
        assertEquals("", divs.attr("missing"));

        List<String> values = divs.eachAttr("data-x");
        assertEquals(1, values.size());
        assertEquals("1", values.get(0));

        divs.removeAttr("data-x");
        assertFalse(divs.hasAttr("data-x"));
        assertEquals("", divs.attr("data-x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void attr_throwsOnNullKey() {
        Document doc = doc("<p>t</p>");
        doc.select("p").attr(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeAttr_throwsOnNullKey() {
        Document doc = doc("<p t='x'>t</p>");
        doc.select("p").removeAttr(null);
    }

    @Test
    public void setAttribute_onAllMatchedElements() {
        Document doc = doc("<p></p><p></p>");
        Elements ps = doc.select("p");

        ps.attr("role", "note");

        assertEquals("note", ps.first().attr("role"));
        assertEquals("note", ps.last().attr("role"));
    }

    // ------------------------------
    // Class APIs
    // ------------------------------

    @Test
    public void add_remove_toggle_and_hasClass() {
        Document doc = doc("<div></div><div class='foo'></div>");
        Elements divs = doc.select("div");

        // add
        divs.addClass("foo");
        assertTrue(divs.hasClass("foo"));

        // remove
        divs.removeClass("foo");
        assertFalse(divs.hasClass("foo"));

        // toggle (adds)
        divs.toggleClass("bar");
        assertTrue(divs.hasClass("bar"));

        // toggle (removes)
        divs.toggleClass("bar");
        assertFalse(divs.hasClass("bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addClass_throwsOnNull() {
        doc("<p></p>").select("p").addClass(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeClass_throwsOnNull() {
        doc("<p></p>").select("p").removeClass(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toggleClass_throwsOnNull() {
        doc("<p></p>").select("p").toggleClass(null);
    }

    // ------------------------------
    // Form value APIs
    // ------------------------------

    @Test
    public void val_getsFirstMatchedElementsValue() {
        Document doc = doc("<input value='x'><textarea>y</textarea>");
        Elements fields = doc.select("input, textarea");
        assertEquals("x", fields.val());
    }

    @Test
    public void val_setsAllMatchedElementsValue() {
        Document doc = doc("<input><textarea></textarea>");
        Elements fields = doc.select("input, textarea");

        fields.val("z");

        assertEquals("z", fields.first().val());
        assertEquals("z", fields.last().val());
    }

    // ------------------------------
    // Text APIs
    // ------------------------------

    @Test
    public void text_eachText_and_hasText() {
        Document doc = doc("<p>One</p><p><b>Two</b></p><p></p>");
        Elements ps = doc.select("p");

        assertEquals("One Two", ps.text());
        assertTrue(ps.hasText());

        List<String> texts = ps.eachText();
        assertEquals(2, texts.size());
        assertTrue(texts.contains("One"));
        assertTrue(texts.contains("Two"));
    }

    // ------------------------------
    // HTML APIs
    // ------------------------------

    @Test
    public void html_getsCombinedInnerHtml() {
        Document doc = doc("<div><b>One</b></div><div>Two</div>");
        Elements divs = doc.select("div");
        assertEquals("<b>One</b>Two", divs.html());
    }

    @Test
    public void html_setsInnerHtmlOnEachElement() {
        Document doc = doc("<div></div><div></div>");
        Elements divs = doc.select("div");

        divs.html("<span>Ok</span>");

        assertEquals("<span>Ok</span>", divs.first().html());
        assertEquals("<span>Ok</span>", divs.last().html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void html_set_throwsOnNull() {
        doc("<div></div>").select("div").html(null);
    }

    @Test
    public void append_and_prepend() {
        Document doc = doc("<p>Base</p>");
        Elements p = doc.select("p");

        p.prepend("<i>A</i>");
        p.append("<b>Z</b>");

        assertEquals("<i>A</i>Base<b>Z</b>", p.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void append_throwsOnNull() {
        doc("<p>t</p>").select("p").append(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void prepend_throwsOnNull() {
        doc("<p>t</p>").select("p").prepend(null);
    }

    @Test
    public void before_and_after_insertAroundEachElement() {
        Document doc = doc("<ul><li>1</li><li>2</li></ul>");
        Elements lis = doc.select("li");

        lis.before("<span class='pre'/>").after("<span class='post'/>");

        assertEquals(2, doc.select("span.pre").size());
        assertEquals(2, doc.select("span.post").size());
    }

    // ------------------------------
    // Tag name updates
    // ------------------------------

    @Test
    public void tagName_renamesEachElement() {
        Document doc = doc("<i>One</i><i>Two</i>");
        Elements italics = doc.select("i");

        italics.tagName("em");

        assertTrue(doc.select("i").isEmpty());
        assertEquals(2, doc.select("em").size());
    }

    // ------------------------------
    // Wrap / Unwrap / Empty / Remove
    // ------------------------------

    @Test
    public void wrap_wrapsEachElement() {
        Document doc = doc("<p><b>This</b> and <b>That</b></p>");
        Elements bolds = doc.select("b");

        bolds.wrap("<i></i>");

        assertEquals(2, doc.select("p > i > b").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrap_throwsOnNull() {
        doc("<p><b>t</b></p>").select("b").wrap(null);
    }

    @Test
    public void unwrap_dropsElementsKeepsChildren() {
        Document doc = doc("<p><span>t</span> and <span>u</span></p>");
        Elements spans = doc.select("span");

        spans.unwrap();

        assertTrue(doc.select("span").isEmpty());
        assertEquals("t and u", doc.select("p").text());
    }

    @Test
    public void empty_removesAllChildren() {
        Document doc = doc("<p>One <b>Two</b></p>");
        Elements ps = doc.select("p");

        ps.empty();

        assertEquals("", ps.first().html());
        assertEquals(1, ps.size());
    }

    @Test
    public void remove_removesElementsFromDomButKeepsList() {
        Document doc = doc("<div id='a'></div><div id='b'></div>");
        Elements divs = doc.select("div");

        Elements returned = divs.remove();

        assertSame(divs, returned);
        assertEquals(2, divs.size());       // list retains elements
        assertTrue(doc.select("div").isEmpty()); // but DOM has removed them
    }

    // ------------------------------
    // Selecting APIs
    // ------------------------------

    @Test
    public void select_selectFirst_expectFirst_not_and_is() {
        Document doc = doc("<div id='a'><span>one</span></div><div id='b'></div>");
        Elements divs = doc.select("div");

        // select
        Elements withSpan = divs.select(":has(span)");
        assertEquals(1, withSpan.size());
        assertEquals("a", withSpan.first().id());

        // selectFirst
        Element firstB = divs.selectFirst("#b");
        assertNotNull(firstB);
        assertEquals("b", firstB.id());

        // not
        Elements notB = divs.not("#b");
        assertEquals(1, notB.size());
        assertEquals("a", notB.first().id());

        // is
        assertFalse(divs.is(".missing"));
        divs.addClass("x");
        assertTrue(divs.is(".x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void select_throwsOnEmptyQuery() {
        doc("<p></p>").select("p").select("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectFirst_throwsOnEmptyQuery() {
        doc("<p></p>").select("p").selectFirst("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void expectFirst_throwsIfNoMatch() {
        doc("<p></p>").select("p").expectFirst("span");
    }

    // ------------------------------
    // eq and list-like navigation
    // ------------------------------

    @Test
    public void eq_returnsSingleElementOrEmpty() {
        Document doc = doc("<p>A</p><p>B</p>");
        Elements ps = doc.select("p");

        Elements first = ps.eq(0);
        Elements second = ps.eq(1);
        Elements outOfRange = ps.eq(2);

        assertEquals(1, first.size());
        assertEquals("A", first.first().text());
        assertEquals(1, second.size());
        assertEquals("B", second.first().text());
        assertTrue(outOfRange.isEmpty());
    }

    // ------------------------------
    // Sibling navigation
    // ------------------------------

    @Test
    public void next_prev_nextAll_prevAll() {
        Document doc = doc("<ul><li>1</li><li>2</li><li>3</li></ul>");
        Elements lis = doc.select("li");

        Elements next = lis.next();
        assertEquals(2, next.size());
        assertEquals("2", next.get(0).text());
        assertEquals("3", next.get(1).text());

        Elements prev = lis.prev();
        assertEquals(2, prev.size());
        assertEquals("1", prev.get(0).text());
        assertEquals("2", prev.get(1).text());

        Elements nextAll = lis.nextAll();
        assertEquals(2, nextAll.size());
        assertEquals("2", nextAll.get(0).text());
        assertEquals("3", nextAll.get(1).text());

        Elements prevAll = lis.prevAll();
        assertEquals(2, prevAll.size());
        assertEquals("1", prevAll.get(0).text());
        assertEquals("2", prevAll.get(1).text());
    }

    @Test
    public void nextAll_and_prevAll_withQuery() {
        Document doc = doc("<ul><li>1</li><li class='x'>2</li><li>3</li></ul>");
        Elements lis = doc.select("li");

        Elements onlyClassX = lis.nextAll(".x");
        assertEquals(1, onlyClassX.size());
        assertEquals("2", onlyClassX.first().text());

        Elements prevOnlyClassX = lis.prevAll(".x");
        assertEquals(1, prevOnlyClassX.size());
        assertEquals("2", prevOnlyClassX.get(0).text());
    }

    // ------------------------------
    // parents / first / last
    // ------------------------------

    @Test
    public void parents_first_last() {
        Document doc = doc("<div><p id='p'>t</p></div>");
        Element p = doc.selectFirst("#p");

        Elements parents = p.parents();
        assertFalse(parents.isEmpty());
        assertEquals("div", parents.first().tagName());
        assertEquals("html", parents.last().tagName());

        Elements onlyP = doc.select("p");
        assertSame(p, onlyP.first());
        assertSame(p, onlyP.last());
    }

    // ------------------------------
    // Node traversal and filtering
    // ------------------------------

    @Test
    public void traverse_visitsNodes() {
        Document doc = doc("<div><span>t</span></div>");
        Elements roots = doc.select("div");

        final int[] count = {0};
        Elements ret = roots.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                count[0]++;
            }
            @Override
            public void tail(Node node, int depth) { }
        });

        assertSame(roots, ret);
        assertTrue("Expected traversal to visit at least 2 nodes", count[0] >= 2);
    }

    @Test
    public void filter_runsOnNodes() {
        Document doc = doc("<div><span>t</span></div>");
        Elements roots = doc.select("div");

        Elements ret = roots.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                return FilterResult.CONTINUE;
            }
            @Override
            public FilterResult tail(Node node, int depth) {
                return FilterResult.CONTINUE;
            }
        });

        assertSame(roots, ret);
    }

    // ------------------------------
    // forms / comments / textNodes / dataNodes
    // ------------------------------

    @Test
    public void forms_comments_textNodes_dataNodes() {
        Document doc = doc(
            "<form id='f'><input name='n'/></form>" +
            "<div><!--c--></div>" +
            "<div>hello<span>child</span></div>" +
            "<script>var a=1;</script>"
        );

        // forms
        List<FormElement> forms = doc.select("form").forms();
        assertEquals(1, forms.size());
        assertEquals("f", forms.get(0).id());

        // comments
        List<Comment> comments = doc.select("div").comments();
        assertEquals(1, comments.size());
        assertEquals("c", comments.get(0).getData().trim());

        // text nodes (direct children only)
        List<TextNode> textNodes = doc.select("div").textNodes();
        assertEquals(1, textNodes.size());
        assertEquals("hello", textNodes.get(0).text());

        // data nodes
        List<DataNode> dataNodes = doc.select("script").dataNodes();
        assertEquals(1, dataNodes.size());
        assertTrue(dataNodes.get(0).getWholeData().contains("var a=1"));
    }

    // ------------------------------
    // set / remove(index) / deselect(index)
    // ------------------------------

    @Test
    public void remove_removesFromDomAndReturnsOldElement() {
        Document doc = doc("<ul><li id='a'>A</li><li id='b'>B</li></ul>");
        Elements lis = doc.select("li");

        Element removed = lis.remove(0);

        assertEquals("a", removed.id());
        assertEquals(1, doc.select("li").size());
        assertEquals("b", doc.select("li").first().id());
    }

    @Test
    public void deselect_removesFromListButNotDom() {
        Document doc = doc("<ul><li id='a'>A</li><li id='b'>B</li></ul>");
        Elements lis = doc.select("li");

        Element old = lis.deselect(0);

        assertEquals("a", old.id());
        assertEquals(1, lis.size());
        assertEquals(2, doc.select("li").size()); // DOM unchanged
    }

    @Test
    public void set_replacesElementInDomAndList() {
        Document doc = doc("<ul><li id='a'>A</li><li id='b'>B</li></ul>");
        Elements lis = doc.select("li");
        Element replacement = new Element("li").attr("id", "c").text("C");

        Element old = lis.set(1, replacement);

        assertEquals("b", old.id());
        assertEquals(2, doc.select("li").size());
        assertNotNull(doc.selectFirst("#c"));
        assertNull(doc.selectFirst("#b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void set_throwsOnNullElement() {
        Document doc = doc("<p></p>");
        doc.select("p").set(0, null);
    }

    // ------------------------------
    // asList and clone
    // ------------------------------

    @Test
    public void asList_isIndependentFromDomAndElements() {
        Document doc = doc("<p>1</p><p>2</p>");
        Elements ps = doc.select("p");

        ArrayList<Element> list = ps.asList();
        list.remove(0);

        // Elements and DOM unaffected
        assertEquals(2, ps.size());
        assertEquals(2, doc.select("p").size());
    }

    @Test
    public void clone_makesDeepCopy() {
        Document doc = doc("<p id='a'>1</p><p id='b'>2</p>");
        Elements ps = doc.select("p");

        Elements copy = ps.clone();

        assertEquals(ps.size(), copy.size());
        assertNotSame(ps.first(), copy.first());
        assertEquals(ps.first().id(), copy.first().id());
    }

    // ------------------------------
    // is(query)
    // ------------------------------

    @Test
    public void is_queryMatchesAnyElement() {
        Document doc = doc("<p class='x'>1</p><p>2</p>");
        Elements ps = doc.select("p");

        assertTrue(ps.is("p"));
        assertTrue(ps.is(".x"));
        assertFalse(ps.is(".missing"));
    }
}