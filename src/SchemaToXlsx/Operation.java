package SchemaToXlsx;

import java.util.ArrayList;

public class Operation {

    private String name;
    private ArrayList<SchemaElement> elements;

    /************
     *  methods
     ************/

    public void add(SchemaElement schemaElement) {
        if (elements == null) {
            elements = new ArrayList<>();
        }

        elements.add(schemaElement);
    }

    /**********************
     *  Getter and Setter
     **********************/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SchemaElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<SchemaElement> elements) {
        this.elements = elements;
    }
}
