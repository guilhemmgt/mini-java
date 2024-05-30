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
import fr.n7.stl.block.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class ConstructorCall implements Expression {
	protected ClassType type;
	protected List<Expression> arguments;
	
	public ConstructorCall(ClassType _type, List<Expression> _arguments) {
		this.type = _type;
		this.arguments = _arguments;
	}

	@Override
	public String toString() {
		String _result = this.type.getName() + "( ";
		Iterator<Expression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += " ," + _iter.next();
		}
		return  _result + ")";
	}

	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean argCollects = true;
		for (Expression arg : this.arguments) {
			argCollects = argCollects && arg.collectAndBackwardResolve(_scope);
		}
		return argCollects;
	}

	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		// On récupère la classe (ClassDeclaration)
		String className = this.type.getName();
		Declaration classNameGet = _scope.get(className);
		if (!(classNameGet instanceof ClassDeclaration)) {
			Logger.error("(ContructorCall) La classe " + className + " n'existe pas.");
			return false;
		}
		ClassDeclaration classDeclaration = (ClassDeclaration) classNameGet;
		HierarchicalScope<Declaration> classLocals = classDeclaration.getLocals();

		// On calcule la signature du constructeur
		List<ParameterDeclaration> parameterDeclarations = new ArrayList<ParameterDeclaration>();
		for (Expression arg : this.arguments) {
			parameterDeclarations.add(new ParameterDeclaration(arg.toString(), arg.getType()));
		}
		Signature signature = new Signature(null, classDeclaration.getName(), parameterDeclarations);

		// On récupère le constructeur correspondant dans la classe (ConstructorDeclaration) depuis le scope de la classe
		Declaration signatureGet = classLocals.get(signature);
		if(!(signatureGet instanceof ConstructorDeclaration)) {
			Logger.error("(ContructorCall) Le constructeur " + signature + " n'existe pas pour la classe " + classDeclaration.getName() + ".");
			return false;
		}
		ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration) signatureGet;

		boolean funResolve = constructorDeclaration.resolveCE(_scope);
		boolean argResolves = true;
		for (Expression arg : this.arguments) {
			argResolves = argResolves && arg.fullResolve(_scope);
		}
		return funResolve && argResolves;
	}
	

	@Override
	public Type getType() {
		return this.type;
	}


	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment frag = _factory.createFragment();
		frag.addComment(this.toString());
		for (Expression e : this.arguments) {
			frag.append(e.getCode(_factory));
		}
		frag.add(_factory.createCall(this.type.getName(), Register.SB));
		return frag;
	}
}
