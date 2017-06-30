package pvlib.util;

import java.util.Comparator;

/**
 * Utilities for Class objects, such as determining whether or not a primitive
 * type can be casted to a certain object type, or the class of the deepest
 * component in an array.
 * 
 * @author Vishal Patel
 *
 */
public final class Classes {
	private Classes() {
	}

	private final static Class<?> b = boolean.class;
	private final static Class<?> by = byte.class;
	private final static Class<?> c = char.class;
	private final static Class<?> s = short.class;
	private final static Class<?> i = int.class;
	private final static Class<?> l = long.class;
	private final static Class<?> f = float.class;
	private final static Class<?> d = double.class;
	private final static Class<?>[] primitives = { b, by, c, s, i, l, f, d };

	private final static Class<?> B = Boolean.class;
	private final static Class<?> BY = Byte.class;
	private final static Class<?> C = Character.class;
	private final static Class<?> S = Short.class;
	private final static Class<?> I = Integer.class;
	private final static Class<?> L = Long.class;
	private final static Class<?> F = Float.class;
	private final static Class<?> D = Double.class;
	private final static Class<?>[] wrappers = { B, BY, C, S, I, L, F, D };

	/**
	 * Checks if an object is an instance of a wrapper class.
	 * 
	 * @param obj
	 *            - the object to check.
	 * @return true if the object is an instance of a wrapper class; false if
	 *         the object is null or if it is not an instance of a wrapper
	 *         class.
	 * @see #isWrapper(Class)
	 */
	public static boolean isWrapper(Object obj) {
		return obj != null && isWrapper(obj.getClass());
	}

