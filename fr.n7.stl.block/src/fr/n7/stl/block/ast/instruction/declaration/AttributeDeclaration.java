package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.type.Type;

public class AttributeDeclaration implements Declaration, ClassElement {

	private String name;
	private Type type;
	private Expression value;
	private boolean isFinal;
	private boolean isStatic;

	
	public AttributeDeclaration(String name, Type type, Expression value, boolean isFinal, boolean isStatic) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.isFinal = isFinal;
		this.isStatic = isStatic;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
