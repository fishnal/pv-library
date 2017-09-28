package pv3199.util;

import pv3199.math.structures.RealMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract graph of some generic type, T, that is similar to key-value mapping
 * structures, but instead relies on the connections between the elements
 * plotted, or inserted, on the graph.
 *
 * @param <T> the type of vertices that this Graph holds
 * @author Vishal Patel
 */
public abstract class Graph<T> implements java.io.Serializable {
	/**
	 * List of vertices in the grid.
	 */
	protected List<Vertex> vertices;
	/**
	 * The adjacency matrix representing the path lengths of 1 for every vector
	 * pair in the graph.
	 */
	private RealMatrix adjacencyMatrix;
	
	/**
	 * Super constructor for children classes. Instantiates the vertices and
	 * adjacency matrix.
	 */
	public Graph() {
		vertices = new ArrayList<>();
		adjacencyMatrix = new RealMatrix(0, 0);
	}
	
	/**
	 * The index of this vertex within the vertices list, {@link #vertices}
	 *
	 * @param vertex - the vertex to look for.
	 * @return -1 if this vertex is not on the graph or if the vertex is null; a
	 * non-negative index where this vertex is located within
	 * {@link #vertices} otherwise.
	 */
	protected final int indexOf(T vertex) {
		if (vertex == null) {
			return -1;
		}
		
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).value.equals(vertex)) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Gets the vertex associated with this vertex value
	 *
	 * @param vertexValue - the specified vertex value to look for
	 * @return null if this vertex value is not associated with any vertex; a
	 * Vertex object otherwise.
	 */
	protected final Vertex get(T vertexValue) {
		int ind = indexOf(vertexValue);
		
		return ind == -1 ? null : vertices.get(ind);
	}
	
	/**
	 * Determines whether a vertex with this value is plotted on the graph.
	 *
	 * @param value - the value for said vertex.
	 * @return true if there exists such a vertex with the specified value on
	 * the graph; false otherwise.
	 */
	public final boolean isPlotted(T value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Adds a vertex to the graph along with a set of vertices, if any, that
	 * this vertex points to. If this vertex points to a vertex (or vertices)
	 * that does not exist on the graph, then it will be added as a new vertex
	 * with no links and then this vertex will point to this newly created
	 * vertex.
	 *
	 * @param vertex - the vertex to add.
	 * @param links  - the set of vertices that <code>vertex</code> will point to.
	 * @return - false if the vertex to add is null; true otherwise indicating a
	 * successful addition of the vertex.
	 */
	public abstract boolean add(T vertex, T... links);
	
	/**
	 * Updates the adjacency matrix. Should be called directly after one
	 * modification or a series of modifications are applied to the grid.
	 */
	protected void updateAdjacencyMatrix() {
		vertices.sort(null);
		
		int len = vertices.size();
		
		adjacencyMatrix = new RealMatrix(len, len);
		
		for (int r = 0; r < len; r++) {
			Vertex vertex = vertices.get(r);
			List<Vertex> links = vertex.links;
			
			for (int c = 0; c < len; c++) {
				adjacencyMatrix.setValue(r, c, links.contains(vertices.get(c)) ? 1 : 0);
			}
		}
	}
	
	/**
	 * @return the integer adjacency matrix of path lengths of 1 for every
	 * vector pair.
	 */
	public final RealMatrix getAdjacencyMatrix() {
		updateAdjacencyMatrix();
		return adjacencyMatrix.clone();
	}
	
	/**
	 * Removes a vertex from the graph and in the process removes all pointers
	 * to it. If a graph contains the vertices {A, B, C} and the edges {AB, AC,
	 * BC, CA, CB} and the vertex {A} is removed from the graph, then the
	 * remaining edges are {BC, CB}
	 *
	 * @param vertex - the vertex to remove.
	 * @return false if the vertex is null or is not in the graph; true
	 * otherwise.
	 */
	public abstract boolean remove(T vertex);
	
	/**
	 * Sets the links for a certain vertex. All previous links for this vertex
	 * are erased and replaced with the links specified by the parameter
	 * <code>links</code>.
	 *
	 * @param vertex - the vertex's links to change
	 * @param links  - the new links
	 * @return false if the vertex is null or is not plotted on the graph; true
	 * otherwise.
	 */
	public abstract boolean set(T vertex, T... links);
	
	/**
	 * Returns the amount of different paths of length <code>n</code> within the
	 * graph.
	 *
	 * @param n - the path length to look for.
	 * @return -1 if an error is caught; the amount of different paths of a
	 * given length otherwise.
	 */
	public abstract long pathLength(int n);
	
	/**
	 * Returns whether or not a vertex has a path length <code>n</code> to
	 * another vertex.
	 *
	 * @param vertex - the vertex to traverse from.
	 * @param n      - the path length
	 * @return true if this vertex has a certain path length to another vertex.
	 */
	public abstract boolean pathLength(T vertex, int n);
	
	/**
	 * Determines whether a vertex, <code>originVertex</code>, has a path length
	 * of <code>n</code> to another vertex, <code>targetVertex</code>.
	 *
	 * @param originVertex - the first/origin vertex
	 * @param targetVertex - the second/target vertex
	 * @param n            - the path length
	 * @return false if:
	 * <ul>
	 * <li>the origin vertex or the target vertex is null</li>
	 * <li>the origin vertex or the target vertex is not plotted on the
	 * graph</li>
	 * <li>the path length is less than 1, in which case:
	 * <ul>
	 * <li>if the path length is 0 and the origin vertex is NOT the same
	 * as the target vertex</li>
	 * <li>or the path length is negative</li>
	 * </ul>
	 * </li>
	 * <li>there is no path of the desired length from the origin vertex
	 * to the target vertex</li>
	 * </ul>
	 */
	public abstract boolean pathLength(T originVertex, T targetVertex, int n);
	
	/**
	 * @return the number of vertices in the graph.
	 */
	public final int vertexCount() {
		return vertices.size();
	}
	
	/**
	 * Checks the equality of the vertices.
	 */
	@Override
	public boolean equals(Object obj) {
		try {
			Graph<T> other = (Graph<T>) obj;
			
			return vertices.equals(other.vertices);
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Returns the edges of the graph.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		for (Vertex v : vertices) {
			s.append(v).append('\n');
		}
		
		return s.toString().trim();
	}
	
	/**
	 * Vertex object representing the vertices of the graph. Each vertex can
	 * point to another vertex, but not necessarily in a directed manner.
	 *
	 * @author Vishal Patel
	 */
	protected abstract class Vertex implements Comparable<Vertex> {
		/**
		 * The value of this vertex.
		 */
		protected T value;
		
		/**
		 * The vertices that this vertex points to.
		 */
		protected List<Vertex> links;
		
		/**
		 * Constructs a vertex from a given value.
		 *
		 * @param value - value for the vertex.
		 */
		Vertex(T value) {
			this.value = value;
			links = new ArrayList<>();
		}
		
		/**
		 * Constructs a vertex from a given value and a set of vertices that
		 * this vertex points to.
		 *
		 * @param value - the value for the vertex.
		 * @param links - the set of pointed vertices.
		 */
		Vertex(T value, List<T> links) {
			this(value);
			setLinks(links);
		}
		
		/**
		 * Changes the vertices that this vertex points to.
		 *
		 * @param newLinks - the set of new vertices to point to.
		 * @return - true if the changes were made; false otherwise.
		 */
		protected abstract boolean setLinks(List<T> newLinks);
		
		/**
		 * Adds additional vertices that this vertex points to. If any of the
		 * vertices that are to be added are already pointed to by this vertex,
		 * then nothing changes.
		 *
		 * @param additionalLinks - the additional set of vertices to point to.
		 * @return true if the changes could be made (regardless if vertices
		 * were added); false otherwise
		 */
		protected abstract boolean addLinks(List<T> additionalLinks);
		
		/**
		 * Checks if this vertex is included in the list of vertices that this
		 * vertex points to.
		 *
		 * @return true if this vertex points to itself; false otherwise.
		 */
		protected final boolean selfPoints() {
			for (Vertex v : links) {
				if (v == this) {
					return true;
				}
			}
			
			return false;
		}
		
		/**
		 * Compares the hash codes of the values of the vertices.
		 */
		@Override
		public int compareTo(Vertex o) {
			return value.hashCode() - o.value.hashCode();
		}
		
		/**
		 * Compares the condition of each vertex pointing to itself, the values
		 * of each vertex, and the vertices that these vertices point to.
		 */
		@Override
		public boolean equals(Object obj) {
			try {
				Vertex v = (Vertex) obj;
				
				return selfPoints() == v.selfPoints() && links.equals(v.links) && value.equals(v.value);
			} catch (Exception e) {
				return false;
			}
		}
		
		/**
		 * @return string of <code>value</code>.
		 */
		@Override
		public String toString() {
			return value.toString();
		}
	}
}
