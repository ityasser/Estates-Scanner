package points.comparison.app.models;

public class Properties {


private  String name,type,value;

private boolean isSelected;

    public Properties(String name, String type, String value, boolean isSelected) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
