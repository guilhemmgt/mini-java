package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
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
	private SymbolTable locals;

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
		boolean	resolved = true;
		if (this.corps != null) {
			resolved = this.corps.resolve(this.locals);
		}
		return resolved;
	}

	@Override
	public boolean collectCE(HierarchicalScope<Declaration> _scope) {
		if (_scope.accepts(this)) {
			_scope.register(this);
			this.locals = new SymbolTable (_scope);

			SymbolTable.constructorDeclaration = null;
			SymbolTable.methodDeclaration = this;
			
			for(ParameterDeclaration pad : this.entete.getParametres()) {
				this.locals.register(pad);
			}
			boolean collect = true;
			if (this.corps != null) {
				collect = this.corps.collect(this.locals);
			}
			return collect;
		} else {
			Logger.error("(MethodDeclaration) " + this.getName() + " déjà utilisé dans ce scope.");
			return false;
		}

	}

	@Override
	public boolean checkType() {
		//throw new UnsupportedOperationException("Unimplemented method 'checkType'");
		boolean checked = true; 
		if (this.corps != null) {
			checked = this.corps.checkType();
		}
		return checked;
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
		if (this.corps != null){
			this.corps.allocateMemory(_register, offset+taille);
		}
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

		//On vérifie si le corps n'est pas null (méthode abstraite)
		if (this.corps != null) {
			frag.append(this.corps.getCode(_factory));
			frag.addPrefix(this.entete.getName());
			frag.addComment(this.toString());
		}
		return frag;
	}
}
