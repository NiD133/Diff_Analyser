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

public class VerificationOverTimeImpl_ESTestTest9 extends VerificationOverTimeImpl_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        VerificationOverTimeImpl verificationOverTimeImpl0 = new VerificationOverTimeImpl(3L, 3L, (VerificationMode) null, true);
        VerificationOverTimeImpl verificationOverTimeImpl1 = verificationOverTimeImpl0.copyWithVerificationMode((VerificationMode) null);
        // Undeclared exception!
        try {
            verificationOverTimeImpl1.copyWithVerificationMode(verificationOverTimeImpl0);
            //  fail("Expecting exception: NoSuchElementException");
            // Unstable assertion
        } catch (NoSuchElementException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.LinkedList", e);
        }
    }
}
