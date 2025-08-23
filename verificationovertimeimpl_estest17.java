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

public class VerificationOverTimeImpl_ESTestTest17 extends VerificationOverTimeImpl_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        After after0 = new After(0L, (VerificationMode) null);
        Timer timer0 = new Timer(488L);
        VerificationOverTimeImpl verificationOverTimeImpl0 = new VerificationOverTimeImpl((-288L), after0, true, timer0);
        long long0 = verificationOverTimeImpl0.getPollingPeriodMillis();
        assertTrue(verificationOverTimeImpl0.isReturnOnSuccess());
        assertEquals((-288L), long0);
    }
}