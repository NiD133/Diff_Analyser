/*
 * Refactored for clarity and maintainability
 */
package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.jsoup.helper.CookieUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
) 
public class CookieUtil_ESTest extends CookieUtil_ESTest_scaffolding {

    // ========================================================================
    // asUri() Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testAsUri_WithValidFtpUrl_ReturnsUriWithCorrectScheme() throws Throwable {
        URL ftpUrl = MockURL.getFtpExample();
        URI result = CookieUtil.asUri(ftpUrl);
        assertEquals("ftp", result.getScheme());
    }

    @Test(timeout = 4000)
    public void testAsUri_WithInvalidCharacters_ThrowsMalformedURLException() {
        MockFile invalidFile = new MockFile("\u0005!/", "\u0005!/");
        URL invalidUrl = invalidFile.toURL();
        
        try {
            CookieUtil.asUri(invalidUrl);
            fail("Expected MalformedURLException");
        } catch(MalformedURLException e) {
            assertTrue(e.getMessage().contains("Illegal character in path"));
        }
    }

    @Test(timeout = 4000)
    public void testAsUri_WithNullUrl_ThrowsNullPointerException() {
        try {
            CookieUtil.asUri((URL) null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected behavior
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getClass().getSimpleName());
        }
    }

    // ========================================================================
    // storeCookies() Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testStoreCookies_WithMalformedUrl_ThrowsException() {
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> headers = request.headers;
        MockFile invalidFile = new MockFile("@]?3#", "@]?3#");
        URL invalidUrl = invalidFile.toURL();
        
        try {
            CookieUtil.storeCookies(request, response, invalidUrl, headers);
            fail("Expected MalformedURLException");
        } catch(MalformedURLException e) {
            assertTrue(e.getMessage().contains("Illegal character in path"));
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookies_WithNullUrl_ThrowsNullPointerException() {
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> headers = request.headers;
        
        try {
            CookieUtil.storeCookies(request, response, null, headers);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected behavior
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getClass().getSimpleName());
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookies_WithNullHeaders_ThrowsIllegalArgumentException() {
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        URL validUrl = MockURL.getFtpExample();
        
        try {
            CookieUtil.storeCookies(request, response, validUrl, null);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Argument is null", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getClass().getSimpleName());
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookies_WithSetCookieHeader_StoresSuccessfully() throws Throwable {
        HttpConnection.Request request = new HttpConnection.Request();
        URL url = request.url;
        Map<String, List<String>> headers = new HashMap<>();
        List<String> cookies = new LinkedList<>();
        cookies.add("session=abc123");
        headers.put("Set-Cookie", cookies);
        
        HttpConnection.Response response = new HttpConnection.Response(request);
        CookieUtil.storeCookies(request, response, url, headers);
        
        assertEquals(0, response.statusCode());
    }

    @Test(timeout = 4000)
    public void testStoreCookies_WithNonCookieHeader_NoCookiesStored() throws Throwable {
        HttpConnection.Request request = new HttpConnection.Request();
        URL url = request.url;
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", new LinkedList<>()); // Non-cookie header
        
        HttpConnection.Response response = new HttpConnection.Response(request);
        CookieUtil.storeCookies(request, response, url, headers);
        
        assertNull(request.requestBody());
    }

    // ========================================================================
    // parseCookie() Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testParseCookie_WithNullResponse_ThrowsNullPointerException() {
        try {
            CookieUtil.parseCookie("session=abc", null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testParseCookie_WithEmptyString_DoesNothing() {
        HttpConnection.Response response = new HttpConnection.Response();
        CookieUtil.parseCookie("", response);
        assertEquals(400, response.statusCode());
    }

    @Test(timeout = 4000)
    public void testParseCookie_WithNullString_DoesNothing() {
        HttpConnection.Response response = new HttpConnection.Response();
        CookieUtil.parseCookie(null, response);
        assertEquals(400, response.statusCode());
    }

    // ========================================================================
    // applyCookiesToRequest() Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_WithEmptyCookieName_ThrowsException() {
        HttpConnection.Request request = new HttpConnection.Request();
        TextNode node = TextNode.createFromEncoded("");
        Attributes attributes = node.attributes();
        attributes.put("", "value"); // Empty cookie name
        request.cookies = attributes.dataset();
        
        try {
            CookieUtil.applyCookiesToRequest(request, null);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("String must not be empty"));
        }
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_WithNullConsumer_ThrowsNullPointerException() {
        HttpConnection.Request request = new HttpConnection.Request();
        request.cookies = new HashMap<>();
        request.cookies.put("session", "abc");
        
        try {
            CookieUtil.applyCookiesToRequest(request, null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_WithNoCookies_DoesNothing() {
        HttpConnection.Request request = new HttpConnection.Request();
        BiConsumer<String, String> mockConsumer = mock(BiConsumer.class);
        
        CookieUtil.applyCookiesToRequest(request, mockConsumer);
        verifyNoInteractions(mockConsumer);
    }
}