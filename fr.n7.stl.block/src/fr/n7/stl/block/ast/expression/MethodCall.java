/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.MethodDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class MethodCall implements Expression, Instruction {

	protected MethodDeclaration methode;
	protected List<Expression> arguments;
	
	/**
	 * @param _methode : called function.
	 * @param _arguments : List of AST nodes that computes the values of the parameters for the function call.
	 */
	public MethodCall(MethodDeclaration _methode, List<Expression> _arguments) {
		this.methode = _methode;
		this.arguments = _arguments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = ((this.methode == null)?this.methode.getName():this.methode) + "( ";
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
		//throw new SemanticsUndefinedException( "Semantics getType is undefined in FunctionCall.");
		return this.methode.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		//throw new SemanticsUndefinedException( "Semantics getCode is undefined in FunctionCall.");
		Fragment frag = _factory.createFragment();
		frag.addComment(this.toString());
		for (Expression e : this.arguments) {
			frag.append(e.getCode(_factory));
		}
		frag.add(_factory.createCall(this.methode.getName(), Register.SB));
		return frag;
	}

	@Override
	public boolean checkType() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'checkType'");
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'allocateMemory'");
	}
}
