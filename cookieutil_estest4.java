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

public class CookieUtil_ESTestTest4 extends CookieUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        URL uRL0 = MockURL.getFtpExample();
        HttpConnection.Response httpConnection_Response0 = new HttpConnection.Response();
        HttpConnection.Request httpConnection_Request0 = new HttpConnection.Request();
        // Undeclared exception!
        try {
            CookieUtil.storeCookies(httpConnection_Request0, httpConnection_Response0, uRL0, (Map<String, List<String>>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Argument is null
            //
            verifyException("java.net.CookieManager", e);
        }
    }
}
