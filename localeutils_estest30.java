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

public class LocaleUtils_ESTestTest30 extends LocaleUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Locale locale0 = Locale.ROOT;
        boolean boolean0 = LocaleUtils.isLanguageUndetermined(locale0);
        assertTrue(boolean0);
    }
}
