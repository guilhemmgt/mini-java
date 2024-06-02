package fr.n7.stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AttributeDeclaration implements ClassElement {

	private String name;
	private Type type;
	private Expression value;
	private boolean isFinal;
	private boolean isStatic;
	private AccessRight typeAcces = AccessRight.PUBLIC;
	private Register register;
	private int offset;

	public AttributeDeclaration(String name, Type type, Expression value, boolean isFinal, boolean isStatic) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.isFinal = isFinal;
		this.isStatic = isStatic;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		return type;
	}


	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Expression getValue() {
		return value;
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public AccessRight getTypeAcces() {
		return typeAcces;
	}

	@Override
	public void setTypeAcces(AccessRight a) {
		this.typeAcces = a;
	}

	@Override
	public boolean resolveCE(HierarchicalScope<Declaration> _scope) {
		//throw new UnsupportedOperationException("Unimplemented method 'resolveCE'");
		boolean resolved = true;
		if (this.value != null) {
			this.value.fullResolve(_scope);
		}
		return  resolved && type.resolve(_scope);
	}

	@Override
	public boolean collectCE(HierarchicalScope<Declaration> _scope) {
		if(_scope.accepts(this)){
			_scope.register(this);
			return this.value == null ? true : this.value.collectAndBackwardResolve(_scope);
		}
		else{
			Logger.error(this.name + " déjà utilisé dans ce scope.");
			return false;
		}
	}

	@Override
	public boolean checkType() {
		//throw new UnsupportedOperationException("Unimplemented method 'checkType'");
		boolean res = true;
		if (this.value != null) {
			Type valueType = this.value.getType();
			res = valueType.compatibleWith(this.type);
			System.out.println("CHECKTYPE (variabledeclaration): " + this.type + " vs " + valueType + " = " + res);
		}
		return res;
	}

	public Register getRegister() {
		//throw new UnsupportedOperationException("Unimplemented method 'getRegister'");
		return this.register;
	}
	
	public int getOffset() {
		//throw new UnsupportedOperationException("Unimplemented method 'getOffset'");
		return this.offset;
	}

	@Override
	public int allocateMemory(Register _register, int offset) {
		//throw new UnsupportedOperationException("Unimplemented method 'allocateMemory'");
		this.register = _register;
		this.offset = offset;
		return type.length();
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		//throw new UnsupportedOperationException("Unimplemented method 'getCode'");
		Fragment frag = _factory.createFragment();
		if (value != null) {
			frag.append(value.getCode(_factory));
		}
		return frag;
	}

}
