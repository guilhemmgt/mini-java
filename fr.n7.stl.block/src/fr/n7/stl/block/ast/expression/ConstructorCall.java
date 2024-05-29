/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.declaration.ConstructorDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.MethodDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.Signature;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.ClassType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ConstructorCall implements Expression {

	protected ConstructorDeclaration methode; // Affecté au collect/resolve
	protected Type type; // Le type de la classe
	protected List<Expression> arguments;
	
	/**
	 * @param _methode : called function.
	 * @param _arguments : List of AST nodes that computes the values of the parameters for the function call.
	 */
	public ConstructorCall(Type _type, List<Expression> _arguments) {
		this.type = _type;
		this.arguments = _arguments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = ((this.methode == null)?this.type.toString():this.methode.getName()) + "( ";
		Iterator<Expression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += " ," + _iter.next();
		}
		return  _result + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		// Obtention des paramètres
		ArrayList<ParameterDeclaration> argumentsDeclarations = new ArrayList<ParameterDeclaration>();
		for (Expression arg : arguments) {
			argumentsDeclarations.add(new ParameterDeclaration(arg.toString(), arg.getType()));
		}
		// Obtention de la méthode du constructeur
		NamedType namedType = (NamedType) this.type;
		ClassType classType = (ClassType) _scope.get(namedType.toString());
		Signature signature = new Signature(null, classType.getName(), argumentsDeclarations);
		this.methode = (ConstructorDeclaration) classType.get(signature);
		// Suite du resolve

		boolean funCollect = this.methode == null ? true : this.methode.collectCE(_scope);
		boolean argCollects = true;
		for (Expression arg : this.arguments) {
			argCollects = argCollects && arg.collectAndBackwardResolve(_scope);
		}
		return funCollect && argCollects;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		boolean funResolve = this.methode.resolveCE(_scope);
		boolean argResolves = true;
		for (Expression arg : this.arguments) {
			argResolves = argResolves && arg.fullResolve(_scope);
		}
		return funResolve && argResolves;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		return this.methode.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		frag.addComment(this.toString());
		for (Expression e : this.arguments) {
			frag.append(e.getCode(_factory));
		}
		frag.add(_factory.createCall(this.methode.getName(), Register.SB));
		return frag;
	}
}
