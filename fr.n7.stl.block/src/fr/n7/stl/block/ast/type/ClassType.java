package fr.n7.stl.block.ast.type;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.AttributeDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.ConstructorDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.MethodDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.Signature;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;

public class ClassType implements Type, Declaration, Scope<ClassElement>{

	private List<ClassElement> elements;
	private boolean isAbstract;
	private String name;
	private ClassType inheritedClass; // null si pas héritée

	/**
	 * Constructor for a record type including fields.
	 * @param _name Name of the record type.
	 * @param _fields Sequence of attributes to initialize the content of the class type.
	 * @param _methods Sequence of methods to initialize the content of the class type.
	 * @param _constructors Sequence of methods to initialize the content of the class type.
	 * @param _isAbstract Boolean valued at true if the class is abstract 
	 */
	public ClassType(String _name, Iterable<ClassElement> _elements, boolean _isAbstract, ClassType inheritedClass) {
		this.name = _name;
		this.isAbstract = _isAbstract;
		this.inheritedClass = inheritedClass;

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
	public ClassElement get(String _name) {
		boolean _found = false;
		Iterator<ClassElement> _iter = this.elements.iterator();
		ClassElement _current = null;
		while (_iter.hasNext() && (! _found)) {
			_current = _iter.next();
			_found = _found || _current.getName().contentEquals(_name);
		}
		if (_found) {
			return _current;
		} else {
			return null;
		}
	}
	@Override
	public ClassElement get(Signature _signature) {
		boolean _found = false;
		Iterator<ClassElement> _iter = this.elements.iterator();
		ClassElement _current = null;
		while (_iter.hasNext() && (! _found)) {
			_current = _iter.next();
			if (_current instanceof MethodDeclaration) {
				MethodDeclaration md = (MethodDeclaration) _current;
				_found = _found || md.getEntete() == _signature;
			} else if (_current instanceof ConstructorDeclaration) {
				ConstructorDeclaration cd = (ConstructorDeclaration) _current;
				_found = _found || cd.getEntete() == _signature;
			}
		}
		if (_found) {
			return _current;
		} else {
			return null;
		}
	}

	@Override
	public boolean contains(String _name) {
		boolean _result = false;
		Iterator<ClassElement> _iter = this.elements.iterator();
		while (_iter.hasNext() && (! _result)) {
			_result = _result || _iter.next().getName().contentEquals(_name);
		}
		return _result;
	}
	@Override
	public boolean contains(Signature _signature) {
		boolean _result = false;
		Iterator<ClassElement> _iter = this.elements.iterator();
		while (_iter.hasNext() && (! _result)) {
			ClassElement next = _iter.next();
			if (next instanceof MethodDeclaration) {
				MethodDeclaration md = (MethodDeclaration) next;
				_result = _result || md.getEntete() == _signature;
			} else if (next instanceof ConstructorDeclaration) {
				ConstructorDeclaration cd = (ConstructorDeclaration) next;
				_result = _result || cd.getEntete() == _signature;
			}
		}
		return _result;
	}

	@Override
	public boolean accepts(ClassElement _element) {
		return ! this.contains(_element.getName());
	}

	@Override
	public void register(ClassElement _element) {
		if (this.accepts(_element)) {
			this.elements.add(_element);
		} else {
			throw new IllegalArgumentException();
		}
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
	public String toString() {
		String _result = "class " + this.name + " { ";
		Iterator<ClassElement> _iter = this.elements.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " " + _iter.next();
			}
		}
		return _result + "}";
	}

	@Override
	public boolean equalsTo(Type _other) {
		throw new SemanticsUndefinedException( "equalsTo is undefined in ClassType.");
	}

	@Override
	public boolean compatibleWith(Type _other) {
		// même classe + on regarde l'héritage
		throw new SemanticsUndefinedException( "compatibleWith is undefined in ClassType.");
	}

	@Override
	public Type merge(Type _other) {
		throw new SemanticsUndefinedException( "merge is undefined in ClassType.");
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
		boolean _result = true;
		for (ClassElement e : this.elements) {
			_result = _result && e.resolveCE(_scope);
		}
		return _result;
	}	

	public boolean collect(HierarchicalScope<Declaration> _scope) {
		boolean _result = true;
		for (ClassElement e : this.elements) {
			_result = _result && e.collectCE(_scope);
		}
		return _result;
	}

	@Override
	public ClassElement get(Signature _signature) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'get'");
	}

	@Override
	public boolean contains(Signature _signature) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'contains'");
	}
}
