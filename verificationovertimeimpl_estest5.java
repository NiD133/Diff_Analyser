package org.mockito.internal.verification;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.After;
import org.mockito.verification.Timeout;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImpl_ESTestTest5 extends VerificationOverTimeImpl_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        AtMost atMost0 = new AtMost(3199);
        Timeout timeout0 = new Timeout(0L, atMost0);
        Timer timer0 = new Timer(3199);
        VerificationOverTimeImpl verificationOverTimeImpl0 = new VerificationOverTimeImpl(0L, timeout0, false, timer0);
        VerificationMode verificationMode0 = verificationOverTimeImpl0.getDelegate();
        assertSame(verificationMode0, timeout0);
    }
}
