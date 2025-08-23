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

public class LocaleUtils_ESTestTest39 extends LocaleUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        List<Locale> list0 = LocaleUtils.countriesByLanguage("q@:a$7mI");
        assertEquals(0, list0.size());
    }
}
