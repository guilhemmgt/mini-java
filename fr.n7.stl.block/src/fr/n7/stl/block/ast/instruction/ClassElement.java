package fr.n7.stl.block.ast.instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.type.AccessRight;

public interface ClassElement extends Declaration {
    public AccessRight typeAcces = null;
    public void setTypeAcces(AccessRight a);
}