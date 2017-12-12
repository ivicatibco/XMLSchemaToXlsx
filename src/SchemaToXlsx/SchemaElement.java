package SchemaToXlsx;

import java.math.BigInteger;

public class SchemaElement {
    private String name;
    private String reference;
    private String path;
    private String elementName;
    private boolean complex;
    private BigInteger minOccurs;
    private String maxOccurs;
    private String type;
    private String restrictions;
    private String simpleType;
    private String description;
    private String necessity;

    /*****************
     *  constructors
     *****************/

    SchemaElement() {
        name = "";
        reference = "";
        path = "";
        elementName = "";
        complex = true;
        minOccurs = new BigInteger("1");
        maxOccurs = "1";
        type = "";
        restrictions = "";
        simpleType = "";
        description = "";
        necessity = "O";
    }

    SchemaElement(String name) {
        this();
        this.name = name;
        if (name.length() - name.replace("/", "").length() == 0) {
            elementName = name;
        } else {
            elementName = name.substring(name.lastIndexOf("/") + 1, name.length());
        }
    }

    SchemaElement(String name, BigInteger minOccurs, String maxOccurs) {
        this(name);
        setMinOccurs(minOccurs);
        setMaxOccurs(maxOccurs);
    }

    /************
     *  methods
     ************/

    @Override

    public String toString() {
        String printType = "";
        String printRestrictions = "";
        String printDescription = "";
        if (simpleType.length() > 0) {
            printType = ", Type: " + simpleType;
        }
        if (restrictions.length() > 0) {
            printRestrictions = ", Restrictions: " + restrictions;
        }
        if (description.length() > 0) {
            printDescription = ", Description: " + description;
        }

        return reference + "\t" + name + "\t (minOccurs: " + minOccurs + ", maxOccurs: " + maxOccurs + ", necessity: " + necessity + printType + printRestrictions + printDescription + ")";
    }


    /**********************
     *  Getter and Setter
     **********************/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (name.length() - name.replace("/", "").length() == 0) {
            elementName = name;
        } else {
            elementName = name.substring(name.lastIndexOf("/") + 1, name.length());
        }
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public boolean isComplex() {
        return complex;
    }

    public void setComplex(boolean complex) {
        this.complex = complex;
    }

    public BigInteger getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(BigInteger minOccurs) {
        this.minOccurs = minOccurs;
        if (minOccurs.compareTo(BigInteger.ZERO) == 0 && !necessity.equalsIgnoreCase("C")) {
            setNecessity("O");
        }
        if (minOccurs.compareTo(BigInteger.ZERO) > 0 && !necessity.equalsIgnoreCase("C")) {
            setNecessity("M");
        }
    }

    public String getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(String maxOccurs) {
        if (maxOccurs.equals("unbounded")) {
            maxOccurs = "*";
        }
        this.maxOccurs = maxOccurs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getSimpleType() {
        return simpleType;
    }

    public void setSimpleType(String simpleType) {
        this.simpleType = simpleType;
        complex = simpleType.length() <= 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (this.description.length() == 0) {
            this.description = description;
        }
    }

    public String getNecessity() {
        return necessity;
    }

    public void setNecessity(String occurrence) {
        if (occurrence.equalsIgnoreCase("M") || occurrence.equalsIgnoreCase("O") || occurrence.equalsIgnoreCase("C")) {
            this.necessity = occurrence.toUpperCase();
        } else {
            System.err.println(necessity + " is an invalid value for Element necessity.");
            System.exit(0);
        }
    }
}
