package fr.n7.stl.block.ast.type;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.AttributeDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;

public class ClassType implements Type, Declaration, Scope<AttributeDeclaration>{

	private List<ClassElement> elements;
	private boolean isAbstract;
	private String name;

	/**
	 * Constructor for a record type including fields.
	 * @param _name Name of the record type.
	 * @param _fields Sequence of attributes to initialize the content of the class type.
	 * @param _methods Sequence of methods to initialize the content of the class type.
	 * @param _constructors Sequence of methods to initialize the content of the class type.
	 * @param _isAbstract Boolean valued at true if the class is abstract 
	 */
	public ClassType(String _name, Iterable<ClassElement> _elements, boolean _isAbstract) {
		this.name = _name;
		this.isAbstract = _isAbstract;

		this.elements = new LinkedList<ClassElement>();
		for (ClassElement _element : _elements) {
			this.elements.add(_element);
		}
	}

	/**
	 * Constructor for an empty class type.
	 * @param _name Name of the class type.
	 */
	public ClassType(String _name) {
		this.name = _name;
		this.isAbstract = false;
		this.elements = new LinkedList<ClassElement>();	
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	@Override
	public AttributeDeclaration get(String _name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(String _name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accepts(AttributeDeclaration _declaration) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void register(AttributeDeclaration _declaration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {
		return this;
	}

	@Override
	public boolean equalsTo(Type _other) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'equalsTo'");
	}

	@Override
	public boolean compatibleWith(Type _other) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'compatibleWith'");
	}

	@Override
	public Type merge(Type _other) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'merge'");
	}

	@Override
	public int length() {
		int _length = 0;
		for (ClassElement e : this.elements) {
			_length += e.getType().length();
		}
		return _length;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'resolve'");
	}	

	public boolean collect(HierarchicalScope<Declaration> _scope) {
		throw new SemanticsUndefinedException("Semantics collect is undefined in ClassType.");
	}
}
