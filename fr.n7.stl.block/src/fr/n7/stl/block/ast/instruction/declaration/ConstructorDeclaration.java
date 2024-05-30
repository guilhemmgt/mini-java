package fr.n7.stl.block.ast.instruction.declaration;

import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class ConstructorDeclaration implements ClassElement, Type {
	private AccessRight typeAcces = AccessRight.PUBLIC;
	private Signature entete;
	private Block corps;

	// Table des symboles spécifiques aux paramètres
	private HierarchicalScope<Declaration> locals;

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
		//throw new UnsupportedOperationException("Unimplemented method 'resolveCE'");
		//TODO : vérifier que suffit de faire que sur le corps
		return this.corps.resolve(_scope);
	}

	@Override
	public boolean collectCE(HierarchicalScope<Declaration> _scope) {
		if (_scope.accepts(this)) {
			_scope.register(this);
			this.locals = new SymbolTable (_scope);
			
			for(ParameterDeclaration pad : this.entete.getParametres()) {
				this.locals.register(pad);
			}
			boolean collect = true;
			if (this.corps != null) {
				collect = this.corps.collect(this.locals);
			}
			return collect;
		} else {
			Logger.error("(ConstructorDeclaration) " + this.getName() + " déjà utilisé dans ce scope.");
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

	@Override
	public boolean checkType() {
		//throw new UnsupportedOperationException("Unimplemented method 'checkType'");
		return this.corps.checkType();
	}

	@Override
	public int allocateMemory(Register _register, int offset) {
		//throw new UnsupportedOperationException("Unimplemented method 'allocateMemory'");
		//TODO : A verifier (pas sûre du tout)
		List<ParameterDeclaration> parameters = this.entete.getParametres();
		int taille = 0;
		for(ParameterDeclaration pad : parameters) {
			taille += pad.getType().length();
		}
		this.corps.allocateMemory(_register, offset+taille);
		return taille;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		//throw new UnsupportedOperationException("Unimplemented method 'getCode'");
		List<ParameterDeclaration> parameters = this.entete.getParametres();
		Fragment frag = _factory.createFragment();
	

		// TODO : A verifier mais je pense pas qu'il faut load ici les paramètres mais plutôt au moment
		// où on les utilise -> voir où
		// for(ParameterDeclaration pad : parameters) {
		// 	frag.add(_factory.createLoad(Register.LB, 0, pad.getType().length()));
		// }
		frag.append(this.corps.getCode(_factory));
		frag.addPrefix(this.entete.getName());
		frag.addComment(this.toString());
		return frag;
	}
}
