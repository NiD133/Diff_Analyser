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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 Tests for Elements.
 
 Goals:
 - Group related tests with @Nested to aid navigation.
 - Use descriptive test names and Arrange / Act / Assert comments.
 - Factor small helpers for repeated operations (HTML normalization, parsing, assertions).
 */
public class ElementsTest {

    // ----------------------------
    // Helpers for readability
    // ----------------------------

    private static Document parse(String html) {
        return Jsoup.parse(html);
    }

    private static Document parse(String html, String baseUri) {
        return Jsoup.parse(html, baseUri);
    }

    private static String stripNl(String html) {
        return TextUtil.stripNewlines(html);
    }

    private static void assertBodyHtml(Document doc, String expectedHtml) {
        assertEquals(expectedHtml, doc.body().html());
    }

    private static void assertBodyHtmlNoNl(Document doc, String expectedHtml) {
        assertEquals(expectedHtml, stripNl(doc.body().html()));
    }

    private static void assertBodyHtmlNormalizedSpaces(Document doc, String expectedHtmlNormalized) {
        assertEquals(expectedHtmlNormalized, TextUtil.normalizeSpaces(doc.body().html()));
    }

    // ----------------------------
    // Selection and filtering
    // ----------------------------

    @Nested
    class SelectionAndFiltering {
        @Test
        void filter_selectingNestedElements_byClassThenTag() {
            // Arrange
            Document doc = parse("<p>Excl</p><div class=headline><p>Hello</p><p>There</p></div><div class=headline><h1>Headline</h1></div>");

            // Act
            Elements psInHeadlines = doc.select(".headline").select("p");

            // Assert
            assertEquals(2, psInHeadlines.size());
            assertEquals("Hello", psInHeadlines.get(0).text());
            assertEquals("There", psInHeadlines.get(1).text());
        }

        @Test
        void filtering_eq_returnsNth() {
            // Arrange
            Document doc = parse("<p>Hello<p>there<p>world");

            // Assert
            assertEquals("there", doc.select("p").eq(1).text());
            assertEquals("there", doc.select("p").get(1).text());
        }

        @Test
        void filtering_is_matchesAny() {
            // Arrange
            Document doc = parse("<p>Hello<p title=foo>there<p>world");
            Elements ps = doc.select("p");

            // Assert
            assertTrue(ps.is("[title=foo]"));
            assertFalse(ps.is("[title=bar]"));
        }

        @Test
        void filtering_not_excludesMatches() {
            // Arrange
            Document doc = parse("<div id=1><p>One</p></div> <div id=2><p><span>Two</span></p></div>");

            // Act / Assert
            Elements divWithoutSpanP = doc.select("div").not(":has(p > span)");
            assertEquals(1, divWithoutSpanP.size());
            assertEquals("1", divWithoutSpanP.first().id());

            Elements notId1 = doc.select("div").not("#1");
            assertEquals(1, notId1.size());
            assertEquals("2", notId1.first().id());
        }

        @Test
        void selection_selectFirst() {
            // Arrange
            Document doc = parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");

            // Act
            Element span = doc.children().selectFirst("span");

            // Assert
            assertNotNull(span);
            assertEquals("Jsoup", span.text());
        }

        @Test
        void selection_selectFirst_returnsNullWhenNoMatch() {
            // Arrange
            Document doc = parse("<p>One</p><p>Two</p><p>Three</p>");

            // Act
            Element span = doc.children().selectFirst("span");

            // Assert
            assertNull(span);
        }

        @Test
        void selection_expectFirst_returnsFirst() {
            // Arrange
            Document doc = parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");

            // Act
            Element span = doc.children().expectFirst("span");

            // Assert
            assertNotNull(span);
            assertEquals("Jsoup", span.text());
        }

        @Test
        void selection_expectFirst_throwsWhenNoMatch() {
            // Arrange
            Document doc = parse("<p>One</p><p>Two</p><p>Three</p>");

            // Act / Assert
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> doc.children().expectFirst("span"));
            assertEquals("No elements matched the query 'span' in the elements.", ex.getMessage());
        }

