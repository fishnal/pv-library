package pvlib.util;

import java.util.ArrayList;
import java.util.List;

import pvlib.math.structures.RealMatrix;

/**
 * Undirected graph of vertices. Various graph-theory properties and calculations are provided, such as cycles
 * and path lengths.
 * @param <T> - the type of vertex the graph will hold
 */
public class UndirectedGraph<T> extends Graph<T> {
	private class UndirectedVertex extends Vertex {
		UndirectedVertex(T value) {
			super(value);
		}

		UndirectedVertex(T value, List<T> links) {
			this(value);
			setLinks(links);
		}

		@Override
		protected boolean setLinks(List<T> newLinks) {
			links.removeIf(v -> {
				if (!newLinks.contains(v.value)) {
					v.links.remove(UndirectedVertex.this);
					return true;
				}

				newLinks.remove(v.value);
				return false;
			});

			if (!newLinks.isEmpty())
				changesMade = true;

			for (T currVal : newLinks) {
				if (isPlotted(currVal)) {
					links.add(vertices.get(indexOf(currVal)));
				} else {
					if (currVal.equals(value))
						links.add(this);
					else {
						Vertex v = new UndirectedVertex(currVal);
						vertices.add(v);
						links.add(v);
						v.links.add(this);
					}
				}
			}

			return true;
		}

		@Override
		protected boolean addLinks(List<T> additionalLinks) {
			additionalLinks.removeAll(links);

			if (!additionalLinks.isEmpty())
				changesMade = true;

			for (T t : additionalLinks) {
				Vertex v;

				if (isPlotted(t)) {
					links.add(v = vertices.get(indexOf(t)));
					v.links.add(this);
				} else {
					v = new UndirectedVertex(t);
					vertices.add(v);
					links.add(v);
				}
			}

			return true;
		}
	}

	private boolean changesMade = false;

	public UndirectedGraph() {
		super();
	}

	@Override
	public boolean add(T vertex, T... links) {
		if (links == null)
			return add0(vertex, null);

		ArrayList<T> linksList = new ArrayList<>(java.util.Arrays.asList(links));
		return add0(vertex, linksList);
	}

	private boolean add0(T vertex, ArrayList<T> links) {
		if (vertex == null) {
			return false;
		}

		if (isPlotted(vertex)) {
			return vertices.get(indexOf(vertex)).setLinks(links);
		}

		Vertex v0;

		if (links != null)
			v0 = new UndirectedVertex(vertex, links);
		else
			v0 = new UndirectedVertex(vertex);

		vertices.add(v0);

		changesMade = true;

		return true;
	}

	@Override
	protected void updateAdjacencyMatrix() {
		if (!changesMade)
			return;
		
		changesMade = false;
		
		super.updateAdjacencyMatrix();
	}

	@Override
	public boolean remove(T vertex) {
		if (vertex == null)
			return false;
				
		int vertexInd = indexOf(vertex);
		if (vertexInd == -1)
			return false;
		
		Vertex v = vertices.get(vertexInd);
		for (int i = 0; i < v.links.size(); i++)
			v.links.get(i).links.remove(v);
		v.links.clear();
		vertices.remove(v);
		
		return true;
	}

	@Override
	public boolean set(T vertex, T... links) {
		if (vertex == null)
			return false;

		Vertex v0 = get(vertex);

		if (v0 == null)
			return false;

		ArrayList<T> linksList = new ArrayList<>(java.util.Arrays.asList(links));

		return v0.setLinks(linksList) && (changesMade = true);

	}

	@Override
	public long pathLength(int n) {
		RealMatrix mat = getAdjacencyMatrix();
		
		mat = mat.pow(n);
		
		long sum = 0;
		
		for (int r = 0; r < mat.height; r++)
			for (int c = 0; c < mat.width; c++)
				sum += mat.getValue(r, c).longValue();
		
		return sum;
	}

	@Override
	public boolean pathLength(T vertex, int n) {
		if (vertex == null || n < 0)
			return false;
		
		int ind = indexOf(vertex);
		
		if (ind < 0)
			return false;
		
		RealMatrix mat = getAdjacencyMatrix();
		
		if (n > 1)
			mat = mat.pow(n);
		
		for (int c = 0; c < mat.width; c++)
			if (mat.getValue(ind, c).intValue() == n)
				return true;

		return false;
	}

	@Override
	public boolean pathLength(T originVertex, T targetVertex, int n) {
		if (originVertex == null || targetVertex == null || n < 0)
			return false;
		
		int oi = indexOf(originVertex);
		int ti = indexOf(targetVertex);
		
		if (oi < 0 || ti < 0)
			return false;
		
		Vertex ov = vertices.get(oi);
		Vertex tv = vertices.get(ti);
		
		if (n == 0 && ov != tv)
			return false;
		
		RealMatrix mat = getAdjacencyMatrix();
		mat = mat.pow(n);
		
		return mat.getValue(oi, ti) > 0;
	}
}
