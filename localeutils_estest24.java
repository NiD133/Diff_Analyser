package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class LocaleUtils_ESTestTest24 extends LocaleUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Locale locale0 = Locale.ENGLISH;
        Locale locale1 = Locale.ROOT;
        List<Locale> list0 = LocaleUtils.localeLookupList(locale0, locale1);
        assertEquals(2, list0.size());
        assertTrue(list0.contains(locale1));
    }
}
