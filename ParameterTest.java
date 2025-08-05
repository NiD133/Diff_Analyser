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

import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link Parameter}.
 *
 * @author Ben Yu
 */
@NullUnmarked
public class ParameterTest extends TestCase {

  /**
   * Tests that all public methods of {@link Parameter} pass {@link NullPointerTester} checks.
   * Skipped on Android VMs due to missing {@code AnnotatedType} class.
   */
  public void testNullPointerTesterOnPublicMethods() {
    if (isRunningInAndroidVm()) {
      // Skip test in Android VM due to missing AnnotatedType class
      return;
    }
    for (Parameter param : getAllParameters()) {
      new NullPointerTester().testAllPublicInstanceMethods(param);
    }
  }

  /**
   * Tests the {@link Parameter#equals} and {@link Parameter#hashCode} contract.
   * Each parameter is added as its own equality group since they are all distinct.
   */
  public void testEqualsContract() {
    EqualsTester tester = new EqualsTester();
    for (Parameter param : getAllParameters()) {
      tester.addEqualityGroup(param);
    }
    tester.testEquals();
  }

  /**
   * Checks if we're running in an Android VM by testing for the presence of
   * {@code java.lang.reflect.AnnotatedType}.
   */
  private boolean isRunningInAndroidVm() {
    try {
      Class.forName("java.lang.reflect.AnnotatedType");
      return false;
    } catch (ClassNotFoundException e) {
      return true;
    }
  }

  /**
   * Collects all parameters from all methods in this test class.
   * Used to provide parameters for test cases.
   */
  private ImmutableList<Parameter> getAllParameters() {
    List<Parameter> parameters = new ArrayList<>();
    for (Method method : getClass().getDeclaredMethods()) {
      parameters.addAll(Invokable.from(method).getParameters());
    }
    return ImmutableList.copyOf(parameters);
  }

  // Methods below are used to generate test parameters
  // Each method has distinct parameters for testing

  @SuppressWarnings("unused")
  private void someMethod(int i, int j) {}

  @SuppressWarnings("unused")
  private void anotherMethod(int i, String s) {}
}