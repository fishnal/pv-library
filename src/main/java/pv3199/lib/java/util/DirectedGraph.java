package pv3199.lib.java.util;

import pv3199.lib.java.math.structures.RealMatrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Directed graph of vertices. Various graph-theory properties and calculations are provided, such as cycles
 * and path lengths.
 *
 * @param <T> - The type of vertex that the graph will hold.
 * @author Vishal Patel
 */
public final class DirectedGraph<T> extends Graph<T> {
	private boolean changesMade = false;
	/**
	 * Used for recursive method {@link #cycle0}. Indicates how many stacks of
	 * this method exist within this instance.
	 */
	private int cycle0Depth;
	
	/**
	 * Constructs a DirectedGraph object, initializing the vertices in this
	 * graph to an empty list and the adjacency matrix to a 0x0 int array.
	 */
	public DirectedGraph() {
		super();
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
	@Override
	public boolean add(T vertex, T... links) {
		if (links == null) return add0(vertex, null);
		
		ArrayList<T> linksList = new ArrayList<>(java.util.Arrays.asList(links));
		return add0(vertex, linksList);
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
	private boolean add0(T vertex, ArrayList<T> links) {
		if (vertex == null) {
			return false;
		} else if (isPlotted(vertex)) {
			return vertices.get(indexOf(vertex)).addLinks(links);
		}
		// TODO Needs to be reviewed
		// for (int i = 0; i < vertices.size(); i++)
		// if (vertices.get(i).value.equals(vertex))
		// return vertices.get(i).setLinks(links);
		
		Vertex vertex0;
		
		if (links != null) {
			vertex0 = new DirectedVertex(vertex, links);
		} else {
			vertex0 = new DirectedVertex(vertex);
		}
		
		vertices.add(vertex0);
		
		changesMade = true;
		
		return true;
	}
	
	@Override
	protected void updateAdjacencyMatrix() {
		if (!changesMade) {
			return;
		}
		
		changesMade = false;
		
		super.updateAdjacencyMatrix();
	}
	
	@Override
	public boolean remove(T vertex) {
		if (vertex == null) {
			return false;
		}
		
		boolean removed = false;
		
		int vertexInd = -1;
		
		for (int i = 0; i < vertices.size(); i++) {
			if (vertices.get(i).value.equals(vertex)) {
				vertexInd = i;
			}
			for (int j = 0; j < vertices.get(i).links.size(); j++) {
				if (vertices.get(i).links.get(j).value.equals(vertex)) {
					vertices.get(i).links.remove(j);
					removed = true;
				}
			}
		}
		
		vertices.remove(vertexInd);
		
		changesMade = true;
		
		return removed;
	}
	
	@Override
	public boolean set(T vertex, T... links) {
		if (vertex == null) {
			return false;
		}
		
		Vertex vertex0 = get(vertex);
		
		if (vertex0 == null) {
			return false;
		}
		
		ArrayList<T> linksList = new ArrayList<>(java.util.Arrays.asList(links));
		
		return vertex0.setLinks(linksList) && (changesMade = true);
		
	}
	
	/**
	 * <p>
	 * Paths are NOT simple and all paths are based upon the links that each
	 * vertex has. Traveling back to the same node requires either a two-way
	 * link between that node and another node or a one-way link between another
	 * node to that node.
	 * </p>
	 */
	@Override
	public long pathLength(int n) {
		RealMatrix matrix = getAdjacencyMatrix();
		
		matrix = matrix.pow(n);
		
		long sum = 0;
		
		for (int r = 0; r < matrix.height; r++) {
			for (int c = 0; c < matrix.width; c++) {
				sum += matrix.getValue(r, c).longValue();
			}
		}
		
		return sum;
	}
	
	/**
	 * <p>
	 * Paths are NOT simple and all paths from <code>vertex</code> are based
	 * upon the links that <code>vertex</code> has. Traveling back to
	 * <code>vertex</code> requires either a two-way link between
	 * <code>vertex</code> and another node in the graph or a one-way link
	 * between another node* to <code>vertex</code>.
	 * </p>
	 */
	@Override
	public boolean pathLength(T vertex, int n) {
		if (vertex == null || n < 0) {
			return false;
		}
		
		int ind = indexOf(vertex);
		
		if (ind < 0) {
			return false;
		}
		
		RealMatrix matrix = getAdjacencyMatrix();
		
		if (n > 1) {
			matrix = matrix.pow(n);
		}
		
		for (int c = 0; c < matrix.width; c++) {
			if (matrix.getValue(ind, c).intValue() == n) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * <p>
	 * Paths are NOT simple and all paths from <code>originVertex</code> to
	 * <code>targetVertex</code> are based upon the adjacency matrix of the
	 * graph raised to the power of <code>n</code>:
	 * </p>
	 * <blockquote> <code>
	 * (M<sub>g</sub>)<sup>n</sup>
	 * </code> </blockquote>
	 * <p>
	 * Where <code>g</code> represents this graph, <code>M</code> represents the
	 * adjacency matrix for this graph, and <code>n</code> is the path length to
	 * find.
	 */
	@Override
	public boolean pathLength(T originVertex, T targetVertex, int n) {
		if (originVertex == null || targetVertex == null || n < 0) {
			return false;
		}
		
		int originInd = indexOf(originVertex);
		int targetInd = indexOf(targetVertex);
		
		if (originInd < 0 || targetInd < 0) {
			return false;
		}
		
		Vertex origin = vertices.get(originInd);
		Vertex target = vertices.get(targetInd);
		
		if (n == 0 && origin != target) {
			return false;
		}
		
		RealMatrix matrix = getAdjacencyMatrix();
		matrix = matrix.pow(n);
		
		return matrix.getValue(originInd, targetInd) > 0;
	}
	
	/**
	 * Determines all different cycles for a certain vertex on the graph.
	 *
	 * @param vertex - the vertex to cycle from.
	 * @return null if the specified vertex is null or not in the graph;
	 * otherwise a set of cycles for this vertex in a list of list of
	 * vertex form, where each list in this list represents a cycle for
	 * this vertex.
	 */
	public List<List<T>> cycle(T vertex) {
		if (vertex == null) {
			return null;
		}
		
		Vertex cycleVertex = get(vertex);
		
		if (cycleVertex == null) {
			return null;
		}
		
		List<List<Vertex>> cycles = new ArrayList<>();
		cycle0(cycleVertex, cycleVertex, cycles, null);
		
		List<List<T>> cyclesValue = new ArrayList<>(cycles.size());
		
		while (!cycles.isEmpty()) {
			List<Vertex> currCycle = cycles.remove(0);
			List<T> currCycleValue = new ArrayList<>(currCycle.size());
			
			while (!currCycle.isEmpty()) {
				currCycleValue.add(currCycle.remove(0).value);
			}
			
			cyclesValue.add(currCycleValue);
		}
		
		return cyclesValue;
	}
	
	/**
	 * Helper method for {@link #cycle(Object)}. Recursively passes through each
	 * vertex in the graph and seeks out every unique cycle from a given vertex.
	 *
	 * @param vertex      - the vertex currently being looked at.
	 * @param cycleVertex - the vertex to cycle from.
	 * @param cycleList   - the list of cycles from cycleVertex.
	 * @param path        - the current path that this method is currently pursuing.
	 * @return the number of unique cycles from cycleVertex.
	 */
	private int cycle0(Vertex vertex, Vertex cycleVertex, List<List<Vertex>> cycleList, List<Vertex> path) {
		cycle0Depth++;
		
		Iterator<Vertex> currLinks = vertex.links.iterator();
		
		if (path == null) {
			path = new ArrayList<>();
		}
		
		path.add(vertex);
		
		while (currLinks.hasNext()) {
			Vertex next = currLinks.next();
			
			if (next == vertex) {
				if (next == cycleVertex) {
					path.add(vertex);
					cycleList.add(new ArrayList<>(path));
					path.remove(path.size() - 1);
				}
				continue;
			} else if (next == cycleVertex) {
				path.add(next);
				cycleList.add(new ArrayList<>(path));
				path.remove(path.size() - 1);
				continue;
			} else if (path.contains(next) || path.contains(next)) {
				continue;
			}
			
			cycle0(next, cycleVertex, cycleList, path);
			
			// TODO Needs to be reviewed
			
			// if (cycle0(next, cycleVertex, cycleList, path) > 0) {
			// if (cycle0Depth > 1) {
			// cycle0Depth--;
			// return 1;
			// } else {
			// if (path.size() > 1 && path.get(0) == path.get(path.size() - 1))
			// {
			// cycleList.add(path);
			// path = new ArrayList<Vertex>();
			// path.add(cycleVertex);
			// }
			// }
			// if (cycle0Depth == 1)
			// path0.add(0, next);
			// else {
			// cycle0Depth--;
			// return true;
			// }
			// }
			
		}
		
		cycle0Depth--;
		
		if (cycle0Depth > 0) path.remove(path.size() - 1);
		
		return 0;
	}
	
	/**
	 * Vertex used for a directed graph that "points" towards other vertices.
	 *
	 * @author Vishal Patel
	 */
	private class DirectedVertex extends Vertex {
		/**
		 * Constructs a DirectedVertex object from a given value of type T and
		 * initializes the vertices that this vertex points to to an empty list.
		 *
		 * @param value - the value of this vertex.
		 */
		DirectedVertex(T value) {
			super(value);
		}
		
		/**
		 * Constructs a DirectedVertex object from a given value of type T and a
		 * list of T objects that this vertex will point to.
		 *
		 * @param value - the value of this vertex.
		 * @param links - the vertices that this vertex will point to.
		 */
		DirectedVertex(T value, List<T> links) {
			super(value, links);
		}
		
		@Override
		protected boolean setLinks(List<T> newLinks) {
			links.removeIf(v -> {
				if (!newLinks.contains(v.value)) {
					return true;
				}
				
				newLinks.remove(v.value);
				return false;
			});
			
			if (!newLinks.isEmpty()) {
				changesMade = true;
			}
			
			for (T currVal : newLinks) {
				if (isPlotted(currVal)) {
					links.add(vertices.get(indexOf(currVal)));
				} else {
					if (currVal.equals(value)) {
						links.add(this);
					} else {
						Vertex v = new DirectedVertex(currVal);
						vertices.add(v);
						links.add(v);
					}
				}
				
				// TODO Needs to be reviewed
				// boolean isGraphed = false;
				//
				// for (int j = 0; j < vertices.size(); j++)
				// if (vertices.get(j).value.equals(currVal)) {
				// isGraphed = true;
				// links.add(vertices.get(j));
				// break;
				// }
				//
				// if (!isGraphed) {
				// if (currVal.equals(value))
				// links.add(this);
				// else {
				// Vertex v = new DirectedVertex(currVal);
				// vertices.add(v);
				// links.add(v);
				// }
				// }
			}
			
			return true;
		}
		
		@Override
		protected boolean addLinks(List<T> additionalLinks) {
			additionalLinks.removeAll(links);
			
			for (T t : additionalLinks) {
				if (isPlotted(t)) {
					links.add(vertices.get(indexOf(t)));
				} else {
					Vertex v = new DirectedVertex(t);
					vertices.add(v);
					links.add(v);
				}
			}
			
			return true;
		}
		
		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			
			List<T> values = new ArrayList<>(links.size());
			links.forEach(link -> values.add(link.value));
			s.append(String.format("%s -> %s", this.value, values));
			
			return s.toString();
		}
	}
}