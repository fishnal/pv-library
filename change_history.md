[ComplexNumber]:src/main/java/pv3199/lib/java/math/ComplexNumber.java
[Fraction]:src/main/java/pv3199/lib/java/math/Fraction.java
[PolarCoordinate]:src/main/java/pv3199/lib/java/math/PolarCoordinate.java
[PVMath]:src/main/java/pv3199/lib/java/math/PVMath.java
[ComplexMatrix]:src/main/java/pv3199/lib/java/math/structures/ComplexMatrix.java
[IllegalMatrixException]:src/main/java/pv3199/lib/java/math/structures/IllegalMatrixException.java
[Matrix]:src/main/java/pv3199/lib/java/math/structures/Matrix.java
[NumberMatrix]:src/main/java/pv3199/lib/java/math/structures/NumberMatrix.java
[RealMatrix]:src/main/java/pv3199/lib/java/math/structures/RealMatrix.java
[AbstractStructure]:src/main/java/pv3199/lib/java/util/AbstractStructure.java
[Arrays]:src/main/java/pv3199/lib/java/util/Arrays.java
[BinaryTree]:src/main/java/pv3199/lib/java/util/BinaryTree.java
[Classes]:src/main/java/pv3199/lib/java/util/Classes.java
[DataStructure]:src/main/java/pv3199/lib/java/util/DataStructure.java
[DirectedGraph]:src/main/java/pv3199/lib/java/util/DirectedGraph.java
[ForEachHolder]:src/main/java/pv3199/lib/java/util/ForEachHolder.java
[Graph]:src/main/java/pv3199/lib/java/util/Graph.java
[LinkedList]:src/main/java/pv3199/lib/java/util/LinkedList.java
[Search]:src/main/java/pv3199/lib/java/util/Search.java
[SortedLinkedList]:src/main/java/pv3199/lib/java/util/SortedLinkedList.java
[SortMethod]:src/main/java/pv3199/lib/java/util/SortMethod.java
[Tree]:src/main/java/pv3199/lib/java/util/Tree.java
[UndirectedGraph]:src/main/java/pv3199/lib/java/util/UndirectedGraph.java

# Date Format: DD/MM/YYYY

# 03/08/2017
### Project Changes
+ Restructured the entire library from `pvlib` to `pv3199.lib.java`
+ Classes are remaining the same, but have been refactored to match this new package structure
+ pom.xml has been updated as well
### change_history.md
+ Added some additional links in the history (might switch to raw html rather than use
markdown formatting to make it more functional).
### [pv3199.lib.java.math.ComplexNumber][ComplexNumber]
+ Added `public PolarCoordinate toPolarCoordinate()`: converts the complex number to a [`PolarCoordinate`][PolarCoordinate]
object
### [pv3199.lib.java.math.structures.ComplexMatrix][ComplexMatrix]
+ Fixed `ComplexMatrix multiply(NumberMatrix<ComplexNumber>)`: product would be a matrix of width=this.height and
height=matrix.width; instead these should be flipped so width=matrix.width and height=this.height
### [pv3199.lib.java.math.structures.Matrix][Matrix]
+ Added some necessary imports
+ Fixed `static int checkData(Object[][])`: iteration through columns continued while column index was
less than number of rows rather than number of columns in current iterating row, which could
lead to an [IndexOutOfBoundsException](https://docs.oracle.com/javase/8/docs/api/java/lang/IndexOutOfBoundsException.html) being thrown
+ Added `public T[] getRow(int row, Class<T> clazz)`: gets the elements in a row for this matrix, provided
the row index and the generic type's class
+ Added `public T[] getColumn(int column, Class<T> clazz)`: gets the elements in a column for this matrix, provided
the column index and the generic type's class
+ Added `public void forEach(Consumer<ForEachHolder<T>> action)`: iterates through each element in this matrix,
performing a consumer operation on each element; the consumer however accepts a
[`ForEachHolder`][ForEachHolder] which allows for retrieval of the current iterating indices in the matrix
as well as the data stored at these indices in the matrix; iteration goes through rows then columns in a top-bottom
then left-right manner
+ Added `public void forEachRow(Consumer<ForEachHolder<T[]>> action)`: iterates through each row in this matrix,
performing a consumer operation on each row; the consumer however accepts a
[`ForEachHolder`][ForEachHolder] which allows for retrieval of the current iterating row index in the matrix
as well as the row data stored at this index in the matrix; iteration goes through in a top to bottom manner
+ Added `public void forEachColumn(Consumer<ForEachHolder<T[]>> action)`: iterates through each column in this
matrix, performing a consumer operation on each column; the consumer however accepts a
[`ForEachHolder`][ForEachHolder] which allows for retrieval of the current iterating column index in the
matrix as well as the row data stored at this index in the matrix; iteration goes through in a left to right manner
### [pv3199.lib.java.math.structures.RealMatrix][RealMatrix]
+ Fixed `RealMatrix multiply(NumberMatrix<Double>)`: product would be a matrix of width=this.height and
height=matrix.width; instead these should be flipped so width=matrix.width and height=this.height
### [pv3199.lib.java.util.BinaryTree.BinaryNode][BinaryTree]
+ Added `protected void remove(BinaryNode node)`: removes a [`BinaryNode`][BinaryNode] from this node's tree
+ Added `private void leafRemoval(BinaryNode node)`: removes a leaf [`BinaryNode`][BinaryNode] from this node's tree
+ Added `private void singleRemoval(BinaryNode node)`: removes a [`BinaryNode`][BinaryNode] that has only one child from this node's
tree
+ Added `private void doubleRemoval(BinaryNode node)`: removes a [`BinaryNode`][BinaryNode] that has two children from this node's
tree
### [pv3199.lib.java.util.BinaryTree][BinaryTree]
+ Modified `void add(E)`: there's no point in checking if "node" is null, we can't update it's value if node is not null
because we wouldn't be changing anything; instead we're going to be adding this value again to the tree (no single value
in tree is unique now), and if this value is not found, it's still added
+ Fixed `boolean remove(E)`: this was a complete mess, so it was rewritten entirely and explains why [`BinaryNode`][BinaryNode] has
removal methods
### [pv3199.lib.java.util.DataStructure][DataStructure]
+ Added `default void forEach(Consumer<E> action)`: iterates through each element in the data structure performing a
consumer operation on each element
### [pv3199.lib.java.util.DirectedGraph][DirectedGraph]
+ Resolved a TODO review comment in `cycle0(Vertex, Vertex, List<List<Vertex>>, List<Vertex>)`: questioned whether
"path != null" was actually needed or not; resolved as no longer needed
### [pv3199.lib.java.util.ForEachHolder][ForEachHolder]
+ Class created which allows for iterations in a data structure, such as matrices.
+ Provides the value and indices at the current iteration. How these indices are stored are up to how the instances
are made
### [pv3199.lib.java.util.Tree][Tree]
+ Nothing changed here, just removed a line