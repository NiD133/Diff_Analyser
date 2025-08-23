package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexExtractor_ESTestTest6 extends IndexExtractor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Shape shape0 = Shape.fromNM(2147352576, 2147352576);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        // Undeclared exception!
        simpleBloomFilter0.asIndexArray();
    }
}
