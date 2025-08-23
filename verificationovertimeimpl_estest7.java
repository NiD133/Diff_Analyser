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

public class VerificationOverTimeImpl_ESTestTest7 extends VerificationOverTimeImpl_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        NoMoreInteractions noMoreInteractions0 = new NoMoreInteractions();
        VerificationOverTimeImpl verificationOverTimeImpl0 = new VerificationOverTimeImpl((-102L), 0L, noMoreInteractions0, false);
        VerificationOverTimeImpl verificationOverTimeImpl1 = verificationOverTimeImpl0.copyWithVerificationMode(noMoreInteractions0);
        assertEquals((-102L), verificationOverTimeImpl1.getPollingPeriodMillis());
        assertEquals((-102L), verificationOverTimeImpl0.getPollingPeriodMillis());
    }
}
