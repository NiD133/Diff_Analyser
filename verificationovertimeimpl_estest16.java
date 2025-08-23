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

public class VerificationOverTimeImpl_ESTestTest16 extends VerificationOverTimeImpl_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Timer timer0 = new Timer(1418L);
        VerificationOverTimeImpl verificationOverTimeImpl0 = new VerificationOverTimeImpl(1418L, (VerificationMode) null, true, timer0);
        verificationOverTimeImpl0.getDelegate();
        assertTrue(verificationOverTimeImpl0.isReturnOnSuccess());
        assertEquals(1418L, verificationOverTimeImpl0.getPollingPeriodMillis());
    }
}