        @Test
        void selection_selectFirst_fromPreviousSelect_contextAndRoots() {
            // Arrange
            Document doc = parse("<div><p>One</p></div><div><p><span>Two</span></p></div><div><p><span>Three</span></p></div>");
            Elements divs = doc.select("div");
            assertEquals(3, divs.size());

            // Act / Assert
            Element span = divs.selectFirst("p span");
            assertNotNull(span);
            assertEquals("Two", span.text());

            assertNotNull(span.selectFirst("span"));   // reselect self
            assertNull(span.selectFirst(">span"));     // no span>span

            assertNotNull(divs.selectFirst("div"));    // reselect self
            assertNull(divs.selectFirst(">div"));      // no div>div
        }
    }

    // ----------------------------
    // Attributes and classes
    // ----------------------------

    @Nested
    class AttributesAndClasses {
        @Test
        void attributes_getSetRemove() {
            // Arrange
            Document doc = parse("<p title=foo><p title=bar><p class=foo><p class=bar>");

            // Act / Assert: selection with attribute
            Elements withTitle = doc.select("p[title]");
            assertEquals(2, withTitle.size());
            assertTrue(withTitle.hasAttr("title"));
            assertFalse(withTitle.hasAttr("class"));
            assertEquals("foo", withTitle.attr("title"));

            // Removing attr does not re-evaluate existing Elements selection
            withTitle.removeAttr("title");
            assertEquals(2, withTitle.size());
            assertEquals(0, doc.select("p[title]").size());

            // Set new attribute across a selection
            Elements ps = doc.select("p").attr("style", "classy");
            assertEquals(4, ps.size());
            assertEquals("classy", ps.last().attr("style"));
            assertEquals("bar", ps.last().attr("class"));
        }

        @Test
        void attributes_hasAttrAcrossSelection() {
            // Arrange
            Document doc = parse("<p title=foo><p title=bar><p class=foo><p class=bar>");
            Elements ps = doc.select("p");

            // Assert
            assertTrue(ps.hasAttr("class"));
            assertFalse(ps.hasAttr("style"));
        }

        @Test
        void attributes_hasAbsAttrAcrossSelection() {
            // Arrange
            Document doc = parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");

            // Act
            Elements one = doc.select("#1");
            Elements two = doc.select("#2");
            Elements both = doc.select("a");

            // Assert
            assertFalse(one.hasAttr("abs:href"));
            assertTrue(two.hasAttr("abs:href"));
            assertTrue(both.hasAttr("abs:href")); // hits on #2
        }

        @Test
        void attributes_attrReturnsFirstAvailable() {
            // Arrange
            Document doc = parse("<p title=foo><p title=bar><p class=foo><p class=bar>");

            // Act
            String classVal = doc.select("p").attr("class");

            // Assert
            assertEquals("foo", classVal);
        }

        @Test
        void attributes_absAttrReturnsAbsoluteFromFirstAvailable() {
            // Arrange
            Document doc = parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");

            // Act
            Elements one = doc.select("#1");
            Elements two = doc.select("#2");
            Elements both = doc.select("a");

            // Assert
            assertEquals("", one.attr("abs:href"));
            assertEquals("https://jsoup.org", two.attr("abs:href"));
            assertEquals("https://jsoup.org", both.attr("abs:href"));
        }

        @Test
        void attributes_eachAttr_handlesAbsAndMissing() {
            // Arrange
            Document doc = parse(
                "<div><a href='/foo'>1</a><a href='http://example.com/bar'>2</a><a href=''>3</a><a>4</a>",
                "http://example.com"
            );

            // Act / Assert: raw hrefs (missing ones omitted)
            List<String> hrefAttrs = doc.select("a").eachAttr("href");
            assertEquals(3, hrefAttrs.size());
            assertEquals("/foo", hrefAttrs.get(0));
            assertEquals("http://example.com/bar", hrefAttrs.get(1));
            assertEquals("", hrefAttrs.get(2));
            assertEquals(4, doc.select("a").size());

            // Act / Assert: absolute hrefs
            List<String> absAttrs = doc.select("a").eachAttr("abs:href");
            assertEquals(3, absAttrs.size());
            assertEquals("http://example.com/foo", absAttrs.get(0));
            assertEquals("http://example.com/bar", absAttrs.get(1));
            assertEquals("http://example.com", absAttrs.get(2));
        }

        @Test
        void classes_addRemoveToggle() {
            // Arrange
            Document doc = parse("<div><p class='mellow yellow'></p><p class='red green'></p>");
            Elements els = doc.select("p");

            // Act
            assertTrue(els.hasClass("red"));
            assertFalse(els.hasClass("blue"));
            els.addClass("blue");
            els.removeClass("yellow");
            els.toggleClass("mellow");

            // Assert
            assertEquals("blue", els.get(0).className());
            assertEquals("red green blue mellow", els.get(1).className());
        }

        @Test
        void classes_hasClass_isCaseInsensitive() {
            // Arrange
            Elements els = Jsoup.parse("<p Class=One>One <p class=Two>Two <p CLASS=THREE>THREE").select("p");
            Element one = els.get(0);
            Element two = els.get(1);
            Element thr = els.get(2);

            // Assert
            assertTrue(one.hasClass("One"));
            assertTrue(one.hasClass("ONE"));

            assertTrue(two.hasClass("TWO"));
            assertTrue(two.hasClass("Two"));

            assertTrue(thr.hasClass("ThreE"));
            assertTrue(thr.hasClass("three"));
        }

        @Test
        void classes_getElementsByClass_withHyphen() {
            // Arrange
            Document doc = parse("<p class='tab-nav'>Check</p>");

            // Act
            Elements els = doc.getElementsByClass("tab-nav");

            // Assert
            assertEquals(1, els.size());
            assertEquals("Check", els.text());
        }
    }

    // ----------------------------
    // Text and HTML
    // ----------------------------

    @Nested
    class TextAndHtml {
        @Test
        void text_combinedFromDirectChildren() {
            // Arrange
            Document doc = parse("<div><p>Hello<p>there<p>world</div>");

            // Assert
            assertEquals("Hello there world", doc.select("div > *").text());
        }

        @Test
        void text_hasTextOnSelection() {
            // Arrange
            Document doc = parse("<div><p>Hello</p></div><div><p></p></div>");
            Elements divs = doc.select("div");

            // Assert
            assertTrue(divs.hasText());
            assertFalse(doc.select("div + div").hasText());
        }

        @Test
        void text_eachText_ignoresEmpty() {
            // Arrange
            Document doc = parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12<p></p></div>");

            // Act
            List<String> divText = doc.select("div").eachText();
            List<String> pText = doc.select("p").eachText();
            Elements ps = doc.select("p");

            // Assert
            assertEquals(2, divText.size());
            assertEquals("1 2 3 4 5 6", divText.get(0));
            assertEquals("7 8 9 10 11 12", divText.get(1));

            assertEquals(13, ps.size());
            assertEquals(12, pText.size()); // last <p> is empty
            assertEquals("1", pText.get(0));
            assertEquals("2", pText.get(1));
            assertEquals("5", pText.get(4));
            assertEquals("7", pText.get(6));
            assertEquals("12", pText.get(11));
        }

        @Test
        void html_innerHtmlOfSelection() {
            // Arrange
            Document doc = parse("<div><p>Hello</p></div><div><p>There</p></div>");
            Elements divs = doc.select("div");

            // Assert
            assertEquals("<p>Hello</p>\n<p>There</p>", divs.html());
        }

        @Test
        void html_outerHtmlOfSelection() {
            // Arrange
            Document doc = parse("<div><p>Hello</p></div><div><p>There</p></div>");
            Elements divs = doc.select("div");

            // Assert
            assertEquals("<div><p>Hello</p></div><div><p>There</p></div>", stripNl(divs.outerHtml()));
        }

        @Test
        void html_setHtml_prependAppend() {
            // Arrange
            Document doc = parse("<p>One</p><p>Two</p><p>Three</p>");
            Elements ps = doc.select("p");

            // Act
            ps.prepend("<b>Bold</b>").append("<i>Ital</i>");

            // Assert
            assertEquals("<p><b>Bold</b>Two<i>Ital</i></p>", stripNl(ps.get(1).outerHtml()));

            // Act
            ps.html("<span>Gone</span>");

            // Assert
            assertEquals("<p><span>Gone</span></p>", stripNl(ps.get(1).outerHtml()));
        }
    }

    // ----------------------------
    // Forms and values
    // ----------------------------

    @Nested
    class FormsAndValues {
        @Test
        void form_val_getAndSet() {
            // Arrange
            Document doc = parse("<input value='one' /><textarea>two</textarea>");
            Elements formEls = doc.select("input, textarea");

            // Assert get
            assertEquals(2, formEls.size());
            assertEquals("one", formEls.val());
            assertEquals("two", formEls.last().val());

            // Act set
            formEls.val("three");

            // Assert set
            assertEquals("three", formEls.first().val());
            assertEquals("three", formEls.last().val());
            assertEquals("<textarea>three</textarea>", formEls.last().outerHtml());
        }

        @Test
        void traversal_forms_fromElements() {
            // Arrange
            Document doc = parse("<form id=1><input name=q></form><div /><form id=2><input name=f></form>");
            Elements els = doc.select("form, div");

            // Act
            List<FormElement> forms = els.forms();

            // Assert
            assertEquals(3, els.size());
            assertEquals(2, forms.size());
            assertNotNull(forms.get(0));
            assertNotNull(forms.get(1));
            assertEquals("1", forms.get(0).id());
            assertEquals("2", forms.get(1).id());
        }
    }

    // ----------------------------
    // DOM manipulation
    // ----------------------------

    @Nested
    class DomManipulation {
        @Test
        void dom_before_insertsHtmlBeforeEach() {
            // Arrange
            Document doc = parse("<p>This <a>is</a> <a>jsoup</a>.</p>");

            // Act
            doc.select("a").before("<span>foo</span>");

            // Assert
            assertBodyHtmlNoNl(doc, "<p>This <span>foo</span><a>is</a> <span>foo</span><a>jsoup</a>.</p>");
        }

        @Test
        void dom_after_insertsHtmlAfterEach() {
            // Arrange
            Document doc = parse("<p>This <a>is</a> <a>jsoup</a>.</p>");

            // Act
            doc.select("a").after("<span>foo</span>");

            // Assert
            assertBodyHtmlNoNl(doc, "<p>This <a>is</a><span>foo</span> <a>jsoup</a><span>foo</span>.</p>");
        }

        @Test
        void dom_wrap_wrapsEach() {
            // Arrange
            Document doc = parse("<p><b>This</b> is <b>jsoup</b></p>");

            // Act
            doc.select("b").wrap("<i></i>");

            // Assert
            assertBodyHtml(doc, "<p><i><b>This</b></i> is <i><b>jsoup</b></i></p>");
        }

        @Test
        void dom_wrap_wrapsEachBlock() {
            // Arrange
            Document doc = parse("<p><b>This</b> is <b>jsoup</b>.</p> <p>How do you like it?</p>");

            // Act
            doc.select("p").wrap("<div></div>");

            // Assert (pretty printed)
            assertBodyHtml(doc,
                "<div>\n <p><b>This</b> is <b>jsoup</b>.</p>\n</div>\n<div>\n <p>How do you like it?</p>\n</div>"
            );
        }

        @Test
        void dom_unwrap_keepsChildren() {
            // Arrange: note the missing '>' in closing div is tolerated by the parser
            Document doc = parse("<div><font>One</font> <font><a href=\"/\">Two</a></font></div");

            // Act
            doc.select("font").unwrap();

            // Assert
            assertBodyHtml(doc, "<div>\n One <a href=\"/\">Two</a>\n</div>");
        }

        @Test
        void dom_unwrap_onPElements() {
            // Arrange
            Document doc = parse("<p><a>One</a> Two</p> Three <i>Four</i> <p>Fix <i>Six</i></p>");

            // Act
            doc.select("p").unwrap();

            // Assert
            assertBodyHtmlNoNl(doc, "<a>One</a> Two Three <i>Four</i> Fix <i>Six</i>");
        }

        @Test
        void dom_unwrap_keepsInterElementSpaces() {
            // Arrange
            Document doc = parse("<p>One <span>two</span> <span>three</span> four</p>");

            // Act
            doc.select("span").unwrap();

            // Assert
            assertBodyHtml(doc, "<p>One two three four</p>");
        }

        @Test
        void dom_empty_clearsChildren() {
            // Arrange
            Document doc = parse("<div><p>Hello <b>there</b></p> <p>now!</p></div>");
            doc.outputSettings().prettyPrint(false);

            // Act
            doc.select("p").empty();

            // Assert
            assertBodyHtml(doc, "<div><p></p> <p></p></div>");
        }

        @Test
        void dom_remove_removesElements() {
            // Arrange
            Document doc = parse("<div><p>Hello <b>there</b></p> jsoup <p>now!</p></div>");
            doc.outputSettings().prettyPrint(false);

            // Act
            doc.select("p").remove();

            // Assert
            assertBodyHtml(doc, "<div> jsoup </div>");
        }

        @Test
        void dom_tagNameSet_renamesTags() {
            // Arrange
            Document doc = parse("<p>Hello <i>there</i> <i>now</i></p>");

            // Act
            doc.select("i").tagName("em");

            // Assert
            assertBodyHtml(doc, "<p>Hello <em>there</em> <em>now</em></p>");
        }

        @Test
        void listOps_replaceAll_replacesWithMapper() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three<p>Four");
            Elements ps = doc.select("p");
            assertEquals(4, ps.size());

            // Act: replace each <p> with a <div> of same text
            ps.replaceAll(el -> {
                Element div = doc.createElement("div");
                div.text(el.text());
                return div;
            });

            // Assert: Elements list
            for (Element p : ps) {
                assertEquals("div", p.tagName());
            }
            // Assert: DOM updated
            assertBodyHtmlNormalizedSpaces(doc, "<div>One</div><div>Two</div><div>Three</div><div>Four</div>");
        }
    }

    // ----------------------------
    // Traversal and node retrieval
    // ----------------------------

    @Nested
    class TraversalAndNodes {
        @Test
        void traversal_parentsCollectsAncestors() {
            // Arrange
            Document doc = parse("<div><p>Hello</p></div><p>There</p>");

            // Act
            Elements parents = doc.select("p").parents();

            // Assert
            assertEquals(3, parents.size());
            assertEquals("div", parents.get(0).tagName());
            assertEquals("body", parents.get(1).tagName());
            assertEquals("html", parents.get(2).tagName());
        }

        @Test
        void traversal_depthFirstCallbacks() {
            // Arrange
            Document doc = parse("<div><p>Hello</p></div><div>There</div>");
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
        void nodes_comments_directChildren() {
            // Arrange
            Document doc = parse("<!-- comment1 --><p><!-- comment2 --><p class=two><!-- comment3 -->");

            // Act / Assert
            List<Comment> comments = doc.select("p").comments();
            assertEquals(2, comments.size());
            assertEquals(" comment2 ", comments.get(0).getData());
            assertEquals(" comment3 ", comments.get(1).getData());

            List<Comment> commentsInTwo = doc.select("p.two").comments();
            assertEquals(1, commentsInTwo.size());
            assertEquals(" comment3 ", commentsInTwo.get(0).getData());
        }

        @Test
        void nodes_textNodes_directChildren() {
            // Arrange
            Document doc = parse("One<p>Two<a>Three</a><p>Four</p>Five");

            // Act
            List<TextNode> textNodes = doc.select("p").textNodes();

            // Assert
            assertEquals(2, textNodes.size());
            assertEquals("Two", textNodes.get(0).text());
            assertEquals("Four", textNodes.get(1).text());
        }

        @Test
        void nodes_dataNodes_directChildren_live() {
            // Arrange
            Document doc = parse("<p>One</p><script>Two</script><style>Three</style>");

            // Act / Assert: mixed selection only returns data nodes for script/style
            List<DataNode> dataNodes = doc.select("p, script, style").dataNodes();
            assertEquals(2, dataNodes.size());
            assertEquals("Two", dataNodes.get(0).getWholeData());
            assertEquals("Three", dataNodes.get(1).getWholeData());

            // Arrange: script with inline data and one external (no data)
            doc = parse("<head><script type=application/json><crux></script><script src=foo>Blah</script>");
            Elements script = doc.select("script[type=application/json]");

            // Act
            List<DataNode> scriptNode = script.dataNodes();

            // Assert
            assertEquals(1, scriptNode.size());
            DataNode dataNode = scriptNode.get(0);
            assertEquals("<crux>", dataNode.getWholeData());

            // Live update reflects in DOM
            dataNode.setWholeData("<cromulent>");
            assertEquals("<script type=\"application/json\"><cromulent></script>", script.outerHtml());
        }

        @Test
        void nodes_textNodes_emptyWhenNoMatches() {
            // Arrange
            Document doc = parse("<p>");

            // Assert
            assertEquals(0, doc.select("form").textNodes().size());
        }

        @Test
        void traversal_siblings_nextPrevVariants() {
            // Arrange
            Document doc = parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12</div>");
            Elements els = doc.select("p:eq(3)"); // gets p4 and p10
            assertEquals(2, els.size());

            // next
            Elements next = els.next();
            assertEquals(2, next.size());
            assertEquals("5", next.first().text());
            assertEquals("11", next.last().text());

            assertEquals(0, els.next("p:contains(6)").size());
            Elements nextFiltered = els.next("p:contains(5)");
            assertEquals(1, nextFiltered.size());
            assertEquals("5", nextFiltered.first().text());

            // nextAll
            Elements nextAll = els.nextAll();
            assertEquals(4, nextAll.size());
            assertEquals("5", nextAll.first().text());
            assertEquals("12", nextAll.last().text());

            Elements nextAllFiltered = els.nextAll("p:contains(6)");
            assertEquals(1, nextAllFiltered.size());
            assertEquals("6", nextAllFiltered.first().text());

            // prev
            Elements prev = els.prev();
            assertEquals(2, prev.size());
            assertEquals("3", prev.first().text());
            assertEquals("9", prev.last().text());

            assertEquals(0, els.prev("p:contains(1)").size());
            Elements prevFiltered = els.prev("p:contains(3)");
            assertEquals(1, prevFiltered.size());
            assertEquals("3", prevFiltered.first().text());

            // prevAll
            Elements prevAll = els.prevAll();
            assertEquals(6, prevAll.size());
            assertEquals("3", prevAll.first().text());
            assertEquals("7", prevAll.last().text());

            Elements prevAllFiltered = els.prevAll("p:contains(1)");
            assertEquals(1, prevAllFiltered.size());
            assertEquals("1", prevAllFiltered.first().text());
        }
    }

    // ----------------------------
    // List operations (and DOM sync)
    // ----------------------------

    @Nested
    class ListOperations {
        @Test
        void listOps_set_replacesInDomAndList() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three");
            Element newP = doc.createElement("p").text("New").attr("id", "new");
            Elements ps = doc.select("p");
            Element two = ps.get(1);

            // Act
            Element old = ps.set(1, newP);

            // Assert
            assertSame(old, two);
            assertSame(newP, ps.get(1)); // replaced in list
            assertBodyHtml(doc, "<p>One</p>\n<p id=\"new\">New</p>\n<p>Three</p>"); // replaced in DOM
        }

        @Test
        void listOps_removeByIndex() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three");
            Elements ps = doc.select("p");
            Element two = ps.get(1);
            assertTrue(ps.contains(two));

            // Act
            Element old = ps.remove(1);

            // Assert
            assertSame(old, two);
            assertEquals(2, ps.size());   // removed from list
            assertFalse(ps.contains(old));
            assertBodyHtml(doc, "<p>One</p>\n<p>Three</p>"); // removed from DOM
        }

        @Test
        void listOps_removeByObject() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three");
            Elements ps = doc.select("p");
            Element two = ps.get(1);
            assertTrue(ps.contains(two));

            // Act
            boolean removed = ps.remove(two);

            // Assert
            assertTrue(removed);
            assertEquals(2, ps.size());   // removed from list
            assertFalse(ps.contains(two));
            assertBodyHtml(doc, "<p>One</p>\n<p>Three</p>"); // removed from DOM
        }

        @Test
        void listOps_removeByObject_doesNotAffectNonMembers() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three");
            String origHtml = doc.html();
            Element newP = doc.createElement("p").text("New");
            Elements ps = doc.select("p");
            int size = ps.size();

            // Act / Assert
            assertFalse(ps.remove(newP));
            assertFalse(ps.remove(newP.childNodes())); // removing a non-member object should be a no-op
            assertEquals(origHtml, doc.html());
            assertEquals(size, ps.size());
        }

        @Test
        void listOps_clear_removesAllFromDomAndList() {
            // Arrange
            Document doc = parse("<p>One</p><p>Two</p><div>Three</div>");
            Elements ps = doc.select("p");
            assertEquals(2, ps.size());

            // Act
            ps.clear();

            // Assert
            assertEquals(0, ps.size());
            assertEquals(0, doc.select("p").size());
        }

        @Test
        void listOps_removeAll_intersection() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
            Elements ps = doc.select("p");
            Elements midPs = doc.select("p:gt(0):lt(3)"); // Two and Three
            assertEquals(4, ps.size());
            assertEquals(2, midPs.size());

            // Act
            boolean removed = ps.removeAll(midPs);

            // Assert
            assertEquals(2, ps.size());
            assertTrue(removed);
            assertEquals(2, midPs.size());

            Elements divs = doc.select("div");
            assertEquals(1, divs.size());
            assertFalse(ps.removeAll(divs));
            assertEquals(2, ps.size());

            assertBodyHtml(doc, "<p>One</p>\n<p>Four</p>\n<div>Div</div>");
        }

        @Test
        void listOps_retainAll_intersection() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
            Elements ps = doc.select("p");
            Elements midPs = doc.select("p:gt(0):lt(3)"); // Two and Three
            assertEquals(4, ps.size());
            assertEquals(2, midPs.size());

            // Act
            boolean removed = ps.retainAll(midPs);

            // Assert
            assertEquals(2, ps.size());
            assertTrue(removed);
            assertEquals(2, midPs.size());
            assertBodyHtml(doc, "<p>Two</p>\n<p>Three</p>\n<div>Div</div>");

            Elements psAgain = doc.select("p");
            assertFalse(midPs.retainAll(psAgain));
            assertBodyHtml(doc, "<p>Two</p>\n<p>Three</p>\n<div>Div</div>");
        }

        @Test
        void listOps_iteratorRemove_reflectsInDom() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three<p>Four");
            Elements ps = doc.select("p");
            assertEquals(4, ps.size());

            // Act: iterator-backed removal by predicate
            for (Iterator<Element> it = ps.iterator(); it.hasNext(); ) {
                Element el = it.next();
                if (el.text().contains("Two"))
                    it.remove();
            }

            // Assert
            assertEquals(3, ps.size());
            assertBodyHtml(doc, "<p>One</p>\n<p>Three</p>\n<p>Four</p>");
        }

        @Test
        void listOps_removeIf_predicate() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three<p>Four");
            Elements ps = doc.select("p");
            assertEquals(4, ps.size());

            // Act
            boolean removed = ps.removeIf(el -> el.text().contains("Two"));

            // Assert
            assertTrue(removed);
            assertEquals(3, ps.size());
            assertBodyHtml(doc, "<p>One</p>\n<p>Three</p>\n<p>Four</p>");

            // Act (no-op)
            assertFalse(ps.removeIf(el -> el.text().contains("Five")));
            assertBodyHtml(doc, "<p>One</p>\n<p>Three</p>\n<p>Four</p>");
        }

        @Test
        void listOps_removeIf_supportsConcurrentRead() {
            // Arrange
            Document doc = parse("<p>One<p>Two<p>Three<p>Four");
            Elements ps = doc.select("p");
            assertEquals(4, ps.size());

            // Act
            boolean removed = ps.removeIf(ps::contains);

            // Assert
            assertTrue(removed);
            assertEquals(0, ps.size());
            assertEquals("", doc.body().html());
        }
    }
}