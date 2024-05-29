package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.util.Logger;

import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.ClassElement;

public class MethodDeclaration implements ClassElement {

	private Signature entete;
	private Block corps;
	private boolean isFinal;
	private boolean isStatic;
	private boolean isAbstract;
	private AccessRight typeAcces = null;

	// Table des symboles spécifiques aux paramètres
	HierarchicalScope<Declaration> scopeParams;

	public MethodDeclaration(Signature entete, Block corps, boolean isFinal, boolean isStatic, boolean isAbstract) {
		this.entete = entete;
		this.corps = corps;
		this.isFinal = isFinal;
		this.isStatic = isStatic;
		this.isAbstract = isAbstract;
	}

	@Override
	public String getName() {
		return this.entete.getName();
	}

	@Override
	public Type getType() {
		return this.entete.getType();
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
		//TODO : vérifier que c'est suffisant de faire sur le coprs
		return this.corps.resolve(_scope);
	}

	@Override
	public boolean collectCE(HierarchicalScope<Declaration> _scope) {
		List<ParameterDeclaration> parameters = this.entete.getParametres();
		this.scopeParams = new SymbolTable(_scope);
		
		if (_scope.accepts(this)) {
			_scope.register(this);
			
			for(ParameterDeclaration pad : parameters) {
				this.scopeParams.register(pad);
			}
			
			boolean collect = this.corps.collect(this.scopeParams);
			if (!collect)
				Logger.error("MethodDeclaration collect failed");
			return collect;
		} else {
			Logger.error("MethodDeclaration collect failed (not accepted)");
			return false;
		}

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
}
