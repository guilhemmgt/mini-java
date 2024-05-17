package fr.n7.stl.block.ast.type;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.type.declaration.AttributeDeclaration;
import fr.n7.stl.block.ast.type.declaration.ConstructorDeclaration;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.block.ast.type.declaration.MethodDeclaration;

public class ClassType implements Type, Declaration, Scope<AttributeDeclaration>{

	private List<AttributeDeclaration> attributes;
	private List<MethodDeclaration> methods;
	private List<ConstructorDeclaration> constructors;
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
	public ClassType(String _name, Iterable<AttributeDeclaration> _attributes, Iterable<MethodDeclaration> _methods, Iterable<ConstructorDeclaration> _constructors, boolean _isAbstract) {
		this.name = _name;
		this.isAbstract = _isAbstract;
		
		// Init attributes
		this.attributes = new LinkedList<AttributeDeclaration>();
		for (AttributeDeclaration _attribute : _attributes) {
			this.attributes.add(_attribute);
		}
		
		// Init methods
		this.methods= new LinkedList<MethodDeclaration>();
		for (MethodDeclaration _method : _methods) {
			this.methods.add(_method);
		}
		
		// Init constructors
		this.constructors = new LinkedList<ConstructorDeclaration>();
		for (ConstructorDeclaration _constructor : _constructors) {
			this.constructors.add(_constructor);
		}
	}

	/**
	 * Constructor for an empty class type.
	 * @param _name Name of the class type.
	 */
	public ClassType(String _name) {
		this.name = _name;
		this.isAbstract = false;
		this.attributes = new LinkedList<AttributeDeclaration>();
		this.methods= new LinkedList<MethodDeclaration>();
		this.constructors = new LinkedList<ConstructorDeclaration>();		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equalsTo(Type _other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compatibleWith(Type _other) {
		// TODO Ajouter la notion d'héritage
		return false;
	}

	@Override
	public Type merge(Type _other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
