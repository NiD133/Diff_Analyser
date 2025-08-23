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

public class VerificationOverTimeImpl_ESTestTest2 extends VerificationOverTimeImpl_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        NoMoreInteractions noMoreInteractions0 = new NoMoreInteractions();
        After after0 = new After(2416L, noMoreInteractions0);
        VerificationOverTimeImpl verificationOverTimeImpl0 = new VerificationOverTimeImpl(5157L, 1L, after0, false);
        Timer timer0 = verificationOverTimeImpl0.getTimer();
        VerificationOverTimeImpl verificationOverTimeImpl1 = new VerificationOverTimeImpl(5157L, noMoreInteractions0, true, timer0);
        verificationOverTimeImpl1.isReturnOnSuccess();
        assertEquals(5157L, verificationOverTimeImpl1.getPollingPeriodMillis());
    }
}
