package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.language.Metaphone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MetaphoneTest extends Metaphone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMetaphoneConversionForSimpleWord() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("acw");
        assertEquals("AK", result);
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testEncodeMethodWithMixedCaseInput() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.encode("GtA");
        // Unstable assertion removed
    }

    @Test(timeout = 4000)
    public void testIsMetaphoneEqualWithEmptyStrings() {
        Metaphone metaphone = new Metaphone();
        metaphone.isMetaphoneEqual("", "");
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testSetMaxCodeLenToZero() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(0);
        assertEquals(0, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testSetMaxCodeLenToNegativeValue() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(-3486);
        assertEquals(-3486, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testMetaphoneConversionWithSpecialCharacters() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("!1-HOe,>9Y[:a%E");
        assertEquals("H", result);
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testMetaphoneConversionForCommonPatterns() {
        Metaphone metaphone = new Metaphone();
        assertEquals("0", metaphone.metaphone("TH"));
        assertEquals("X", metaphone.metaphone("CH"));
        assertEquals("SK", metaphone.metaphone("SCH"));
        assertEquals("M", metaphone.metaphone("MB"));
        assertEquals("N", metaphone.metaphone("GN"));
        assertEquals("V", metaphone.metaphone("v"));
    }

    @Test(timeout = 4000)
    public void testMetaphoneConversionForNullInput() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone((String) null);
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testGetMaxCodeLenDefaultValue() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testEncodeMethodWithNullInput() {
        Metaphone metaphone = new Metaphone();
        metaphone.encode((String) null);
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testEncodeMethodWithNonStringObject() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.encode((Object) metaphone);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.language.Metaphone", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsMetaphoneEqualWithDifferentStrings() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("I", "X");
        assertFalse(result);
        assertEquals(4, metaphone.getMaxCodeLen());
    }
}