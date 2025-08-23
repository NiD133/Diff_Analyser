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

public class VerificationOverTimeImpl_ESTestTest11 extends VerificationOverTimeImpl_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        NoMoreInteractions noMoreInteractions0 = new NoMoreInteractions();
        Timeout timeout0 = new Timeout(318L, noMoreInteractions0);
        Timer timer0 = new Timer(5157L);
        VerificationOverTimeImpl verificationOverTimeImpl0 = new VerificationOverTimeImpl(1L, timeout0, true, timer0);
        boolean boolean0 = verificationOverTimeImpl0.canRecoverFromFailure(noMoreInteractions0);
        assertEquals(1L, verificationOverTimeImpl0.getPollingPeriodMillis());
        assertFalse(boolean0);
    }
}
