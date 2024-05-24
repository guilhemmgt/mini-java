package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.ClassElement;


public class MethodDeclaration implements ClassElement {

	private Signature entete;
	private Block corps;
	private boolean isFinal;
	private boolean isStatic;
	private boolean isAbstract;

	

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
	public boolean resolveCE(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'resolveCE'");
	}

	@Override
	public boolean collectCE(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'collectCE'");
	}

	@Override
	public void setTypeAcces(AccessRight a) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setTypeAcces'");
	}
}
