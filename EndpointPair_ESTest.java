package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.Network;
import com.google.common.graph.NetworkBuilder;
import com.google.common.graph.StandardMutableGraph;
import com.google.common.graph.StandardMutableNetwork;
import com.google.common.graph.StandardNetwork;
import com.google.common.graph.StandardValueGraph;
import com.google.common.graph.ValueGraph;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class EndpointPair_ESTest extends EndpointPair_ESTest_scaffolding {

    // Constants for reused objects
    private static final Locale.Category DISPLAY = Locale.Category.DISPLAY;
    private static final Locale.Category FORMAT = Locale.Category.FORMAT;
    private static final Integer NEGATIVE_INT = -1;
    private static final Integer ZERO = 0;

    // Tests for EndpointPair creation
    @Test(timeout = 4000)
    public void testCreationFromNetwork() {
        NetworkBuilder<Object, Object> builder = NetworkBuilder.undirected();
        MutableNetwork<Integer, Locale.Category> network = builder.build();
        EndpointPair<Object> pair = EndpointPair.of(network, builder, FORMAT);
        assertNotNull(pair);
    }

    @Test(timeout = 4000)
    public void testCreationFromGraph() {
        GraphBuilder<Object> builder = GraphBuilder.undirected();
        StandardMutableGraph<Integer> graph = new StandardMutableGraph<>(builder);
        EndpointPair<Integer> pair = EndpointPair.of(graph, ZERO, ZERO);
        assertNotNull(pair);
    }

    // Tests for ordered pairs
    @Test(timeout = 4000)
    public void testOrderedPairSourceAndTarget() {
        EndpointPair<Locale.Category> pair = EndpointPair.ordered(DISPLAY, DISPLAY);
        assertSame(DISPLAY, pair.source());
        assertSame(DISPLAY, pair.target());
    }

    @Test(timeout = 4000)
    public void testOrderedPairIsOrdered() {
        EndpointPair<Locale.Category> pair = EndpointPair.ordered(DISPLAY, FORMAT);
        assertTrue(pair.isOrdered());
    }

    @Test(timeout = 4000)
    public void testOrderedPairAdjacentNode() {
        EndpointPair<Locale.Category> pair = EndpointPair.ordered(DISPLAY, FORMAT);
        assertSame(DISPLAY, pair.adjacentNode(FORMAT));
    }

    // Tests for unordered pairs
    @Test(timeout = 4000)
    public void testUnorderedPairNodeV() {
        EndpointPair<Locale.Category> pair = EndpointPair.unordered(DISPLAY, DISPLAY);
        assertSame(DISPLAY, pair.nodeV());
    }

    @Test(timeout = 4000)
    public void testUnorderedPairIsNotOrdered() {
        GraphBuilder<Object> builder = GraphBuilder.undirected();
        StandardMutableGraph<Integer> graph = new StandardMutableGraph<>(builder);
        EndpointPair<Integer> pair = EndpointPair.of(graph, ZERO, ZERO);
        assertFalse(pair.isOrdered());
    }

    @Test(timeout = 4000)
    public void testUnorderedPairAdjacentNode() {
        EndpointPair<Locale.Category> pair = EndpointPair.unordered(DISPLAY, DISPLAY);
        assertSame(DISPLAY, pair.adjacentNode(DISPLAY));
    }

    // Tests for equality and hashcode
    @Test(timeout = 4000)
    public void testEqualsSameUnorderedPairDifferentOrder() {
        EndpointPair<Object> pair1 = EndpointPair.unordered(DISPLAY, ZERO);
        EndpointPair<Object> pair2 = EndpointPair.unordered(ZERO, DISPLAY);
        assertEquals(pair1, pair2);
    }

    @Test(timeout = 4000)
    public void testEqualsSameOrderedPair() {
        EndpointPair<Object> pair1 = EndpointPair.ordered(NEGATIVE_INT, NEGATIVE_INT);
        EndpointPair<Object> pair2 = EndpointPair.ordered(NEGATIVE_INT, NEGATIVE_INT);
        assertEquals(pair1, pair2);
    }

    @Test(timeout = 4000)
    public void testNotEqualsOrderedVsUnordered() {
        EndpointPair<Object> ordered = EndpointPair.ordered(DISPLAY, DISPLAY);
        EndpointPair<Object> unordered = EndpointPair.unordered(DISPLAY, DISPLAY);
        assertNotEquals(ordered, unordered);
    }

    @Test(timeout = 4000)
    public void testNotEqualsDifferentTypes() {
        EndpointPair<Locale.Category> pair = EndpointPair.unordered(DISPLAY, DISPLAY);
        assertNotEquals(pair, DISPLAY);
    }

    // Tests for exception scenarios
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testUnorderedPairCreationWithNullThrowsNPE() {
        EndpointPair.unordered(null, null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testOrderedPairCreationWithNullThrowsNPE() {
        EndpointPair.ordered(null, null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testAdjacentNodeWithNullThrowsNPE() {
        EndpointPair<Integer> pair = EndpointPair.unordered(NEGATIVE_INT, NEGATIVE_INT);
        pair.adjacentNode(null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testAdjacentNodeInvalidNodeThrowsIAE() {
        EndpointPair<Locale.Category> pair = EndpointPair.unordered(DISPLAY, DISPLAY);
        pair.adjacentNode(FORMAT);
    }

    @Test(timeout = 4000, expected = UnsupportedOperationException.class)
    public void testSourceOnUnorderedPairThrowsUSE() {
        EndpointPair<Locale.Category> pair = EndpointPair.unordered(DISPLAY, DISPLAY);
        pair.source();
    }

    @Test(timeout = 4000, expected = UnsupportedOperationException.class)
    public void testTargetOnUnorderedPairThrowsUSE() {
        EndpointPair<Locale.Category> pair = EndpointPair.unordered(DISPLAY, DISPLAY);
        pair.target();
    }

    // Additional edge case tests
    @Test(timeout = 4000)
    public void testIterator() {
        EndpointPair<Integer> pair = EndpointPair.unordered(NEGATIVE_INT, NEGATIVE_INT);
        UnmodifiableIterator<Integer> iterator = pair.iterator();
        assertNotNull(iterator);
    }

    @Test(timeout = 4000)
    public void testEqualsReflexive() {
        EndpointPair<Locale.Category> pair = EndpointPair.ordered(DISPLAY, DISPLAY);
        assertEquals(pair, pair);
    }
}