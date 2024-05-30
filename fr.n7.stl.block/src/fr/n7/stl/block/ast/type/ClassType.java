/**
 * 
 */
package fr.n7.stl.block.ast.type;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.declaration.TypeDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a named type.
 * 
 * @author Marc Pantel
 *
 */
public class ClassType implements Type {

	private ClassDeclaration declaration; // n'a pas forcément de valeur avant le resolve
	private String name;

	public ClassType(String _name) {
		this.name = _name;
		this.declaration = null;
	}

	public ClassType(ClassDeclaration _declaration) {
		this.declaration = _declaration;
		this.name = _declaration.getName();
	}

	public String getName() {
		return this.name;
	}

	public ClassDeclaration getClassDeclaration () {
		return this.declaration;
	}

	@Override
	public boolean equalsTo(Type _other) {
		throw new SemanticsUndefinedException( "equalsTo is undefined in ClassType.");
	}


	@Override
	public boolean compatibleWith(Type _other) {
		throw new SemanticsUndefinedException( "compatibleWith is undefined in ClassType.");
	}

	@Override
	public Type merge(Type _other) {
		throw new SemanticsUndefinedException( "merge is undefined in ClassType.");
	}


	@Override
	public int length() {
		throw new SemanticsUndefinedException( "length is undefined in ClassType.");
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		Declaration declaration = _scope.get(this.name);
		if (!(declaration instanceof ClassDeclaration)) {
			Logger.error("(ClassType) La classe " + this.name + " n'existe pas");
			return false;
		}
		this.declaration = (ClassDeclaration) declaration;

		boolean resolve = this.declaration.resolve(_scope);

		return resolve;
	}
}
