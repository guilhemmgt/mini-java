package fr.n7.stl.block.ast.instruction.declaration;

import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

public class ConstructorDeclaration implements ClassElement, Type {
	private AccessRight typeAcces = null;
	private Signature entete;
	private Block corps;

	HierarchicalScope<Declaration> scopeParams;

	public ConstructorDeclaration(String name, List<ParameterDeclaration> parameters, Block corps) {
		this.entete = new Signature(null, name, parameters);
		this.corps = corps;
	}

	@Override
	public String getName() {
		return this.entete.getName();
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

	public Signature getEntete () {
		return this.entete;
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
		this.scopeParams = new SymbolTable(_scope);
		
		if (_scope.accepts(this)) {
			_scope.register(this);
			
			for(ParameterDeclaration pad : this.entete.getParametres()) {
				this.scopeParams.register(pad);
			}
			
			boolean result = this.corps.collect(this.scopeParams);
			if (!result)
				Logger.error("ConstructorDeclaration collect failed");

			return result;
		} else {
			Logger.error("ConstructorDeclaration collect failed (not accepted)");
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
