package fr.n7.stl.block.ast.instruction.declaration;

import java.util.List;

import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.Type;

public class ConstructorDeclaration implements ClassElement {

	private String name;
	private List<ParameterDeclaration> parameters;

	public ConstructorDeclaration(String name, List<ParameterDeclaration> parameters) {
		this.name = name;
		this.parameters = parameters;
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

	@Override
	public void setTypeAcces(AccessRight a) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setTypeAcces'");
	}

}