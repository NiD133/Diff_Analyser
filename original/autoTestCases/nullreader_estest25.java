package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        NullReader nullReader0 = new NullReader((-955L));
        nullReader0.INSTANCE.mark(1180);
        nullReader0.getPosition();
        char[] charArray0 = new char[1];
        char char0 = 'U';
        charArray0[0] = 'U';
        nullReader0.read(charArray0, 1180, 1180);
        nullReader0.read();
        char[] charArray1 = new char[9];
        charArray1[0] = 'U';
        nullReader0.getPosition();
        nullReader0.INSTANCE.read(charArray1, (-1056), (-196));
    }
}
