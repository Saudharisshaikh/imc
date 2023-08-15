package sa.med.imc.myimc.Network;

public class IDNameModel {
    String id;
    String name;
    public IDNameModel(String i,String na){
        id=i;
        name=na;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
