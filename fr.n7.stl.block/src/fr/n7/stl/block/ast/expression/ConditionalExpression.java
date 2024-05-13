/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for a conditional expression.
 * @author Marc Pantel
 *
 */
public class ConditionalExpression implements Expression {

	/**
	 * AST node for the expression whose value is the condition for the conditional expression.
	 */
	protected Expression condition;
	
	/**
	 * AST node for the expression whose value is the then parameter for the conditional expression.
	 */
	protected Expression thenExpression;
	
	/**
	 * AST node for the expression whose value is the else parameter for the conditional expression.
	 */
	protected Expression elseExpression;
	
	/**
	 * Builds a binary expression Abstract Syntax Tree node from the left and right sub-expressions
	 * and the binary operation.
	 * @param _left : Expression for the left parameter.
	 * @param _operator : Binary Operator.
	 * @param _right : Expression for the right parameter.
	 */
	public ConditionalExpression(Expression _condition, Expression _then, Expression _else) {
		this.condition = _condition;
		this.thenExpression = _then;
		this.elseExpression = _else;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		boolean condCollect = this.condition.collectAndBackwardResolve(_scope);
		boolean thenCollect = this.thenExpression.collectAndBackwardResolve(_scope);
		boolean elseCollect = this.elseExpression.collectAndBackwardResolve(_scope);
		return condCollect && thenCollect && elseCollect;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		boolean condResolve = this.condition.fullResolve(_scope);
		boolean thenResolve = this.thenExpression.fullResolve(_scope);
		boolean elseResolve = this.elseExpression.fullResolve(_scope);
		return condResolve && thenResolve && elseResolve;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.condition + " ? " + this.thenExpression + " : " + this.elseExpression + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		boolean condType = this.condition.getType().compatibleWith(AtomicType.BooleanType);
		
		if (!condType) {
			Logger.warning("Type error in conditional expression : " + this.condition + " parameter " + this.condition.getType() + "\n");
			return AtomicType.ErrorType;
		}
		
		Type resType = this.thenExpression.getType().merge(this.elseExpression.getType());
		
		return resType;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment code = _factory.createFragment();
		int labelNumber = _factory.createLabelNumber();
		String elseLabel = "condexElse" + labelNumber;
		String endLabel = "condexEnd" + labelNumber;
		
		code.append(this.condition.getCode(_factory)); // code condition
		code.add(_factory.createJumpIf(elseLabel, 0)); // si faux, on va au else, sinon on continue
		code.append(this.thenExpression.getCode(_factory)); // code then
		code.add(_factory.createJump(endLabel)); // jump end
		code.addSuffix(elseLabel); // label else
		code.append(this.elseExpression.getCode(_factory)); // code else
		code.addSuffix(endLabel); // label end
		
		return code;
	}

}
