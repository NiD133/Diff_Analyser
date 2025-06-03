package com.itextpdf.text.error_messages;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeneratedTestCase {

    @Test
    public void testBackslashes() throws Exception {
        String testPath = "C:\\test\\file.txt";
        String rslt = MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", testPath);
        Assert.assertTrue("Result doesn't contain the test path", rslt.contains(testPath));
    }
}
