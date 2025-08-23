package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("QueryParser: Comma-Separated and Child Selectors")
class QueryParserTest {

    private Document doc;

    @BeforeEach
    void setUp() {
        // This HTML structure is used to test various child and descendant selections.
        String html = """
            <html><head></head><body>
              <li><strong>l1</strong></li>          <!-- direct child of body -->
              <a><li><strong>l2</strong></li></a>    <!-- nested inside <a> -->
              <p><strong>yes</strong></p>          <!-- direct child of body -->
            </body></html>
            """;
        doc = Jsoup.parse(html);
    }

    @Test
    @DisplayName("should select immediate children using an OR condition")
    void selectsImmediateChildrenWithOrLogic() {
        // Selector Breakdown:
        // 1. `>p>strong`: From the context (body), find a direct child <p>, then its direct child <strong>. Matches "yes".
        // 2. `>li>strong`: From the context (body), find a direct child <li>, then its direct child <strong>. Matches "l1".
        // "l2" is not matched because its parent <li> is not a direct child of <body>.
        Elements selection = doc.body().select(">p>strong,>li>strong");

        assertEquals(2, selection.size());
        assertEquals("l1 yes", selection.text());
    }

    @Test
    @DisplayName("should handle whitespace around combinators and separators")
    void handlesWhitespaceAroundCombinators() {
        // This test verifies that parsing is tolerant of extra whitespace around combinators (>) and separators (,).
        Elements selection = doc.body().select(" > p > strong , > li > strong ");

        assertEquals(2, selection.size());
        assertEquals("l1 yes", selection.text());
    }

    @Test
    @DisplayName("should correctly combine wildcard, child, and OR selectors")
    void combinesWildcardAndChildSelectors() {
        // Selector Breakdown:
        // 1. `body>p>strong`: Finds <body>, its direct child <p>, then its direct child <strong>. Matches "yes".
        // 2. `body>*>li>strong`: Finds <body>, any direct child (*), its direct child <li>, then its direct child <strong>.
        // The wildcard (*) matches the <a> tag, leading to the selection of "l2".
        Elements selection = doc.select("body>p>strong,body>*>li>strong");

        assertEquals(2, selection.size());
        assertEquals("l2 yes", selection.text());
    }

    @Test
    @DisplayName("should produce the same result regardless of OR clause order")
    void resultIsIndependentOfOrClauseOrder() {
        String selectorOrder1 = "body>*>li>strong, body>p>strong";
        String selectorOrder2 = "body>p>strong, body>*>li>strong";

        String result1 = doc.select(selectorOrder1).text();
        String result2 = doc.select(selectorOrder2).text();

        assertEquals("l2 yes", result1);
        assertEquals(result1, result2, "The order of OR'd selector clauses should not change the result.");
    }
}