package fr.n7.stl.block.ast.instruction.declaration;

import java.util.ArrayList;
import java.util.List;

import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;;

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
    
    @Override
	public String toString() {
        String params = "";
        for (ParameterDeclaration pd : parametres) {
            params += pd.getType() + ",";
        }
		return (this.type == null ? "null" : this.getName()) + ":" + this.name + "(" + params + ")";
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        Signature other = (Signature) obj;
        //System.out.println("EQUALS ??? " + other.toString() + " vs " + this.toString());
        boolean eqType = (other.getType() == this.type) || (other.getType() != null && other.getType().equalsTo(this.type));
        boolean eqName = other.getName().equals(this.name);
        boolean eqParams = other.getParametres().size() == this.parametres.size();
        for (int i = 0; i < this.parametres.size(); i++) {
            eqParams = eqParams && other.getParametres().get(i).getType() != null && other.getParametres().get(i).getType().equalsTo(this.parametres.get(i).getType());
        }
        //System.out.println(eqType + " " + eqName + " " + eqParams);
        return eqType && eqName && eqParams;
    }
}
