package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AllPredicate;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NullIsTruePredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class OrderedProperties_ESTestTest20 extends OrderedProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        OrderedProperties orderedProperties0 = new OrderedProperties();
        Integer integer0 = new Integer(95);
        Predicate<Object>[] predicateArray0 = (Predicate<Object>[]) Array.newInstance(Predicate.class, 5);
        Predicate<Object> predicate0 = NullPredicate.nullPredicate();
        predicateArray0[0] = predicate0;
        NonePredicate<Object> nonePredicate0 = new NonePredicate<Object>(predicateArray0);
        predicateArray0[1] = (Predicate<Object>) nonePredicate0;
        predicateArray0[4] = predicateArray0[1];
        IfTransformer<Object, Integer> ifTransformer0 = new IfTransformer<Object, Integer>(predicateArray0[4], (Transformer<? super Object, ? extends Integer>) null, (Transformer<? super Object, ? extends Integer>) null);
        // Undeclared exception!
        orderedProperties0.computeIfAbsent(integer0, ifTransformer0);
    }
}
