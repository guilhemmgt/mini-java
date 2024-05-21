package fr.n7.stl.block.ast.instruction.declaration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.ClassElement;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;

public class ClassDeclaration implements Type, Declaration, Scope<AttributeDeclaration>{

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
	public ClassDeclaration(String _name, Iterable<ClassElement> _elements, boolean _isAbstract) {
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
	public ClassDeclaration(String _name) {
		this.name = _name;
		this.isAbstract = false;
		this.elements = new LinkedList<ClassElement>();	
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
