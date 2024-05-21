/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for accessing a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAccess extends AbstractField implements Expression {

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public FieldAccess(Expression _record, String _name) {
		super(_record, _name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment code = _factory.createFragment();

		List<FieldDeclaration> fields = ((RecordType)this.getRecordType()).getFields();
		FieldDeclaration thisField = null;
		int thisFieldOffset = 0;
		int totalOffset = 0;
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).getName().equals(this.name)) {
				thisField = fields.get(i);
				thisFieldOffset = totalOffset;
			}
			totalOffset += fields.get(i).getType().length();
		}
		
		code.append(this.record.getCode(_factory));
		code.add(_factory.createPop(0, totalOffset - thisFieldOffset - thisField.getType().length()));
		code.add(_factory.createPop(thisField.getType().length(), thisFieldOffset));

		return code;
	}

}
