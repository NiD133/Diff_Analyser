package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.Network;
import com.google.common.graph.NetworkBuilder;
import com.google.common.graph.StandardMutableNetwork;
import com.google.common.graph.StandardNetwork;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the ImmutableNetwork class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ImmutableNetwork_ESTest extends ImmutableNetwork_ESTest_scaffolding {

    /**
     * Test that adding an edge to an ImmutableNetwork.Builder returns a non-null builder.
     */
    @Test(timeout = 4000)
    public void testAddEdgeToBuilder() throws Throwable {
        NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
        StandardNetwork<Integer, Integer> standardNetwork = new StandardNetwork<>(directedBuilder);
        NetworkBuilder<Integer, Integer> networkBuilder = NetworkBuilder.from(standardNetwork);
        ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = new ImmutableNetwork.Builder<>(networkBuilder);

        Integer node1 = 1029;
        Integer node2 = 1;
        ImmutableNetwork.Builder<Integer, Integer> resultBuilder = immutableBuilder.addEdge(node1, node2, node2);

        assertNotNull(resultBuilder);
    }

    /**
     * Test that copying a null Network throws a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testCopyOfNullNetwork() throws Throwable {
        try {
            ImmutableNetwork.copyOf((Network<Integer, Integer>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.graph.NetworkBuilder", e);
        }
    }

    /**
     * Test that copying a null ImmutableNetwork throws a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testCopyOfNullImmutableNetwork() throws Throwable {
        try {
            ImmutableNetwork.copyOf((ImmutableNetwork<Object, Integer>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    /**
     * Test creating an ImmutableNetwork with parallel edges allowed.
     */
    @Test(timeout = 4000)
    public void testImmutableNetworkWithParallelEdges() throws Throwable {
        NetworkBuilder<Object, Object> undirectedBuilder = NetworkBuilder.undirected();
        StandardNetwork<Integer, Comparable<Integer>> standardNetwork = new StandardNetwork<>(undirectedBuilder);
        NetworkBuilder<Integer, Comparable<Integer>> networkBuilder = NetworkBuilder.from(standardNetwork);
        networkBuilder.allowsParallelEdges(true);

        StandardNetwork<Integer, Integer> standardNetworkWithParallelEdges = new StandardNetwork<>(networkBuilder);
        ImmutableNetwork<Integer, Integer> initialImmutableNetwork = ImmutableNetwork.copyOf(standardNetworkWithParallelEdges);
        NetworkBuilder<Integer, Integer> builderFromImmutable = NetworkBuilder.from(initialImmutableNetwork);
        ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = builderFromImmutable.immutable();

        Integer newNode = 949;
        immutableBuilder.addNode(newNode);
        ImmutableNetwork<Integer, Integer> newImmutableNetwork = immutableBuilder.build();

        assertNotSame(initialImmutableNetwork, newImmutableNetwork);
    }

    /**
     * Test creating a directed ImmutableNetwork with parallel edges allowed.
     */
    @Test(timeout = 4000)
    public void testDirectedImmutableNetworkWithParallelEdges() throws Throwable {
        NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
        directedBuilder.allowsParallelEdges(true);
        StandardNetwork<Integer, Integer> standardNetwork = new StandardNetwork<>(directedBuilder);

        ImmutableNetwork<Integer, Integer> initialImmutableNetwork = ImmutableNetwork.copyOf(standardNetwork);
        NetworkBuilder<Integer, Integer> builderFromImmutable = NetworkBuilder.from(initialImmutableNetwork);
        ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = builderFromImmutable.immutable();

        Integer newNode = -967;
        ImmutableNetwork.Builder<Integer, Integer> resultBuilder = immutableBuilder.addNode(newNode);
        ImmutableNetwork<Integer, Integer> newImmutableNetwork = resultBuilder.build();

        assertNotSame(initialImmutableNetwork, newImmutableNetwork);
    }

    /**
     * Test creating an ImmutableNetwork from a StandardMutableNetwork.
     */
    @Test(timeout = 4000)
    public void testImmutableNetworkFromMutableNetwork() throws Throwable {
        NetworkBuilder<Object, Object> undirectedBuilder = NetworkBuilder.undirected();
        StandardMutableNetwork<Integer, Integer> mutableNetwork = new StandardMutableNetwork<>(undirectedBuilder);

        Integer node1 = -405;
        NetworkBuilder<Integer, Integer> networkBuilder = NetworkBuilder.from(mutableNetwork);
        ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = networkBuilder.immutable();

        Integer node2 = -1124;
        EndpointPair<Integer> endpointPair = EndpointPair.unordered(node2, node1);
        ImmutableNetwork.Builder<Integer, Integer> resultBuilder = immutableBuilder.addEdge(endpointPair, node2);

        ImmutableNetwork<Integer, Integer> immutableNetwork = resultBuilder.build();
        assertNotNull(immutableNetwork);
    }

    /**
     * Test that copying an ImmutableNetwork returns the same instance.
     */
    @Test(timeout = 4000)
    public void testCopyOfImmutableNetwork() throws Throwable {
        NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
        StandardNetwork<Integer, Integer> standardNetwork = new StandardNetwork<>(directedBuilder);

        ImmutableNetwork<Integer, Integer> immutableNetwork = ImmutableNetwork.copyOf(standardNetwork);
        ImmutableNetwork<Integer, Integer> copiedImmutableNetwork = ImmutableNetwork.copyOf(immutableNetwork);

        assertSame(immutableNetwork, copiedImmutableNetwork);
    }

    /**
     * Test that copying an ImmutableNetwork from a StandardMutableNetwork returns the same instance.
     */
    @Test(timeout = 4000)
    public void testCopyOfImmutableNetworkFromMutable() throws Throwable {
        NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
        StandardMutableNetwork<Object, Object> mutableNetwork = new StandardMutableNetwork<>(directedBuilder);

        ImmutableNetwork<Object, Object> immutableNetwork = ImmutableNetwork.copyOf(mutableNetwork);
        ImmutableNetwork<Object, Object> copiedImmutableNetwork = ImmutableNetwork.copyOf(immutableNetwork);

        assertSame(immutableNetwork, copiedImmutableNetwork);
    }

    /**
     * Test converting an ImmutableNetwork to an ImmutableGraph.
     */
    @Test(timeout = 4000)
    public void testImmutableNetworkToImmutableGraph() throws Throwable {
        NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
        StandardNetwork<Integer, Integer> standardNetwork = new StandardNetwork<>(directedBuilder);

        ImmutableNetwork<Integer, Integer> immutableNetwork = ImmutableNetwork.copyOf(standardNetwork);
        ImmutableGraph<Integer> immutableGraph = immutableNetwork.asGraph();

        assertNotNull(immutableGraph);
    }

    /**
     * Test creating an ImmutableNetwork by adding a node.
     */
    @Test(timeout = 4000)
    public void testAddNodeToImmutableNetwork() throws Throwable {
        NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
        StandardNetwork<Integer, Integer> standardNetwork = new StandardNetwork<>(directedBuilder);

        NetworkBuilder<Integer, Integer> networkBuilder = NetworkBuilder.from(standardNetwork);
        ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = networkBuilder.immutable();

        Integer newNode = -967;
        ImmutableNetwork.Builder<Integer, Integer> resultBuilder = immutableBuilder.addNode(newNode);
        ImmutableNetwork<Integer, Integer> immutableNetwork = resultBuilder.build();

        assertNotNull(immutableNetwork);
    }
}