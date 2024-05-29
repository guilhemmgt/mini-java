package fr.n7.stl.block.ast.instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AccessRight;
import fr.n7.stl.tam.ast.Register;

public interface ClassElement extends Declaration {
    public AccessRight getTypeAcces();
    public void setTypeAcces(AccessRight a);

    public boolean resolveCE(HierarchicalScope<Declaration> _scope);
	public boolean collectCE(HierarchicalScope<Declaration> _scope);
    public boolean checkType();
    public int allocateMemory(Register _register, int offset);
}