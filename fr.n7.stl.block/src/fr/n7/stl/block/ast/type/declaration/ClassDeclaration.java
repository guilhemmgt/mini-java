package fr.n7.stl.block.ast.type.declaration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.AttributeDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.ConstructorDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.MethodDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.Signature;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.block.ast.type.ClassType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class ClassDeclaration implements Declaration, Scope<ClassElement>{
	private HierarchicalScope<Declaration> locals;
	private List<ClassElement> elements;
	private boolean isAbstract;
	private String name;
	private ClassType inheritedClassType; // null si pas héritée
	private ClassDeclaration inheritedClass; // pas de valeur avant resolve
	private int size;

	/**
	 * Constructor for a record type including fields.
	 * @param _name Name of the record type.
	 * @param _fields Sequence of attributes to initialize the content of the class type.
	 * @param _methods Sequence of methods to initialize the content of the class type.
	 * @param _constructors Sequence of methods to initialize the content of the class type.
	 * @param _isAbstract Boolean valued at true if the class is abstract 
	 */
	public ClassDeclaration(String _name, Iterable<ClassElement> _elements, boolean _isAbstract, ClassType inheritedClassType) {
		this.name = _name;
		this.isAbstract = _isAbstract;
		this.inheritedClassType = inheritedClassType;

		this.elements = new LinkedList<ClassElement>();
		for (ClassElement _element : _elements) {
			this.elements.add(_element);
		}
	}

	/**
	 * Constructor for an empty class type.
	 * @param _name Name of the class type.
	 */
	public ClassDeclaration(String _name) {
		this.name = _name;
		this.isAbstract = false;
		this.elements = new LinkedList<ClassElement>();	
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public HierarchicalScope<Declaration> getLocals() {
		return this.locals;
	}

	public List<ClassElement> getElements () {
		return this.elements;
	}

	public ClassType getInheritedClassType () {
		return this.inheritedClassType;
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
				_found = _found || md.getEntete().equals(_signature);
			} else if (_current instanceof ConstructorDeclaration) {
				ConstructorDeclaration cd = (ConstructorDeclaration) _current;
				_found = _found || cd.getEntete().equals(_signature);
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
				_result = _result || md.getEntete().equals(_signature);
			} else if (next instanceof ConstructorDeclaration) {
				ConstructorDeclaration cd = (ConstructorDeclaration) next;
				_result = _result || cd.getEntete().equals(_signature);
			}
		}
		System.out.println("CLASSMATCH");
		return _result;
	}

	@Override
	public boolean accepts(ClassElement _element) {
		if (_element instanceof MethodDeclaration) {
			MethodDeclaration md = (MethodDeclaration) _element;
			return ! this.contains(md.getEntete());
		} else if (_element instanceof ConstructorDeclaration) {
			ConstructorDeclaration cd = (ConstructorDeclaration) _element;
			return ! this.contains(cd.getEntete());
		} else {
			return ! this.contains(_element.getName());
		}
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
		return new ClassType(this);
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

	public boolean collect(HierarchicalScope<Declaration> _scope) {
		if(_scope.accepts(this)){
			_scope.register(this);
			this.locals = new SymbolTable (_scope);

			SymbolTable.classDeclaration = this;

			boolean _result = true;
			for (ClassElement e : this.elements) {
				_result = _result && e.collectCE(locals);
			}
			return _result;
		} else {
			Logger.error("(ClassDeclaration) " + this.name + " déjà utilisé dans ce scope.");
			return false;
		}
	}

	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean _result = true;

		if (inheritedClassType != null) {
			// On récupère la classe dont on hérite (ClassDeclaration)
			String className = this.inheritedClassType.getName();
			System.out.println("[EXTENDS] heriting from " + className + " to " + this.name);
			Declaration classNameGet = _scope.get(className);
			if (!(classNameGet instanceof ClassDeclaration)) {
				Logger.error("(ClassDeclaration) La classe " + className + " n'existe pas.");
				return false;
			}
			this.inheritedClass = (ClassDeclaration) classNameGet;
			// On récupère tous les classelements auxquels on aura accès
			List<ClassElement> heritedElements = new ArrayList<ClassElement>();
			for (ClassElement e : this.inheritedClass.getElements()) {
				if (e.getTypeAcces() == AccessRight.PROTEGE || e.getTypeAcces() == AccessRight.PUBLIC)
					heritedElements.add(e);
			}
			// On collect et on resolve
			for (ClassElement e : heritedElements) {
				_result = _result && e.collectCE(this.locals);
			}
			System.out.println("[EXTENDS] collected");
			for (ClassElement e : heritedElements) {
				_result = _result && e.resolveCE(this.locals);
			}
			System.out.println("[EXTENDS] " + heritedElements.size() + " ce herited from " + className + " to " + this.name);
		}

		for (ClassElement e : this.elements) {
			_result = _result && e.resolveCE(this.locals);
		}
		return _result;
	}	


	public boolean checkType() {
		boolean _result = true;
		for (ClassElement e : this.elements) {
			_result = _result && e.checkType();
		}
		return _result;
	}

	public int allocateMemory(Register _register, int _offset) {
	int offset = _offset;
	for (ClassElement e : this.elements) {
		offset += e.allocateMemory(_register, offset);
	}
	this.size = offset - _offset;
	System.out.println("ALLOCATEMEMORY (block): allocated " + this.size);
	return offset;
	}

	public Fragment getCode(TAMFactory _factory) {
		Fragment code = _factory.createFragment();
		for (ClassElement e : this.elements) {
			code.append(e.getCode(_factory));
		}
		return code;
	}

}
