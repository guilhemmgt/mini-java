/**
 * 
 */
package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractArray;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.type.ArrayType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for an expression whose computation assigns a cell in an array.
 * @author Marc Pantel
 */
public class ArrayAssignment extends AbstractArray implements AssignableExpression {

	/**
	 * Construction for the implementation of an array element assignment expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element assignment expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element assignment expression.
	 */
	public ArrayAssignment(AssignableExpression _array, Expression _index) {
		super(_array, _index);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.ArrayAccessImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment code = _factory.createFragment();
		VariableDeclaration varDeclaration = ((VariableAssignment)this.array).getDeclaration();

		code.add(_factory.createLoadA(varDeclaration.getRegister(), varDeclaration.getOffset()));
		code.add(_factory.createLoadI(this.array.getType().length())); // charge l'adresse du tableau
		code.append(this.index.getCode(_factory)); // charger l'index
		Type arrayType = this.array.getType();
		while (arrayType instanceof NamedType) {
			arrayType = ((NamedType)arrayType).getType();
		}
		code.add(_factory.createLoadL(((ArrayType)arrayType).getType().length())); // charge la taille d'un éléménet
		code.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
		code.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
		code.add(_factory.createStoreI(((ArrayType)arrayType).getType().length()));
		code.addComment("ArrayAssignement: " + this.toString());
		return code;
	}

	
}
