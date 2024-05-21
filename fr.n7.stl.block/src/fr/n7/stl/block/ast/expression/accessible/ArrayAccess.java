/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractArray;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.assignable.VariableAssignment;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.type.ArrayType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing an array element.
 * @author Marc Pantel
 *
 */
public class ArrayAccess extends AbstractArray implements AccessibleExpression {

	/**
	 * Construction for the implementation of an array element access expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element access expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element access expression.
	 */
	public ArrayAccess(Expression _array, Expression _index) {
		super(_array,_index);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment code = _factory.createFragment();
		code.append(this.array.getCode(_factory));
		code.append(this.index.getCode(_factory)); // charger l'index
		Type arrayType = this.array.getType();
		while (arrayType instanceof NamedType) {
			arrayType = ((NamedType)arrayType).getType();
		}
		code.add(_factory.createLoadL(((ArrayType)arrayType).getType().length())); // charge la taille d'un éléménet
		code.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
		code.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
		code.add(_factory.createLoadI(((ArrayType)arrayType).getType().length()));
		code.addComment("ArrayAccess: " + this.toString());
		return code;
	}

}
