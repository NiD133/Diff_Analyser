package org.apache.ibatis.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.ibatis.builder.ParameterExpression;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ParameterExpression_ESTest extends ParameterExpression_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testValidExpressionWithTwoAttributes() throws Throwable {
        ParameterExpression parameterExpression = new ParameterExpression("L\"r(,=BUu>r7|KV)X");
        assertEquals(2, parameterExpression.size());
    }

    @Test(timeout = 4000)
    public void testInvalidExpressionThrowsRuntimeException() throws Throwable {
        try {
            new ParameterExpression("(V5BD)wo7C47");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.builder.ParameterExpression", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidExpressionThrowsStringIndexOutOfBoundsException1() throws Throwable {
        try {
            new ParameterExpression(" ([}otejk;%91MIm8?:");
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testValidExpressionWithOneAttribute() throws Throwable {
        ParameterExpression parameterExpression = new ParameterExpression("%t~fZCvjt{");
        assertEquals(1, parameterExpression.size());
    }

    @Test(timeout = 4000)
    public void testNullExpressionThrowsNullPointerException() throws Throwable {
        try {
            new ParameterExpression((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.ibatis.builder.ParameterExpression", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidExpressionThrowsStringIndexOutOfBoundsException2() throws Throwable {
        try {
            new ParameterExpression("], b");
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testValidExpressionWithTwoAttributes2() throws Throwable {
        ParameterExpression parameterExpression = new ParameterExpression("wLz5Rg,g= +b1h^([5");
        assertEquals(2, parameterExpression.size());
    }

    @Test(timeout = 4000)
    public void testInvalidExpressionThrowsRuntimeException2() throws Throwable {
        try {
            new ParameterExpression("/;vUk0|B%J1\"7WA: ");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.builder.ParameterExpression", e);
        }
    }

    @Test(timeout = 4000)
    public void testValidExpressionWithTwoAttributes3() throws Throwable {
        ParameterExpression parameterExpression = new ParameterExpression("mRl: ;vM;");
        assertEquals(2, parameterExpression.size());
    }

    @Test(timeout = 4000)
    public void testInvalidExpressionThrowsStringIndexOutOfBoundsException3() throws Throwable {
        try {
            new ParameterExpression("( :s>(R5C/J>,K1");
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testValidExpressionWithOneAttribute2() throws Throwable {
        ParameterExpression parameterExpression = new ParameterExpression("(Vx5:BD)");
        assertEquals(1, parameterExpression.size());
    }

    @Test(timeout = 4000)
    public void testInvalidExpressionThrowsRuntimeException3() throws Throwable {
        try {
            new ParameterExpression("(;)$");
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.ibatis.builder.ParameterExpression", e);
        }
    }

    @Test(timeout = 4000)
    public void testValidExpressionWithTwoAttributes4() throws Throwable {
        ParameterExpression parameterExpression = new ParameterExpression(" ;|l, =R=kNH");
        assertEquals(2, parameterExpression.size());
    }
}