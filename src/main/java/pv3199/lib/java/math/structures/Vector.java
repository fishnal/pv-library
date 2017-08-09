package pv3199.lib.java.math.structures;

import pv3199.lib.java.util.Arrays;

public class Vector {
	private double[] components;
	
	public Vector(double... components) {
		this.components = components.clone();
	}
	
	public Vector(double[] start, double[] end) throws IllegalArgumentException {
		int length;
		if ((length = start.length) != end.length) {
			throw new IllegalArgumentException("start and end different lengths");
		}
		
		this.components = new double[length];
		for (int i = 0; i < length; i++) {
			this.components[i] = end[i] - start[i];
		}
	}
	
	public int size() {
		return this.components.length;
	}
	
	public double magnitude() {
		double m = 0;
		
		for (double d : this.components) {
			m += d * d;
		}
		
		return Math.sqrt(m);
	}
	
	public double get(int index) {
		return this.components[index];
	}
	
	public void set(int index, double component) {
		this.components[index] = component;
	}
	
	public Vector unit() {
		double mag = this.magnitude();
		double[] comps = new double[this.size()];
		
		for (int i = 0; i < comps.length; i++) {
			comps[i] = this.components[i] / mag;
		}
		
		return new Vector(comps);
	}
	
	public Vector dot(Vector v) {
		int size;
		if ((size = this.size()) != v.size()) {
			throw new IllegalArgumentException("vectors are not in the same space");
		}
		
		double[] comps = new double[size];
		
		for (int i = 0; i < size; i++) {
			comps[i] = this.components[i] * v.components[i];
		}
		
		return new Vector(comps);
	}
	
	public Vector cross(Vector v) throws IllegalArgumentException {
		int s1 = this.size();
		int s2 = v.size();
		
		if (s1 < 2 || s1 > 3 || s2 < 2 || s2 > 3) {
			throw new IllegalArgumentException("vectors are not 2d or 3d");
		}
		
		if (s1 != s2) {
			throw new IllegalArgumentException("vectors are not in the same space");
		}
		
		if (s1 == 2) {
			// 2d vector
			return new Vector(0, 0, this.components[0] * v.components[1] - this.components[1] * v.components[0]);
		} else {
			return new Vector(this.components[1] * v.components[2] - this.components[2] * v.components[1], this.components[2] * v.components[0] - this.components[0] * v.components[2], this.components[0] * v.components[1] - this.components[1] * v.components[0]);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		Vector v = (Vector) obj;
		
		if (this.size() != v.size()) {
			return false;
		}
		
		return Arrays.equals(this.components, v.components);
	}
}