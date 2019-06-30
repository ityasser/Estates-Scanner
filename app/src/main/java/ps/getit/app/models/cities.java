package ps.getit.app.models;

import java.util.ArrayList;

public class cities {

    public cities(String name, String name_en) {
        this.name = name;
        this.name_en=name_en;
    }

    public ArrayList<ps.getit.app.models.sections> getSections() {
        return sections;
    }

    public void setSections(ArrayList<ps.getit.app.models.sections> sections) {
        this.sections = sections;
    }

    private String id,name,name_en;
    private ArrayList<sections> sections;

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

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }


}
