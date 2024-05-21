package fr.n7.stl.block.ast.instruction.declaration;

import java.util.List;

import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.scope.Declaration;;

public class Signature implements Declaration{
    private Type type;
    private String name;
    private List<ParameterDeclaration> parametres;

    public Signature(Type typeRetour, String name, List<ParameterDeclaration> parametres) {
        this.type = typeRetour;
        this.name = name;
        this.parametres = parametres;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<ParameterDeclaration> getParametres() {
        return parametres;
    }

    
}
