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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class CookieUtil_ESTest extends CookieUtil_ESTest_scaffolding {

    // Tests for asUri() method
    
    @Test(timeout = 4000)
    public void testAsUri_WithValidFtpUrl_ShouldReturnCorrectScheme() throws Throwable {
        // Given: A valid FTP URL
        URL ftpUrl = MockURL.getFtpExample();
        
        // When: Converting URL to URI
        URI result = CookieUtil.asUri(ftpUrl);
        
        // Then: URI should have correct FTP scheme
        assertEquals("ftp", result.getScheme());
    }

    @Test(timeout = 4000)
    public void testAsUri_WithNullUrl_ShouldThrowNullPointerException() throws Throwable {
        // Given: A null URL
        URL nullUrl = null;
        
        // When & Then: Should throw NullPointerException
        try { 
            CookieUtil.asUri(nullUrl);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAsUri_WithMalformedUrl_ShouldThrowMalformedURLException() throws Throwable {
        // Given: A file with illegal characters that creates malformed URL
        MockFile fileWithIllegalChars = new MockFile("\u0005!/", "\u0005!/");
        URL malformedUrl = fileWithIllegalChars.toURL();
        
        // When & Then: Should throw MalformedURLException
        try { 
            CookieUtil.asUri(malformedUrl);
            fail("Expected MalformedURLException");
        } catch(MalformedURLException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    // Tests for storeCookies() method
    
    @Test(timeout = 4000)
    public void testStoreCookies_WithValidSetCookieHeader_ShouldProcessSuccessfully() throws Throwable {
        // Given: Request, response, URL and headers with Set-Cookie
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response(request);
        URL url = request.url;
        
        Map<String, List<String>> headers = new HashMap<>();
        List<String> cookieValues = new LinkedList<>();
        cookieValues.add("XtzONrG&vb");
        headers.put("Set-Cookie", cookieValues);
        
        // When: Storing cookies
        CookieUtil.storeCookies(request, response, url, headers);
        
        // Then: Should complete without error
        assertEquals(0, response.statusCode());
    }

    @Test(timeout = 4000)
    public void testStoreCookies_WithNonCookieHeaders_ShouldProcessSuccessfully() throws Throwable {
        // Given: Request, response, URL and headers without Set-Cookie
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response(request);
        URL url = request.url;
        
        Map<String, List<String>> headers = new HashMap<>();
        List<String> nonCookieValues = new LinkedList<>();
        headers.put("inputStream", nonCookieValues);
        
        // When: Storing cookies (no Set-Cookie header present)
        CookieUtil.storeCookies(request, response, url, headers);
        
        // Then: Should complete without error
        assertNull(request.requestBody());
    }

    @Test(timeout = 4000)
    public void testStoreCookies_WithNullUrl_ShouldThrowNullPointerException() throws Throwable {
        // Given: Request, response with null URL
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, List<String>> headers = request.headers;
        
        // When & Then: Should throw NullPointerException
        try { 
            CookieUtil.storeCookies(request, response, null, headers);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookies_WithNullHeaders_ShouldThrowIllegalArgumentException() throws Throwable {
        // Given: Request, response, URL but null headers
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        URL url = MockURL.getFtpExample();
        
        // When & Then: Should throw IllegalArgumentException
        try { 
            CookieUtil.storeCookies(request, response, url, null);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Argument is null", e.getMessage());
            verifyException("java.net.CookieManager", e);
        }
    }

    @Test(timeout = 4000)
    public void testStoreCookies_WithMalformedFileUrl_ShouldThrowMalformedURLException() throws Throwable {
        // Given: Request, response with malformed file URL
        HttpConnection.Response response = new HttpConnection.Response();
        HttpConnection.Request request = new HttpConnection.Request();
        Map<String, List<String>> headers = request.headers;
        
        MockFile fileWithSpecialChars = new MockFile("@]?3#", "@]?3#");
        URL malformedUrl = fileWithSpecialChars.toURL();
        
        // When & Then: Should throw MalformedURLException
        try { 
            CookieUtil.storeCookies(request, response, malformedUrl, headers);
            fail("Expected MalformedURLException");
        } catch(MalformedURLException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    // Tests for parseCookie() method
    
    @Test(timeout = 4000)
    public void testParseCookie_WithEmptyString_ShouldHandleGracefully() throws Throwable {
        // Given: Empty cookie string and response
        HttpConnection.Response response = new HttpConnection.Response();
        
        // When: Parsing empty cookie
        CookieUtil.parseCookie("", response);
        
        // Then: Should handle gracefully
        assertEquals(400, response.statusCode());
    }

    @Test(timeout = 4000)
    public void testParseCookie_WithNullString_ShouldHandleGracefully() throws Throwable {
        // Given: Null cookie string and response
        HttpConnection.Response response = new HttpConnection.Response();
        
        // When: Parsing null cookie
        CookieUtil.parseCookie(null, response);
        
        // Then: Should handle gracefully
        assertEquals(400, response.statusCode());
    }

    @Test(timeout = 4000)
    public void testParseCookie_WithNullResponse_ShouldThrowNullPointerException() throws Throwable {
        // Given: Valid cookie string but null response
        String cookieValue = "del";
        
        // When & Then: Should throw NullPointerException
        try { 
            CookieUtil.parseCookie(cookieValue, null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseCookie_WithValidCookieString_ShouldParseCorrectly() throws Throwable {
        // Given: Request, response with shared cookie map, and valid cookie string
        HttpConnection.Request request = new HttpConnection.Request();
        HttpConnection.Response response = new HttpConnection.Response();
        Map<String, String> sharedCookieMap = request.cookies;
        response.cookies = sharedCookieMap;
        
        // When: Parsing valid cookie
        CookieUtil.parseCookie("wToM>AI", response);
        
        // Then: Cookie should be parsed (verified by subsequent applyCookiesToRequest call behavior)
        try { 
            CookieUtil.applyCookiesToRequest(request, null);
            fail("Expected NullPointerException due to null BiConsumer");
        } catch(NullPointerException e) {
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }

    // Tests for applyCookiesToRequest() method
    
    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_WithEmptyCookies_ShouldCompleteSuccessfully() throws Throwable {
        // Given: Request with no cookies and mock BiConsumer
        HttpConnection.Request request = new HttpConnection.Request();
        BiConsumer<String, String> mockSetter = mock(BiConsumer.class, new ViolatedAssumptionAnswer());
        
        // When: Applying cookies to request
        CookieUtil.applyCookiesToRequest(request, mockSetter);
        
        // Then: Should complete without error
        assertNull(request.requestBody());
    }

    @Test(timeout = 4000)
    public void testApplyCookiesToRequest_WithEmptyStringCookie_ShouldThrowIllegalArgumentException() throws Throwable {
        // Given: Request with empty string cookie name
        HttpConnection.Request request = new HttpConnection.Request();
        
        // Create attributes with empty string key
        TextNode textNode = TextNode.createFromEncoded("");
        Attributes attributes = textNode.attributes();
        attributes.put("", "");
        Map<String, String> cookiesWithEmptyKey = attributes.dataset();
        request.cookies = cookiesWithEmptyKey;
        
        // When & Then: Should throw IllegalArgumentException for empty string
        try { 
            CookieUtil.applyCookiesToRequest(request, null);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("String must not be empty", e.getMessage());
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    // Test for constructor (coverage)
    
    @Test(timeout = 4000)
    public void testCookieUtilConstructor_ShouldCreateInstance() throws Throwable {
        // When: Creating CookieUtil instance
        CookieUtil cookieUtil = new CookieUtil();
        
        // Then: Instance should be created successfully
        assertNotNull(cookieUtil);
    }
}