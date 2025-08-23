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

public class PhoneticEngine_ESTestTest24 extends PhoneticEngine_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        NameType nameType0 = NameType.SEPHARDIC;
        RuleType ruleType0 = RuleType.APPROX;
        PhoneticEngine phoneticEngine0 = new PhoneticEngine(nameType0, ruleType0, true);
        assertEquals(20, phoneticEngine0.getMaxPhonemes());
        String string0 = phoneticEngine0.encode("della languages([o/,y)ptjgjdw~` r7b])");
        assertEquals("langvagisDbzghdvrf|langvagisDbzghdvrp|langvagisDbzgzdvrf|langvagisDbzgzdvrp|langvagisuibzghdvrf|langvagisuibzghdvrp|langvagisuibzgzdvrf|langvagisuibzgzdvrp|langvahisDbzghdvrf|langvahisDbzghdvrp|langvahisDbzgzdvrf|langvahisDbzgzdvrp|langvahisuibzghdvrf|langvahisuibzghdvrp|langvahisuibzgzdvrf|langvahisuibzgzdvrp|langvazisuibzghdvrf|langvazisuibzghdvrp|langvazisuibzgzdvrf|langvazisuibzgzdvrp", string0);
    }
}
