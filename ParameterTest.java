/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.reflect;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.lang.reflect.Method;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Parameter}.
 *
 * @author Ben Yu
 */
@NullUnmarked
@RunWith(JUnit4.class)
public class ParameterTest {

  /**
   * Verifies that Parameter's equals() and hashCode() are based on the declaring invokable and the
   * parameter's position within it.
   */
  @Test
  public void testEquals_basedOnInvokableAndPosition() throws Exception {
    // Arrange: Get invokables for two distinct methods to test equality rules.
    Method method1 = ParameterTest.class.getDeclaredMethod("someMethod", int.class, int.class);
    Invokable<?, ?> invokable1 = Invokable.from(method1);

    Method method2 =
        ParameterTest.class.getDeclaredMethod("anotherMethod", int.class, String.class);
    Invokable<?, ?> invokable2 = Invokable.from(method2);

    // Act & Assert: Use EqualsTester to verify the contract.
    new EqualsTester()
        // Group 1: A parameter is equal to another instance representing the same parameter.
        .addEqualityGroup(
            invokable1.getParameters().get(0), Invokable.from(method1).getParameters().get(0))
        // Group 2: A parameter is not equal to another parameter in the same method.
        .addEqualityGroup(invokable1.getParameters().get(1))
        // Group 3: A parameter is not equal to a parameter from a different method.
        .addEqualityGroup(invokable2.getParameters().get(0))
        .testEquals();
  }

  /**
   * Verifies that public methods of Parameter reject null arguments where contractually required.
   */
  @Test
  public void testPublicMethods_areNonNullByDefault() throws Exception {
    // Arrange: Skip test on older Android VMs where AnnotatedType is unavailable.
    // NullPointerTester would fail with NoClassDefFoundError when reflecting on getAnnotatedType().
    try {
      Class.forName("java.lang.reflect.AnnotatedType");
    } catch (ClassNotFoundException runningOnAndroid) {
      return; // Skip test
    }

    // Arrange: Get a representative parameter to test.
    Method method = ParameterTest.class.getDeclaredMethod("someMethod", int.class, int.class);
    Parameter param = Invokable.from(method).getParameters().get(0);

    // Act & Assert: Verify all public methods reject nulls where appropriate.
    new NullPointerTester().testAllPublicInstanceMethods(param);
  }

  // Helper method to provide parameters for testing.
  @SuppressWarnings("unused")
  private void someMethod(int i, int j) {}

  // Another helper method with a different signature.
  @SuppressWarnings("unused")
  private void anotherMethod(int i, String s) {}
}