/**
 * 
 */
package fr.n7.stl.block.ast.scope;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.n7.stl.block.ast.instruction.declaration.ConstructorDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.MethodDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.Signature;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.util.Logger;

/**
 * Implementation of a hierarchical scope using maps.
 * @author Marc Pantel
 *
 */
public class SymbolTable implements HierarchicalScope<Declaration> {
	
	private Map<String, Declaration> declarations;
	private Map<Signature, Declaration> signDeclarations;
	
	private Scope<Declaration> context;

	public static MethodDeclaration methodDeclaration = null;
	public static ConstructorDeclaration constructorDeclaration = null;
	public static ClassDeclaration classDeclaration = null;

	public SymbolTable() {
		this( null );
	}
	
	public SymbolTable(Scope<Declaration> _context) {
		this.declarations = new HashMap<String,Declaration>();
		this.signDeclarations = new HashMap<Signature,Declaration>();
		this.context = _context;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#get(java.lang.String)
	 */
	@Override
	public Declaration get(String _name) {
		if (this.declarations.containsKey(_name)) {
			return this.declarations.get(_name);
		} else {
			if (this.context != null) {
				return this.context.get(_name);
			} else {
				return null;
			}
		}
	}
	@Override
	public Declaration get(Signature _signature) {
		for(Map.Entry<Signature, Declaration> entry : signDeclarations.entrySet()) {
			Signature sig = entry.getKey();
			if (sig.equals(_signature))
				return entry.getValue();
		}
		if (this.context != null) {
			return this.context.get(_signature);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String _name) {
		return (this.declarations.containsKey(_name));
	}
	@Override
	public boolean contains(Signature _signature) {
		for(Map.Entry<Signature, Declaration> entry : signDeclarations.entrySet()) {
			Signature sig = entry.getKey();
			if (sig.equals(_signature)) {
				System.out.println("MATCH: " + sig + " vs " + _signature);
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#accepts(fr.n7.stl.block.ast.scope.Declaration)
	 */
	@Override
	public boolean accepts(Declaration _declaration) {
		if (_declaration instanceof MethodDeclaration) {
			MethodDeclaration _methodDeclaration = (MethodDeclaration) _declaration;
			return (! this.contains(_methodDeclaration.getEntete()));
		} else if (_declaration instanceof ConstructorDeclaration) {
			ConstructorDeclaration _constructorDeclaration = (ConstructorDeclaration) _declaration;
			return (! this.contains(_constructorDeclaration.getEntete()));
		} else {
			return (! this.contains(_declaration.getName()));
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.Scope#register(fr.n7.stl.block.ast.scope.Declaration)
	 */
	@Override
	public void register(Declaration _declaration) {
		if (this.accepts(_declaration)) {
			
			if (_declaration instanceof MethodDeclaration) {
				MethodDeclaration _methodDeclaration = (MethodDeclaration) _declaration;
				System.out.println("REGISTER: " + _methodDeclaration.getEntete() + " (" + _methodDeclaration.getClass().getName().replace("fr.n7.stl.block.ast.", "") + ")");
				this.signDeclarations.put(_methodDeclaration.getEntete(), _methodDeclaration);
			} else if(_declaration instanceof ConstructorDeclaration) {
				ConstructorDeclaration _constructorDeclaration = (ConstructorDeclaration) _declaration;
				System.out.println("REGISTER: " + _constructorDeclaration.getEntete() + " (" + _constructorDeclaration.getClass().getName().replace("fr.n7.stl.block.ast.", "") + ")");
				this.signDeclarations.put(_constructorDeclaration.getEntete(), _constructorDeclaration);
			} else {
				System.out.println("REGISTER: " + _declaration.getName() + " (" + _declaration.getClass().getName().replace("fr.n7.stl.block.ast.", "") + ")");
				this.declarations.put(_declaration.getName(), _declaration);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.scope.HierarchicalScope#knows(java.lang.String)
	 */
	@Override
	public boolean knows(String _name) {
		if (this.contains(_name)) {
			return true;
		} else {
			if (this.context != null) {
				if (this.context instanceof HierarchicalScope<?>) {
					return ((HierarchicalScope<?>)this.context).knows(_name);
				} else {
					return this.context.contains(_name);
				}
			} else {
				return false;
			}
		}
	}
	@Override
	public boolean knows(Signature _signature) {
		if (this.contains(_signature)) {
			return true;
		} else {
			if (this.context != null) {
				if (this.context instanceof HierarchicalScope<?>) {
					return ((HierarchicalScope<?>)this.context).knows(_signature);
				} else {
					return this.context.contains(_signature);
				}
			} else {
				return false;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _local = "";
		if (this.context != null) {
			_local += "Hierarchical definitions :\n" + this.context.toString();
		}
		_local += "Local definitions : ";
		for (Entry<String,Declaration> _entry : this.declarations.entrySet()) {
			_local += _entry.getKey() + " -> " + _entry.getValue().toString() + "\n";
		}
		for (Entry<Signature,Declaration> _entry : this.signDeclarations.entrySet()) {
			_local += _entry.getKey() + " -> " + _entry.getValue().toString() + "\n";
		}
		return _local;
	}

}
