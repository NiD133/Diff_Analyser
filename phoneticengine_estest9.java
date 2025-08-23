package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import java.util.LinkedHashSet;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PhoneticEngine_ESTestTest9 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        NameType nameType0 = NameType.SEPHARDIC;
        RuleType ruleType0 = RuleType.EXACT;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, false);
        LinkedHashSet<String> linkedHashSet0 = new LinkedHashSet<String>();
        Languages.LanguageSet languages_LanguageSet0 = Languages.LanguageSet.from(linkedHashSet0);
        // Undeclared exception!
        phoneticEngine0.encode("langvadZesoibdZgZdf|langvadZesoibdZgxdf|langvadZesojbdZgZdf|langvadZesojbdZgxdf|langvagesoibdZgZdf|langvagesoibdZgxdf|langvagesojbdZgZdf|langvagesojbdZgxdf|langvaxesoibdZgZdf|langvaxesoibdZgxdf|langvaxesojbdZgZdf|langvaxesojbdZgxdf-rf|rp", languages_LanguageSet0);
    }
}
