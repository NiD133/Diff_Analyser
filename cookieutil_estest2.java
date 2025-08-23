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

public class CookieUtil_ESTestTest2 extends CookieUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        HttpConnection.Response httpConnection_Response0 = new HttpConnection.Response();
        HttpConnection.Request httpConnection_Request0 = new HttpConnection.Request();
        Map<String, List<String>> map0 = httpConnection_Request0.headers;
        MockFile mockFile0 = new MockFile("@]?3#", "@]?3#");
        URL uRL0 = mockFile0.toURL();
        try {
            CookieUtil.storeCookies(httpConnection_Request0, httpConnection_Response0, uRL0, map0);
            fail("Expecting exception: MalformedURLException");
        } catch (MalformedURLException e) {
            //
            // Illegal character in path at index 53: file:/Users/tenghaha/Desktop/EvoSuiteProjects/jsoup/@]?3#/@]?3#
            //
            verifyException("org.jsoup.helper.CookieUtil", e);
        }
    }
}
