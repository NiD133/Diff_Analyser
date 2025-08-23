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

public class VerificationOverTimeImpl_ESTestTest10 extends VerificationOverTimeImpl_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        VerificationOverTimeImpl verificationOverTimeImpl0 = new VerificationOverTimeImpl(0L, 0L, (VerificationMode) null, false);
        VerificationOverTimeImpl verificationOverTimeImpl1 = null;
        try {
            verificationOverTimeImpl1 = new VerificationOverTimeImpl((-720L), (-720L), verificationOverTimeImpl0, false);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
        }
    }
}
