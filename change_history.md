[ComplexNumber]:src/main/java/pvlib/math/ComplexNumber.java
[Fraction]:src/main/java/pvlib/math/Fraction.java
[PolarCoordinate]:src/main/java/pvlib/math/PolarCoordinate.java
[PVMath]:src/main/java/pvlib/math/PVMath.java
[ComplexMatrix]:src/main/java/pvlib/math/structures/ComplexMatrix.java
[IllegalMatrixException]:src/main/java/pvlib/math/structures/IllegalMatrixException.java
[Matrix]:src/main/java/pvlib/math/structures/Matrix.java
[NumberMatrix]:src/main/java/pvlib/math/structures/NumberMatrix.java
[RealMatrix]:src/main/java/pvlib/math/structures/RealMatrix.java
[AbstractStructure]:src/main/java/pvlib/util/AbstractStructure.java
[Arrays]:src/main/java/pvlib/util/Arrays.java
[BinaryTree]:src/main/java/pvlib/util/BinaryTree.java
[Classes]:src/main/java/pvlib/util/Classes.java
[DataStructure]:src/main/java/pvlib/util/DataStructure.java
[DirectedGraph]:src/main/java/pvlib/util/DirectedGraph.java
[ForEachHolder]:src/main/java/pvlib/util/ForEachHolder.java
[Graph]:src/main/java/pvlib/util/Graph.java
[LinkedList]:src/main/java/pvlib/util/LinkedList.java
[Search]:src/main/java/pvlib/util/Search.java
[SortedLinkedList]:src/main/java/pvlib/util/SortedLinkedList.java
[SortMethod]:src/main/java/pvlib/util/SortMethod.java
[Tree]:src/main/java/pvlib/util/Tree.java
[UndirectedGraph]:src/main/java/pvlib/util/UndirectedGraph.java

# Date Format: DD/MM/YYYY

# 03/08/2017
### [pvlib.math.structures.ComplexMatrix][ComplexMatrix]
+ Fixed `ComplexMatrix multiply(NumberMatrix<ComplexNumber>)`: product would be a matrix of width=this.height and
height=matrix.width; instead these should be flipped so width=matrix.width and height=this.height
### [pvlib.math.structures.Matrix][Matrix]
+ Added some necessary imports
+ Fixed `static int checkData(Object[][])`: iteration through columns continued while column index was
less than number of rows rather than number of columns in current iterating row, which could
lead to an IndexOutOfBoundsException being thrown
+ Added `public T[] getRow(int row, Class<T> clazz)`: gets the elements in a row for this matrix, provided
the row index and the generic type's class
+ Added `public T[] getColumn(int column, Class<T> clazz)`: gets the elements in a column for this matrix, provided
the column index and the generic type's class
+ Added `public void forEach(Consumer<pvlib.util.ForEachHolder<T>> action)`: iterates through each element in this matrix,
performing a consumer operation on each element; the consumer however accepts a
[`pvlib.util.ForEachHolder`][ForEachHolder] which allows for retrieval of the current iterating indices in the matrix
as well as the data stored at these indices in the matrix; iteration goes through rows then columns in a top-bottom
then left-right manner
+ Added `public void forEachRow(Consumer<pvlib.util.ForEachHolder<T[]>> action)`: iterates through each row in this matrix,
performing a consumer operation on each row; the consumer however accepts a
[`pvlib.util.ForEachHolder`][ForEachHolder] which allows for retrieval of the current iterating row index in the matrix
as well as the row data stored at this index in the matrix; iteration goes through in a top to bottom manner
+ Added `public void forEachColumn(Consumer<pvlib.util.ForEachHolder<T[]>> action)`: iterates through each column in this
matrix, performing a consumer operation on each column; the consumer however accepts a
[`pvlib.util.ForEachHolder`][ForEachHolder] which allows for retrieval of the current iterating column index in the
matrix as well as the row data stored at this index in the matrix; iteration goes through in a left to right manner
### [pvlib.math.structures.RealMatrix][RealMatrix]
+ Fixed `RealMatrix multiply(NumberMatrix<Double>)`: product would be a matrix of width=this.height and
height=matrix.width; instead these should be flipped so width=matrix.width and height=this.height
### [pvlib.util.BinaryTree.BinaryNode][BinaryTree]
+ Added `protected void remove(BinaryNode node)`: removes a `BinaryNode` from this node's tree
+ Added `private void leafRemoval(BinaryNode node)`: removes a leaf `BinaryNode` from this node's tree
+ Added `private void singleRemoval(BinaryNode node)`: removes a `BinaryNode` that has only one child from this node's
tree
+ Added `private void doubleRemoval(BinaryNode node)`: removes a `BinaryNode` that has two children from this node's
tree
### [pvlib.util.BinaryTree][BinaryTree]
+ Modified `void add(E)`: there's no point in checking if "node" is null, we can't update it's value if node is not null
because we wouldn't be changing anything; instead we're going to be adding this value again to the tree (no single value
in tree is unique now), and if this value is not found, it's still added
+ Fixed `boolean remove(E)`: this was a complete mess, so it was rewritten entirely and explains why `BinaryNode` has
removal methods
### [pvlib.util.DataStructure][DataStructure]
+ Added `default void forEach(Consumer<E> action)`: iterates through each element in the data structure performing a
consumer operation on each element
### [pvlib.util.DirectedGraph][DirectedGraph]
+ Resolved a TODO review comment in `cycle0(Vertex, Vertex, List<List<Vertex>>, List<Vertex>)`: questioned whether
"path != null" was actually needed or not; resolved as no longer needed
### [pvlib.util.ForEachHolder][ForEachHolder]
+ Class created which allows for iterations in a data structure, such as matrices.
+ Provides the value and indices at the current iteration. How these indices are stored are up to how the instances
are made
### [pvlib.util.Tree][Tree]
+ Nothing changed here, just removed a line