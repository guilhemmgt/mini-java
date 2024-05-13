/**
 * 
 */
package fr.n7.stl.block.ast.instruction;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.expression.UnaryOperator;
import fr.n7.stl.block.ast.expression.value.StringValue;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.CoupleType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a printer instruction.
 * @author Marc Pantel
 *
 */
public class Printer implements Instruction {

	protected Expression parameter;

	public Printer(Expression _value) {
		this.parameter = _value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "print " + this.parameter + ";\n";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean paramCollec = this.parameter.collectAndBackwardResolve(_scope);
		return paramCollec;
		// throw new SemanticsUndefinedException( "Semantics collect is undefined in Printer.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		boolean paramResolve = this.parameter.fullResolve(_scope);
		return paramResolve;
		//throw new SemanticsUndefinedException( "Semantics resolve is undefined in Printer.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		boolean result = false;
		for(Type atomicType : AtomicType.values()){
			result = result || parameter.getType().compatibleWith(atomicType);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment code = _factory.createFragment();
		code.append(this.parameter.getCode(_factory));
		Type paramType = this.parameter.getType();
		if (paramType == AtomicType.BooleanType) {
			code.add(Library.BOut);
		} else if (paramType == AtomicType.StringType) {
			// HACK bricolage (cf StringValue.getCode)
			for (int i = 1; i < ((StringValue)this.parameter).toString().length() - 1; i++) {
				code.add(Library.COut);
			}
		} else if (paramType == AtomicType.CharacterType) {
			code.add(Library.COut);
		} else {
			code.add(Library.IOut);
		}
		code.addComment(this.toString());
		
		return code;
	}

}
