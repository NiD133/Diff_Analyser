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

public class LocaleUtils_ESTestTest1 extends LocaleUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Locale locale0 = Locale.SIMPLIFIED_CHINESE;
        Locale locale1 = new Locale("Italy", "und", "LV");
        List<Locale> list0 = LocaleUtils.localeLookupList(locale1, locale0);
        assertEquals(4, list0.size());
    }
}
