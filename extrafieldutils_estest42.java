package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ExtraFieldUtils_ESTestTest42 extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        Class<UnparseableExtraFieldData> class0 = UnparseableExtraFieldData.class;
        ExtraFieldUtils.register(class0);
    }
}
