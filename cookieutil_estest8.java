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
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.junit.runner.RunWith;

public class CookieUtil_ESTestTest8 extends CookieUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        HttpConnection.Request httpConnection_Request0 = new HttpConnection.Request();
        TextNode textNode0 = TextNode.createFromEncoded("");
        Attributes attributes0 = textNode0.attributes();
        attributes0.put("", "");
        Map<String, String> map0 = attributes0.dataset();
        httpConnection_Request0.cookies = map0;
        // Undeclared exception!
        try {
            CookieUtil.applyCookiesToRequest(httpConnection_Request0, (BiConsumer<String, String>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // String must not be empty
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
