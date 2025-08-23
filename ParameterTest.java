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
import java.util.ArrayList;
import java.util.List;
import org.junit.Assume;
import org.junit.Test;

/**
 * Tests for {@link Parameter}.
 *
 * This version uses a small, dedicated fixture class so it's obvious which parameters are exercised,
 * and it documents why one test is conditionally skipped on Android VMs.
 */
public final class ParameterTest {

  /**
   * Verifies that all public instance methods on Parameter throw NullPointerException where
   * appropriate. We skip this check on Android, where java.lang.reflect.AnnotatedType is absent and
   * Class.getDeclaredMethods() would cause NoClassDefFoundError when probing Parameter.
   */
  @Test
  public void publicInstanceMethods_nullChecks() {
    Assume.assumeTrue("Skipping on Android: AnnotatedType not available", isAnnotatedTypeAvailable());

    for (Parameter parameter : allFixtureParameters()) {
      new NullPointerTester().testAllPublicInstanceMethods(parameter);
    }
  }

  /**
   * Verifies equals/hashCode:
   * - The same logical parameter (same declaring Invokable and same position) compares equal across
   *   separate retrievals.
   * - Different parameters (different method or different position) are not equal.
   */
  @Test
  public void equalsAndHashCode_byDeclarationAndPosition() {
    List<Method> methods = fixtureMethods();

    EqualsTester tester = new EqualsTester();

    for (Method method : methods) {
      // Build two fresh views so we can assert equality across independent retrievals.
      List<Parameter> firstView = Invokable.from(method).getParameters();
      List<Parameter> secondView = Invokable.from(method).getParameters();

      for (int i = 0; i < firstView.size(); i++) {
        tester.addEqualityGroup(firstView.get(i), secondView.get(i));
      }
    }

    tester.testEquals();
  }

  // ----- Test fixture and helpers -----

  /**
   * A tiny, purpose-built fixture so it's obvious which parameters are under test.
   */
  private static final class Fixture {
    @SuppressWarnings("unused")
    private static void twoInts(int first, int second) {}

    @SuppressWarnings("unused")
    private static void intAndString(int count, String name) {}
  }

  private static boolean isAnnotatedTypeAvailable() {
    try {
      Class.forName("java.lang.reflect.AnnotatedType");
      return true;
    } catch (ClassNotFoundException e) {
      return false; // Android VM (older), where AnnotatedType isn't present.
    }
  }

  /** Returns the two fixture methods in a stable, explicit order. */
  private static List<Method> fixtureMethods() {
    List<Method> methods = new ArrayList<>(2);
    methods.add(findMethod(Fixture.class, "twoInts", int.class, int.class));
    methods.add(findMethod(Fixture.class, "intAndString", int.class, String.class));
    return methods;
  }

  /** Returns all Parameter instances from the fixture methods. */
  private static List<Parameter> allFixtureParameters() {
    List<Parameter> params = new ArrayList<>();
    for (Method method : fixtureMethods()) {
      params.addAll(Invokable.from(method).getParameters());
    }
    return params;
  }

  private static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
    try {
      Method method = clazz.getDeclaredMethod(name, parameterTypes);
      method.setAccessible(true);
      return method;
    } catch (NoSuchMethodException e) {
      throw new AssertionError("Fixture method not found: " + clazz.getName() + "#" + name, e);
    }
  }
}