	/**
	 * Checks if a class type is a wrapper class.
	 * 
	 * @param type
	 *            - the class type to check.
	 * @return true if the class type is a wrapper class.
	 * @see #isWrapper(Object)
	 */
	public static boolean isWrapper(Class<?> type) {
		for (Class<?> clazz : wrappers) {
			if (type == clazz) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the primitive class associated with a wrapper class.
	 * 
	 * @param wrapper
	 *            - the wrapper class.
	 * @return the primitive class associated with the given wrapper class.
	 */
	public static Class<?> wrapperClassToPrimitiveClass(Class<?> wrapper) {
		if (wrapper.isPrimitive()) {
			return wrapper;
		}

		for (int i = 0; i < wrappers.length; i++) {
			if (wrapper == wrappers[i]) {
				return primitives[i];
			}
		}

		return null;
	}

	/**
	 * Gets the wrapper class associated with a primitive class.
	 * 
	 * @param prim
	 *            - the primitive class.
	 * @return the wrapper class associated with the given primitive class.
	 */
	public static Class<?> primitiveClassToWrapperClass(Class<?> prim) {
		for (int i = 0; i < primitives.length; i++) {
			if (prim == primitives[i]) {
				return wrappers[i];
			}
		}

		return null;
	}

	/**
	 * An array of unknown generic type primitive classes representing what one
	 * primitive data type can be casted to. <b>boolean</b> always returns
	 * <b>boolean.class</b> while any other primitive data type returns all
	 * other primitive classes.
	 * 
	 * @param prim
	 *            - the primitive class.
	 * @return null if the primitive class is null or is not actually a
	 *         primitive class; <b>boolean.class</b> if
	 *         <code>prim == <b>boolean.class</b></code> in an array of unknown
	 *         generic typed classes; otherwise an array of of unknown generic
	 *         typed classes filled with every primitive class except for
	 *         <b>boolean.class</b>
	 */
	private static Class<?>[] primitiveCastTypes(Class<?> prim) {
		if (prim == null) {
			return null;
		}

		if (prim.isPrimitive()) {
			if (prim == boolean.class) {
				return new Class<?>[]{boolean.class};
			}

			return primitives.clone();
		}

		return null;
	}

	/**
	 * Checks whether one primitive type can be casted into another primitive
	 * type.
	 * 
	 * @param prim1
	 *            - the first primitive type to cast.
	 * @param prim2
	 *            - the second primitive type to be casted to.
	 * @return true if the cast is possible; false if either of the parameters
	 *         are null or if the cast is not possible.
	 */
	public static boolean canCastPrimitiveToPrimitive(Class<?> prim1, Class<?> prim2) {
		if (prim1 == null || prim2 == null) {
			return false;
		}

		Class<?>[] castsPrim1 = primitiveCastTypes(prim1);

		if (castsPrim1 == null) {
			return false;
		}

		for (Class<?> aCastsPrim1 : castsPrim1) {
			if (prim2 == aCastsPrim1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks whether an object type can be casted into another object type.
	 * Wrapper classes are included (Boolean can only be casted into itself,
	 * while Character, Byte, Short, Integer, Long, Float, and Double can all
	 * cast into one another).
	 * 
	 * @param obj1
	 *            - the object type to cast.
	 * @param obj2
	 *            - the object type to cast to.
	 * @return true if the first object type can cast into the second object
	 *         type; false otherwise.
	 */
	public static boolean canCastObjectToObject(Class<?> obj1, Class<?> obj2) {
		if (obj1 == null || obj2 == null)
			return false;

		if (isWrapper(obj1) && isWrapper(obj2)) {
			Class<?> prim1 = wrapperClassToPrimitiveClass(obj1);
			Class<?> prim2 = wrapperClassToPrimitiveClass(obj2);

			return canCastPrimitiveToPrimitive(prim1, prim2);
		} else {
			try {
				// obj2.asSubclass(obj1);
				return obj2.isAssignableFrom(obj1);
			} catch (ClassCastException cce) {
				return false;
			}
		}
	}

	public static boolean canCast(Class<?> clazz1, Class<?> clazz2) {
		if (clazz1 == null || clazz2 == null)
			return false;

		boolean a = isWrapper(clazz1) || clazz1.isPrimitive();
		boolean b = isWrapper(clazz2) || clazz2.isPrimitive();
		if (a ^ b)
			return false;

		if (a & b) {
			Class<?> primClazz1 = isWrapper(clazz1) ? wrapperClassToPrimitiveClass(clazz1) : clazz1;
			Class<?> primClazz2 = isWrapper(clazz2) ? wrapperClassToPrimitiveClass(clazz2) : clazz2;

			return canCastPrimitiveToPrimitive(primClazz1, primClazz2);
		}

		try {
			return clazz2.isAssignableFrom(clazz1);
		} catch (ClassCastException cce) {
			return false;
		}
	}

	/**
	 * Casts a primitive data into another primitive data.
	 * 
	 * @param primitive
	 *            - the primitive data to cast.
	 * @param newPrimitiveType
	 *            - the primitive data type to cast to.
	 * @return null if either of the parameters are null, if the primitive data
	 *         given was not wrapped, or if the primitive data cannot be casted
	 *         into the new specified primitive data type; an Object wrapping
	 *         casted primitive data otherwise.
	 * @throws IllegalArgumentException if the primitive argument does not have
	 * a wrapper equivalent class
	 */
	public static Object primitiveToPrimitiveCast(Object primitive, Class<?> newPrimitiveType) {
		if (!isWrapper(primitive)) {
			return new IllegalArgumentException("object must be primitive");
		}

		if (!isWrapper(primitive)) {
			return null;
		}

		Class<?> primClass = wrapperClassToPrimitiveClass(primitive.getClass());

		if (!canCastPrimitiveToPrimitive(primClass, newPrimitiveType)) {
			return null;
		}

		Number n;

		if (primitive.getClass() == Character.class) {
			n = (int) ((char) primitive);
		} else {
			n = (Number) primitive;
		}

		if (newPrimitiveType == char.class) {
			return (char) n.doubleValue();
		} else if (newPrimitiveType == byte.class) {
			return n.byteValue();
		} else if (newPrimitiveType == short.class) {
			return n.shortValue();
		} else if (newPrimitiveType == int.class) {
			return n.intValue();
		} else if (newPrimitiveType == long.class) {
			return n.longValue();
		} else if (newPrimitiveType == float.class) {
			return n.floatValue();
		} else if (newPrimitiveType == double.class) {
			return n.doubleValue();
		}

		return null;
	}

	/**
	 * Casts an object to a new type.
	 * 
	 * @param obj
	 *            - the object to cast.
	 * @param newType
	 *            - the new type to cast the object to.
	 * @return null if the object or the new type is null; otherwise the newly
	 *         casted object.
	 * @throws ClassCastException if the object could not be casted.
	 */
	public static Object objectToObjectCast(Object obj, Class<?> newType) throws ClassCastException {
		if (obj == null || newType == null) {
			return null;
		}

		if ((obj.getClass().isPrimitive() || isWrapper(obj)) && (newType.isPrimitive() || isWrapper(newType))) {
			// obj is primitive or wrapper and newType is primitive or wrapper
			Class<?> primClass1 = isWrapper(obj) ? wrapperClassToPrimitiveClass(obj.getClass()) : obj.getClass();
			Class<?> primClass2 = isWrapper(newType) ? wrapperClassToPrimitiveClass(newType) : newType;

			if (primClass1 == boolean.class && primClass2 == boolean.class) {
				return obj;
			} else {
				if (primClass1 == primClass2) {
					return obj;
				}

				return primitiveToPrimitiveCast(obj, primClass2);
			}
		} else {
			// is not a primitive data type or equivalent
			return newType.cast(obj);
		}
	}

	/**
	 * Retrieves the class of the deepest component of a class (used for array
	 * classes).
	 * 
	 * @param clazz
	 *            - the class to get the deepest component from.
	 * @return the class of the deepest component of <code>clazz</code>.
	 */
	public static Class<?> getDeepestComponent(Class<?> clazz) {
		if (clazz.isArray()) {
			return getDeepestComponent(clazz.getComponentType());
		}
		return clazz;
	}

	/**
	 * Strictly checks if an array has some element in any of its dimensions
	 * whose class is the EXACT same as another class. Unlike
	 * {@link #componentCheck(Class, Class)}, wrapper classes are not checked
	 * for data equality against primitive classes and utilizes the
	 * <code>==</code> comparison operator.
	 * 
	 * @param array
	 *            - the array to check upon.
	 * @param clazz
	 *            - the given class to check the array's element with.
	 * @return true if the array has some element in any of its dimensions whose
	 *         class is the EXACT same as the given class.
	 */
	public static boolean strictComponentCheck(Class<?> array, Class<?> clazz) {
		return array == clazz || array.isArray() && strictComponentCheck(array.getComponentType(), clazz);
	}

	/**
	 * Checks if an array has some element in any of its dimensions whose class
	 * is equivalent as another class. If the element's class is primitive, then
	 * the element's wrapper class equivalent is used for equality checking. For
	 * example, given <code>Double[][].class</code> and
	 * <code>double.class</code>, the method returns true.
	 * 
	 * @param array
	 *            - the array to check upon.
	 * @param clazz
	 *            - the given class to check the array's element with.
	 * @return true if the array has some element in any of its dimensions whose
	 *         class is equivalent to the given class.
	 */
	public static boolean componentCheck(Class<?> array, Class<?> clazz) {
		if (clazz.isPrimitive()) {
			clazz = primitiveClassToWrapperClass(clazz);
		} else if (array.isPrimitive()) {
			array = primitiveClassToWrapperClass(array);
		}

		return array == clazz || array.isArray() && componentCheck(array.getComponentType(), clazz);
	}

	/**
	 * The natural ordering comparator. Ultimately equivalent to
	 * {@link java.util.Comparator#naturalOrder()}.
	 * 
	 * @return the natural ordering comparator that compares two objects,
	 *         assuming they can be casted into {@link java.lang.Comparable}
	 *         objects.
	 */
	public static Comparator<Comparable<Object>> naturalComparator() {
		return Comparable::compareTo;
	}
}
