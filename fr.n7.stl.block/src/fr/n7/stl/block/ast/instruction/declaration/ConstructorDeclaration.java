package fr.n7.stl.block.ast.instruction.declaration;

import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.Type;

public class ConstructorDeclaration implements ClassElement, Type {
	private AccessRight typeAcces = null;
	private String name;
	private List<ParameterDeclaration> parameters;
	private Block corps;

	HierarchicalScope<Declaration> scopeParams;

	public ConstructorDeclaration(String name, List<ParameterDeclaration> parameters, Block corps) {
		this.name = name;
		this.parameters = parameters;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccessRight getTypeAcces() {
		return typeAcces;
	}

	@Override
	public void setTypeAcces(AccessRight a) {
		this.typeAcces = a;
	}

	@Override
	public boolean resolveCE(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'resolveCE'");
	}

	@Override
	public boolean collectCE(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		this.scopeParams = new SymbolTable(_scope);
		
		if (_scope.accepts(this)) {
			_scope.register(this);
			
			for(ParameterDeclaration pad : parameters) {
				this.scopeParams.register(pad);
			}
			
			return this.corps.collect(this.scopeParams);
		} else {
			return false;
		}
	}

	@Override
	public boolean equalsTo(Type _other) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'equalsTo'");
	}

	@Override
	public boolean compatibleWith(Type _other) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'compatibleWith'");
	}

	@Override
	public Type merge(Type _other) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'merge'");
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'length'");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'resolve'");
	}
}
