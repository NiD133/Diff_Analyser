package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * This test focuses on the structural integrity and design contract of the
 * {@link DateTimeComparator} class. It verifies class and constructor modifiers
 * to ensure it is used as intended (e.g., via factory methods).
 */
public class DateTimeComparatorStructureTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(DateTimeComparatorStructureTest.class);
    }

    /**
     * Verifies the essential class and constructor modifiers of DateTimeComparator.
     * <p>
     * It checks that the class is public and not final, which allows it to be
     * used and extended. It also ensures the constructor is protected, enforcing
     * the use of factory methods for instantiation.
     */
    public void testClassStructure() {
        Class<?> clazz = DateTimeComparator.class;

        // Ensure the class is public and not final
        assertTrue("DateTimeComparator must be a public class.", Modifier.isPublic(clazz.getModifiers()));
        assertFalse("DateTimeComparator must not be a final class.", Modifier.isFinal(clazz.getModifiers()));

        // Ensure there is exactly one constructor and it is protected
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        assertEquals("There should be exactly one constructor.", 1, constructors.length);
        assertTrue("The constructor must be protected.", Modifier.isProtected(constructors[0].getModifiers()));
    }
}