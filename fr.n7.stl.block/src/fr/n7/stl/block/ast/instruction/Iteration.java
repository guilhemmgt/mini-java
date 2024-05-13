/**
 * 
 */
package fr.n7.stl.block.ast.instruction;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Iteration implements Instruction {

	protected Expression condition;
	protected Block body;

	public Iteration(Expression _condition, Block _body) {
		this.condition = _condition;
		this.body = _body;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "while (" + this.condition + " )" + this.body;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean condCollect = condition.collectAndBackwardResolve(_scope);
		boolean bodyCollect = body.collect(_scope);
		return condCollect && bodyCollect;
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in Iteration.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		boolean condResolve = condition.fullResolve(_scope);
		boolean bodyResolve = body.resolve(_scope);
		return condResolve && bodyResolve;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		boolean condType = this.condition.getType().compatibleWith(AtomicType.BooleanType);
		boolean bodyType = this.body.checkType();
		return condType && bodyType;
		// throw new SemanticsUndefinedException( "Semantics checkType is undefined in Iteration.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.body.allocateMemory(_register, _offset);
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		int labelNumber = _factory.createLabelNumber();
		String whileLabel = "while" + labelNumber;
		String endwhileLabel = "endwhile" + labelNumber;
		
		Fragment code = _factory.createFragment();
		
		// label while + évaluation de la condition: le résultat est en haut de la pile
		code.append(this.condition.getCode(_factory)); 
		code.addPrefix(whileLabel);
		
		// si faux (=0), on jump au else/endif. sinon, on continue
		code.add(_factory.createJumpIf(endwhileLabel, 0));
		
		// code body + jump while
		code.append(this.body.getCode(_factory));
		code.add(_factory.createJump(whileLabel));

		// label endwhile
		code.addSuffix(endwhileLabel);
		
		code.addComment("while");
		return code;
	}

}
