package sa.med.imc.myimc.HealthSummary.model;

public class SimpleNameIDModel {
    int icon;
    String name;
    String title;

    public SimpleNameIDModel(String title, String name, int id) {
        this.icon = id;
        this.name = name;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
