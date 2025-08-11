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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class EndpointPair_ESTest extends EndpointPair_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCreateEndpointPairFromNetwork() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder = NetworkBuilder.undirected();
        MutableNetwork<Integer, Locale.Category> mutableNetwork = networkBuilder.build();
        Locale.Category localeCategory = Locale.Category.FORMAT;
        EndpointPair<Object> endpointPair = EndpointPair.of(
            (Network<?, ?>) mutableNetwork, networkBuilder, localeCategory
        );
        assertNotNull(endpointPair);
    }

    @Test(timeout = 4000)
    public void testCreateEndpointPairFromGraph() throws Throwable {
        GraphBuilder<Object> graphBuilder = GraphBuilder.undirected();
        StandardMutableGraph<Integer> standardMutableGraph = new StandardMutableGraph<>(graphBuilder);
        Integer node = 0;
        EndpointPair<Integer> endpointPair = EndpointPair.of(
            (Graph<?>) standardMutableGraph, node, node
        );
        assertNotNull(endpointPair);
    }

    @Test(timeout = 4000)
    public void testOrderedEndpointPairTarget() throws Throwable {
        Locale.Category localeCategory = Locale.Category.FORMAT;
        EndpointPair<Locale.Category> endpointPair = EndpointPair.ordered(localeCategory, localeCategory);
        Locale.Category target = endpointPair.target();
        assertSame(target, localeCategory);
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairNodeV() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair = EndpointPair.unordered(localeCategory, localeCategory);
        Locale.Category nodeV = endpointPair.nodeV();
        assertSame(localeCategory, nodeV);
    }

    @Test(timeout = 4000)
    public void testOrderedEndpointPairIsOrdered() throws Throwable {
        Integer node = -1;
        NetworkBuilder<Object, Object> networkBuilder = NetworkBuilder.directed();
        StandardValueGraph<Object, Integer> standardValueGraph = new StandardValueGraph<>(networkBuilder);
        ImmutableValueGraph<Object, Integer> immutableValueGraph = ImmutableValueGraph.copyOf(standardValueGraph);
        ImmutableGraph<Object> immutableGraph = new ImmutableGraph<>(immutableValueGraph);
        EndpointPair<Object> endpointPair = EndpointPair.of(
            (Graph<?>) immutableGraph, node, standardValueGraph
        );
        boolean isOrdered = endpointPair.isOrdered();
        assertTrue(isOrdered);
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairIsNotOrdered() throws Throwable {
        GraphBuilder<Object> graphBuilder = GraphBuilder.undirected();
        StandardMutableGraph<Integer> standardMutableGraph = new StandardMutableGraph<>(graphBuilder);
        Integer node = 0;
        EndpointPair<Integer> endpointPair = EndpointPair.of(
            (Graph<?>) standardMutableGraph, node, node
        );
        boolean isOrdered = endpointPair.isOrdered();
        assertFalse(isOrdered);
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairWithNullNodesThrowsException() throws Throwable {
        try {
            EndpointPair.unordered(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testOrderedEndpointPairWithNullNodesThrowsException() throws Throwable {
        try {
            EndpointPair.ordered(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testEndpointPairOfWithNullNetworkThrowsException() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        try {
            EndpointPair.of((Network<?, ?>) null, localeCategory, localeCategory);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.graph.EndpointPair", e);
        }
    }

    @Test(timeout = 4000)
    public void testEndpointPairOfWithNullNodesInNetworkThrowsException() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder = NetworkBuilder.directed();
        StandardNetwork<Object, Object> standardNetwork = new StandardNetwork<>(networkBuilder);
        try {
            EndpointPair.of(standardNetwork, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testEndpointPairOfWithNullGraphThrowsException() throws Throwable {
        Locale.Category localeCategory = Locale.Category.FORMAT;
        try {
            EndpointPair.of((Graph<?>) null, localeCategory, localeCategory);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.graph.EndpointPair", e);
        }
    }

    @Test(timeout = 4000)
    public void testEndpointPairOfWithNullNodesInGraphThrowsException() throws Throwable {
        NetworkBuilder<Object, Object> networkBuilder = NetworkBuilder.directed();
        StandardMutableNetwork<Object, Object> standardMutableNetwork = new StandardMutableNetwork<>(networkBuilder);
        Graph<Object> graph = standardMutableNetwork.asGraph();
        try {
            EndpointPair.of(graph, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testAdjacentNodeWithNullNodeThrowsException() throws Throwable {
        Integer node = -831;
        EndpointPair<Integer> endpointPair = EndpointPair.unordered(node, node);
        try {
            endpointPair.adjacentNode(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.graph.EndpointPair", e);
        }
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairNotEqualsToDifferentObject() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair = EndpointPair.unordered(localeCategory, localeCategory);
        boolean isEqual = endpointPair.equals(localeCategory);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairNotEqualsToDifferentEndpointPair() throws Throwable {
        Locale.Category localeCategory = Locale.Category.FORMAT;
        Object object = new Object();
        EndpointPair<Object> endpointPair1 = EndpointPair.unordered(localeCategory, object);
        EndpointPair<Object> endpointPair2 = EndpointPair.unordered(endpointPair1, localeCategory);
        boolean isEqual = endpointPair1.equals(endpointPair2);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairEqualsItself() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        Object object = new Object();
        EndpointPair<Object> endpointPair = EndpointPair.unordered(localeCategory, object);
        boolean isEqual = endpointPair.equals(endpointPair);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testOrderedEndpointPairNotEqualsToUnorderedEndpointPair() throws Throwable {
        Locale.Category localeCategory = Locale.Category.FORMAT;
        EndpointPair<Object> orderedPair = EndpointPair.ordered(localeCategory, localeCategory);
        EndpointPair<Object> unorderedPair = EndpointPair.unordered(localeCategory, orderedPair);
        boolean isEqual = orderedPair.equals(unorderedPair);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testOrderedEndpointPairEqualsItself() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair = EndpointPair.ordered(localeCategory, localeCategory);
        boolean isEqual = endpointPair.equals(endpointPair);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testOrderedEndpointPairEqualsSameOrderedPair() throws Throwable {
        Integer node = -1068;
        NetworkBuilder<Object, Object> networkBuilder = NetworkBuilder.directed();
        StandardNetwork<Object, Object> standardNetwork = new StandardNetwork<>(networkBuilder);
        EndpointPair<Object> endpointPair1 = EndpointPair.ordered(node, node);
        EndpointPair<Object> endpointPair2 = EndpointPair.of(standardNetwork, node, node);
        boolean isEqual = endpointPair1.equals(endpointPair2);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testAdjacentNodeWithInvalidNodeThrowsException() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair = EndpointPair.unordered(localeCategory, localeCategory);
        Locale.Category invalidNode = Locale.Category.FORMAT;
        try {
            endpointPair.adjacentNode(invalidNode);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.graph.EndpointPair", e);
        }
    }

    @Test(timeout = 4000)
    public void testAdjacentNodeReturnsCorrectNode() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair = EndpointPair.unordered(localeCategory, localeCategory);
        Locale.Category adjacentNode = endpointPair.adjacentNode(localeCategory);
        assertEquals(Locale.Category.DISPLAY, adjacentNode);
    }

    @Test(timeout = 4000)
    public void testIteratorIsNotNull() throws Throwable {
        Integer node = -1;
        EndpointPair<Integer> endpointPair = EndpointPair.unordered(node, node);
        UnmodifiableIterator<Integer> iterator = endpointPair.iterator();
        assertNotNull(iterator);
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairEqualsDifferentOrder() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        Integer node = 9;
        EndpointPair<Object> endpointPair1 = EndpointPair.unordered(localeCategory, node);
        EndpointPair<Object> endpointPair2 = EndpointPair.unordered(node, localeCategory);
        boolean isEqual = endpointPair1.equals(endpointPair2);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairSourceThrowsException() throws Throwable {
        Locale.Category localeCategory = Locale.Category.FORMAT;
        EndpointPair<Locale.Category> orderedPair = EndpointPair.ordered(localeCategory, localeCategory);
        Object source = orderedPair.source();
        EndpointPair<Object> unorderedPair = EndpointPair.unordered(source, orderedPair);
        try {
            unorderedPair.source();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("com.google.common.graph.EndpointPair$Unordered", e);
        }
    }

    @Test(timeout = 4000)
    public void testIncidentNodesThrowsExceptionForNonExistentEdge() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair = EndpointPair.unordered(localeCategory, localeCategory);
        NetworkBuilder<Object, Object> networkBuilder = NetworkBuilder.directed();
        StandardNetwork<Object, Object> standardNetwork = new StandardNetwork<>(networkBuilder);
        try {
            standardNetwork.incidentNodes(endpointPair);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.graph.StandardNetwork", e);
        }
    }

    @Test(timeout = 4000)
    public void testUnorderedEndpointPairTargetThrowsException() throws Throwable {
        Locale.Category localeCategory = Locale.Category.DISPLAY;
        EndpointPair<Locale.Category> endpointPair = EndpointPair.unordered(localeCategory, localeCategory);
        try {
            endpointPair.target();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("com.google.common.graph.EndpointPair$Unordered", e);
        }
    }
}