/**
 * 
 */
package fr.n7.stl.block.ast.instruction;

import java.util.Optional;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Conditional implements Instruction {

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch + ((this.elseBranch != null)?(" else " + this.elseBranch):"");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean condCollect = this.condition.collectAndBackwardResolve(_scope);
		boolean thenCollect = this.thenBranch.collect(_scope);
		boolean elseCollect = elseBranch == null ? true : this.elseBranch.collect(_scope); // créer nouveaux scopes ? jsp
		return condCollect && thenCollect && elseCollect;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		boolean condResolve = this.condition.fullResolve(_scope);
		boolean thenResolve = this.thenBranch.resolve(_scope);
		boolean elseResolve = this.elseBranch == null ? true : this.elseBranch.resolve(_scope);
		return condResolve && thenResolve && elseResolve;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		boolean condType = this.condition.getType().compatibleWith(AtomicType.BooleanType);
		boolean thenType = this.thenBranch.checkType();
		boolean elseType = this.elseBranch == null ? true : this.elseBranch.checkType();
		return condType && thenType && elseType;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.thenBranch.allocateMemory(_register, _offset);
		if (this.elseBranch != null) {
			this.elseBranch.allocateMemory(_register, _offset);
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment code = _factory.createFragment();
		
		int labelNumber = _factory.createLabelNumber();
		String elseLabel = "else" + labelNumber;
		String endifLabel = "endif" + labelNumber;
		
		// évaluation de la condition: le résultat est en haut de la pile
		code.append(this.condition.getCode(_factory)); 
		
		// si faux (=0), on jump au else/endif. sinon, on continue
		if (this.elseBranch != null) {
			code.add(_factory.createJumpIf(elseLabel, 0));
		} else {
			code.add(_factory.createJumpIf(endifLabel, 0));
		}
		
		// code then
		code.append(this.thenBranch.getCode(_factory)); 
		
		// s'il y a un else: jump endif + label else + code else
		if (this.elseBranch != null) {
			code.add(_factory.createJump(endifLabel));
			code.addSuffix(elseLabel);
			code.append(this.elseBranch.getCode(_factory));
		}

		// label endif
		code.addSuffix(endifLabel);
		
		code.addComment(this.elseBranch == null ? "if" : "if else");
		return code;
	}

}
