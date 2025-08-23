package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for {@link StringUtil#resolve(String, String)}.
 * This class verifies the correctness of URL resolution logic across various scenarios.
 */
@DisplayName("StringUtil.resolve()")
public class StringUtilResolveTest {

    @Test
    void resolvesBasicRelativePaths() {
        // A simple relative path is appended to the base URL's path.
        assertEquals("http://example.com/one/two?three", resolve("http://example.com", "./one/two?three"));
        // A relative path without './' is handled identically.
        assertEquals("http://example.com/one/two.c", resolve("http://example.com/one/", "two.c"));
    }

    @Test
    void resolvesRelativePathsWithQueryAndFragment() {
        // The base URL's query string should be replaced by the relative URL's path.
        assertEquals("http://example.com/one/two?three", resolve("http://example.com?one", "./one/two?three"));
        // The fragment from the relative URL should be preserved.
        assertEquals("http://example.com/one/two?three#four", resolve("http://example.com", "./one/two?three#four"));
    }

    @Test
    void resolvesPathsWithParentDirectoryNavigation() {
        // '..' should navigate up the path hierarchy.
        assertEquals("http://example.com/one/two.html", resolve("http://example.com/two/", "../one/two.html"));
        assertEquals("ftp://example.com/one", resolve("ftp://example.com/two/", "../one"));
    }

    @Test
    void returnsRelativeUrlIfItIsAbsolute() {
        // If the "relative" URL is already absolute, it should be returned, ignoring the base.
        assertEquals("https://example.com/one", resolve("http://example.com/", "https://example.com/one"));
        assertEquals("https://example2.com/one", resolve("http://example.com/", "https://example2.com/one"));
    }

    @Test
    void resolvesProtocolRelativeUrl() {
        // A protocol-relative URL ("//...") should adopt the base URL's protocol.
        assertEquals("https://example2.com/one", resolve("https://example.com/", "//example2.com/one"));
    }

    @Test
    void preservesPortAndProtocolFromBaseUrl() {
        assertEquals("https://example.com:8080/one", resolve("https://example.com:8080", "./one"));
        assertEquals("ftp://example.com/one/two.c", resolve("ftp://example.com/one/", "./two.c"));
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTest {
        @Test
        void returnsRelativeUrlWhenBaseIsInvalid() {
            // If the base URL is invalid but the relative URL is a valid absolute URL, return the relative one.
            assertEquals("https://example.com/one", resolve("wrong", "https://example.com/one"));
        }

        @Test
        void returnsBaseUrlWhenRelativeIsEmpty() {
            // If the relative URL is empty, the base URL should be returned.
            assertEquals("https://example.com/one", resolve("https://example.com/one", ""));
        }

        @Test
        void returnsEmptyStringWhenBothUrlsAreMalformed() {
            // If both URLs are malformed or cannot be resolved, return an empty string.
            assertEquals("", resolve("wrong", "also wrong"));
        }
    }

    /**
     * Test cases adapted from RFC 3986, section 5.4.2.
     * These test the handling of dot-segments ('.', '..') and other path normalization rules.
     * @see <a href="https://tools.ietf.org/html/rfc3986#section-5.4.2">RFC 3986 5.4.2</a>
     */
    @Nested
    @DisplayName("RFC 3986 Section 5.4.2 Examples")
    class Rfc3986ExamplesTest {
        private static final String BASE_URL = "http://example.com/b/c/d;p?q";

        @ParameterizedTest(name = "[{index}] resolve(\"{1}\") -> \"{2}\"")
        @CsvSource(textBlock = """
            # Input relative URL,  Expected resolved URL
            '../../../g',          'http://example.com/g'
            '../../../../g',         'http://example.com/g'
            '/./g',                'http://example.com/g'
            '/../g',               'http://example.com/g'
            'g.',                  'http://example.com/b/c/g.'
            '.g',                  'http://example.com/b/c/.g'
            'g..',                 'http://example.com/b/c/g..'
            '..g',                 'http://example.com/b/c/..g'
            './../g',              'http://example.com/b/g'
            './g/.',               'http://example.com/b/c/g/'
            'g/./h',               'http://example.com/b/c/g/h'
            'g/../h',              'http://example.com/b/c/h'
            'g;x=1/./y',           'http://example.com/b/c/g;x=1/y'
            'g;x=1/../y',          'http://example.com/b/c/y'
            'g?y/./x',             'http://example.com/b/c/g?y/./x'
            'g?y/../x',            'http://example.com/b/c/g?y/../x'
            'g#s/./x',             'http://example.com/b/c/g#s/./x'
            'g#s/../x',            'http://example.com/b/c/g#s/../x'
            """, useHeadersInDisplayName = true)
        void resolvesAccordingToRfc3986(String relativeUrl, String expectedUrl) {
            String actualUrl = resolve(BASE_URL, relativeUrl);
            assertEquals(expectedUrl, actualUrl);
        }
    }
}