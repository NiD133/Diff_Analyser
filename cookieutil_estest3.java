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

public class CookieUtil_ESTestTest3 extends CookieUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        HttpConnection.Request httpConnection_Request0 = new HttpConnection.Request();
        HttpConnection.Response httpConnection_Response0 = new HttpConnection.Response();
        Map<String, List<String>> map0 = httpConnection_Request0.headers;
        // Undeclared exception!
        try {
            CookieUtil.storeCookies(httpConnection_Request0, httpConnection_Response0, (URL) null, map0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
