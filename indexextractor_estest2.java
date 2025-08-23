package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest2 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher((-2285L), (-2285L));
        Shape shape0 = Shape.fromNM(6, 2147352576);
        IndexExtractor indexExtractor0 = enhancedDoubleHasher0.indices(shape0);
        // Undeclared exception!
        indexExtractor0.uniqueIndices();
    }
}
