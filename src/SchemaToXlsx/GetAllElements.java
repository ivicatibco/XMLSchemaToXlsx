package SchemaToXlsx;

import org.w3._2001.xmlschema.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GetAllElements {
    private boolean debug = false;
    private String fileName;// = "C:/SVN/JointVentureService/trunk/Services/src/JointVentureService_BW/Schemas/IDM/ESBBusinessObjects/Sales_1_0_0/JointVenture_1_0_0/JointVentureTransferObjects_1_0_0/JointVentureTransferObjects_1_0_0.xsd";
    private final String schemaFileName = "/Users/ivisuker/LHC/JavaProjects/Schemas/XMLSchema.xsd";
    private static final String TYPES = "string,boolean,float,double,decimal,dateTime,time,date,long,int,short,byte";
    private HashMap<String, String> namespaceMap;
    private String rootName;
    private ArrayList<SchemaElement> elementList;
    private ArrayList<Operation> operationList;
    private Schema schema;
    private static final String NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema";

    public static void main(String[] args) {
        String getAllElementsFileName = "C:/SVN/ShipmentService/prod/Services/src/ShipmentServiceBW/ServiceResources/XMLSchemas/ExternalRepositories/iCargo/wsdl/operations/shipment/external/OperationsShipmentServicesExtTypes.xsd";

        new GetAllElements(getAllElementsFileName);
    }

    private GetAllElements(String fileName) {
        namespaceMap = new HashMap<>();

        this.fileName = fileName;
        File schemaFile = new File(schemaFileName);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        javax.xml.validation.Schema valSchema = null;
        try {
            valSchema = sf.newSchema(schemaFile);
        } catch (SAXException e) {

            System.exit(0);
        }

        JAXBContext jc;
        schema = null;

        try {
            jc = JAXBContext.newInstance(Schema.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setSchema(valSchema);
            File xml = new File(fileName);
            schema = (Schema) unmarshaller.unmarshal(xml);
        } catch (JAXBException e) {

            System.exit(0);
        }

        createNamespaceMap(); // name: "namespaceMap"

        operationList = new ArrayList<>();

        getElementsFromSchema(schema);

    } // end constructor

    GetAllElements(String fileName, boolean debug) {
        this(fileName);
        this.debug = debug;
    }


    private GetAllElements(SchemaElement schemaElement, String path, String rootName, ArrayList<SchemaElement> elementList) {
        namespaceMap = new HashMap<>();
        fileName = path;
        this.rootName = rootName;
        this.elementList = elementList;

        File schemaFile = new File(schemaFileName);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        javax.xml.validation.Schema valSchema = null;
        try {
            valSchema = sf.newSchema(schemaFile);
        } catch (SAXException e) {

            System.exit(0);
        }

        JAXBContext jc;
        schema = null;

        try {
            jc = JAXBContext.newInstance(Schema.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setSchema(valSchema);
            File xml = new File(path);
            schema = (Schema) unmarshaller.unmarshal(xml);
        } catch (JAXBException e) {

            System.exit(0);
        }

        createNamespaceMap(); // name: "namespaceMap"

        getSpecificElementFromSchema(schemaElement);
    }

    private void getElementsFromSchema(Schema schema) {

        for (OpenAttrs element : schema.getSimpleTypeOrComplexTypeOrGroup()) {

            if (element instanceof TopLevelElement) {

                elementList = new ArrayList<>();
                Operation operation = new Operation();

                TopLevelElement tElement = (TopLevelElement) element;
                if (debug) {
                    System.out.println("\n" + getLineNumber() + "\t" + tElement.getName() + "\t\t (minOccurs: " + tElement.getMinOccurs() + "; maxOccurs: " + tElement.getMaxOccurs() + ")" + "\t" + tElement.getType());
                }

                operation.setName(tElement.getName());
                SchemaElement schemaElement = new SchemaElement(tElement.getName(), tElement.getMinOccurs(), tElement.getMaxOccurs());
                if (debug) {
                    System.out.println(getLineNumber() + "\t" + tElement + "\t" + tElement.getComplexType());
                }
                schemaElement.setType(tElement.getType().getLocalPart());
                rootName = tElement.getName() + "/";

                String fullName = rootName + tElement.getName();

                if (tElement.getType() != null && tElement.getType().getNamespaceURI().equals(NAMESPACE_URI)) {
                    schemaElement.setType(tElement.getType().getLocalPart());
                    try {
                        Documentation documentation = (Documentation) tElement.getAnnotation().getAppinfoOrDocumentation().get(0);
                        schemaElement.setDescription(documentation.getContent().get(0).toString());
                        //System.out.println(getLineNumber());//TODO
                    } catch (NullPointerException | IndexOutOfBoundsException ignored) {
                    }
                    if (!TYPES.contains(schemaElement.getType()) && isElementInSchema(schemaElement.getType())) {
                        getChildElements(schemaElement);
                    } else {
                        elementList.add(schemaElement);
                    }
                } else if (tElement.getType() != null && tElement.getType().getNamespaceURI().equals(schema.getTargetNamespace())) {
                    try {
                        Documentation documentation = (Documentation) tElement.getAnnotation().getAppinfoOrDocumentation().get(0);
                        schemaElement.setDescription(documentation.getContent().get(0).toString());
                        //System.out.println(getLineNumber());//TODO
                    } catch (NullPointerException | IndexOutOfBoundsException ignored) {

                    }
                    elementList.add(schemaElement);
                    getChildElements(schemaElement);
                } else if (tElement.getComplexType() != null) {
                    //System.out.println(getLineNumber() + "\t" + tElement.getName() + "\t c");// TODO
                    elementList.add(schemaElement);
                    for (Object objectFromList : tElement.getComplexType().getSequence().getParticle()) {
                        @SuppressWarnings("rawtypes")
                        JAXBElement elementFromList = (JAXBElement) objectFromList;
                        LocalElement localElement = (LocalElement) elementFromList.getValue();
                        SchemaElement localSchemaElement = new SchemaElement();

                        if (localElement.getName() != null) {
                            localSchemaElement.setName(localElement.getName());
                        } else if (localElement.getRef() != null) {
                            localSchemaElement.setName(localElement.getRef().getLocalPart());
                        }
                        localSchemaElement.setPath(rootName);
                        if (localElement.getRef() != null) {
                            localSchemaElement.setType(localElement.getRef().getLocalPart());
                        }

                        if (localElement.getRef() != null && namespaceMap.containsKey(localElement.getRef().getNamespaceURI())) {
                            GetAllElements getAllElements = new GetAllElements(localSchemaElement, namespaceMap.get(localElement.getRef().getNamespaceURI()), localSchemaElement.getPath(), elementList);
                            elementList = getAllElements.getElementList();
                        } else {
                            GetAllElements getAllElements = new GetAllElements(localSchemaElement, fileName, localSchemaElement.getPath(), elementList);
                            if (getAllElements.getElementList() != null) {
                                elementList = getAllElements.getElementList();
                            }
                        }

                    }//end for
                } else if (tElement.getSimpleType() != null) {
                    schemaElement.setType(tElement.getSimpleType().getRestriction().getBase().getLocalPart());
                    try {
                        Documentation documentation = (Documentation) tElement.getAnnotation().getAppinfoOrDocumentation().get(0);
                        schemaElement.setDescription(documentation.getContent().get(0).toString());
                        //System.out.println(getLineNumber());//TODO
                    } catch (NullPointerException | IndexOutOfBoundsException ignored) {

                    }
                    elementList.add(schemaElement);
                } else if (tElement.getType() != null && namespaceMap.containsKey(tElement.getType().getNamespaceURI())) {
                    //System.out.println(getLineNumber());//TODO
                    schemaElement.setType(tElement.getType().getLocalPart());
                    String namespacePath = namespaceMap.get(tElement.getType().getNamespaceURI());
                    String path = fileName.substring(0, fileName.lastIndexOf("/") + 1) + namespacePath;
                    GetAllElements getAllElements = new GetAllElements(schemaElement, path, fullName + "/", elementList);
                    elementList = getAllElements.getElementList();
                } else {
                    //System.out.println(getLineNumber());//TODO
                    elementList.add(schemaElement);
                }

                operation.setElements(elementList);

                operationList.add(operation);

                printElementList(operation.getElements()); //TODO                     ************** print List *****************

            } // end if (element instanceof TopLevelElement)


        } // end for
    } // end getElementsFromSchema()

    private boolean isElementInSchema(String elementName) {
        for (OpenAttrs element : schema.getSimpleTypeOrComplexTypeOrGroup()) {
            if (element instanceof TopLevelElement) {
                if (((TopLevelElement) element).getName().equals(elementName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getChildElements(SchemaElement parentSchemaElement) {

        for (OpenAttrs element : schema.getSimpleTypeOrComplexTypeOrGroup()) {

            if (element != null) {

                if (element instanceof TopLevelComplexType) {
                    TopLevelComplexType tlcType = (TopLevelComplexType) element;

                    if (tlcType.getName().equals(parentSchemaElement.getType())) {

                        getAllParticles(tlcType);

                    }// end if (tlcType.getName().equals(parentSchemaElement.getName()))
                }// end if (element instanceof TopLevelComplexType)

                if (element instanceof TopLevelElement) {

                    TopLevelElement tlElement = (TopLevelElement) element;

                    if (tlElement.getName().equals(parentSchemaElement.getType())) {

                        if (tlElement.getComplexType() != null) {
                            LocalComplexType lcType = tlElement.getComplexType();
                            getAllParticles(lcType);
                        } // end if(tlElement.getComplexType() != null)

                        else if (tlElement.getRef() != null) {
                            if (tlElement.getRef().getNamespaceURI().equals(schema.getTargetNamespace())) {
                                SchemaElement refSchemaElement = new SchemaElement(tlElement.getRef().getLocalPart());
                                refSchemaElement.setType(tlElement.getRef().getLocalPart());
                                getSpecificElementFromSchema(refSchemaElement);
                            }
                        } else if (tlElement.getSimpleType() != null) {
                            SimpleType sType = tlElement.getSimpleType();
                            if (sType.getRestriction().getBase().getNamespaceURI().equals(NAMESPACE_URI)) {
                                SchemaElement lastElement = elementList.remove(elementList.size() - 1);
                                lastElement.setSimpleType(sType.getRestriction().getBase().getLocalPart());
                                try {
                                    Documentation documentation = (Documentation) sType.getAnnotation().getAppinfoOrDocumentation().get(0);
                                    lastElement.setDescription(documentation.getContent().get(0).toString());
                                    System.out.println(getLineNumber());//TODO
                                } catch (NullPointerException | IndexOutOfBoundsException ignored) {

                                }
                                elementList.remove(lastElement);
                                getRestrictions(lastElement, sType);
                                elementList.add(lastElement);
                            }
                        } else {
                            if (tlElement.getType().getNamespaceURI().equals(NAMESPACE_URI)) {
                                SchemaElement lastElement = elementList.remove(elementList.size() - 1);
                                lastElement.setSimpleType(tlElement.getType().getLocalPart());
                                try {
                                    Documentation documentation = (Documentation) tlElement.getAnnotation().getAppinfoOrDocumentation().get(0);
                                    lastElement.setDescription(documentation.getContent().get(0).toString());
                                } catch (NullPointerException | IndexOutOfBoundsException ignored) {

                                }
                                elementList.add(lastElement);
                            }
                        }

                    }

                }// end if (element instanceof TopLevelComplexType)
            }// end if (element != null)
        }// end for
    }


    private void getSpecificElementFromSchema(SchemaElement parentSchemaElement) {

        if (elementList == null) {
            elementList = new ArrayList<>();
        }

        for (OpenAttrs element : schema.getSimpleTypeOrComplexTypeOrGroup()) {

            if (element != null) {

                if (element instanceof TopLevelElement) {
                    TopLevelElement tElement = (TopLevelElement) element;
                    if (tElement.getName().equals(parentSchemaElement.getType().substring(parentSchemaElement.getType().lastIndexOf("/") + 1, parentSchemaElement.getType().length()))) {
                        SchemaElement schemaElement = new SchemaElement(parentSchemaElement.getName(), tElement.getMinOccurs(), tElement.getMaxOccurs());
                        schemaElement.setType(tElement.getType().getLocalPart());

                        try {
                            Documentation documentation = (Documentation) tElement.getAnnotation().getAppinfoOrDocumentation().get(0);
                            schemaElement.setDescription(documentation.getContent().get(0).toString());
                        } catch (NullPointerException ignored) {

                        }
                        elementList.add(schemaElement);

                        getChildElements(schemaElement);

                    }
                } else if (element instanceof ComplexType) {
                    TopLevelComplexType cType = (TopLevelComplexType) element;

                    if (cType.getName().equalsIgnoreCase(parentSchemaElement.getType())) {
                        if (debug) {
                            System.out.println(getLineNumber() + "\t" + cType.getName());
                        }

                        try {
                            Documentation documentation = (Documentation) cType.getAnnotation().getAppinfoOrDocumentation().get(0);
                            parentSchemaElement.setDescription(documentation.getContent().get(0).toString());
                        } catch (NullPointerException | ClassCastException | IndexOutOfBoundsException ignored) {

                        }

                        elementList.add(parentSchemaElement);

                        if (cType.getChoice() != null) {
                            System.out.println(getLineNumber() + "\t" + cType.getChoice() + "\t" + cType.getSequence());//TODO
                            getChoiceElements(cType, cType.getChoice());
                        }

                        if (cType.getSequence() != null) {
                            getAllParticles(cType);
                        }

                    }// end if (cType.getName().equalsIgnoreCase(elementName))
                } // end if (element instanceof ComplexType)

                else if (element instanceof TopLevelSimpleType) {

                    TopLevelSimpleType sType = (TopLevelSimpleType) element;
                    if (sType.getName().equals(parentSchemaElement.getType())) {

                        try {
                            Documentation documentation = (Documentation) sType.getAnnotation().getAppinfoOrDocumentation().get(0);
                            elementList.remove(parentSchemaElement);
                            parentSchemaElement.setDescription(documentation.getContent().toString());
                            elementList.add(parentSchemaElement);
                        } catch (NullPointerException ignored) {

                        }

                        elementList.remove(parentSchemaElement);
                        getRestrictions(parentSchemaElement, sType);
                        elementList.add(parentSchemaElement);

                    }// end if (sType.getName().equals(parentSchemaElement.getType()))
                }// end if (element instanceof TopLevelSimpleType)
            } // end if (element != null){
        } // end for

    }

    private void getChoiceElements(ComplexType cType, ExplicitGroup group) {
        for (Object object2 : group.getParticle()) {
            @SuppressWarnings("rawtypes")
            JAXBElement jaxb2 = (JAXBElement) object2;
            LocalElement lElement = (LocalElement) jaxb2.getValue();
            int listSize = elementList.size();
            getSequenceElementType(lElement, cType);
            if (elementList.size() > listSize) {
                elementList.get(listSize).setNecessity("C");
            }
        }
    }

    private void getRestrictions(SchemaElement schemaElement, SimpleType sType) {
        if (sType.getRestriction() != null) {
            if (sType.getRestriction().getBase() != null) {
                if (sType.getRestriction().getBase().getNamespaceURI().equals(NAMESPACE_URI) || sType.getRestriction().getBase().getNamespaceURI().equals(schema.getTargetNamespace())) {
                    try {
                        //elementList.remove(schemaElement);
                        schemaElement.setSimpleType(sType.getRestriction().getBase().getLocalPart());
                        //elementList.add(schemaElement);
                    } catch (ArrayIndexOutOfBoundsException ignored) {

                    }
                }
            }
            if (sType.getRestriction().getFacets() != null) {
                StringBuilder restrictions = new StringBuilder();
                ArrayList<String> enumeration = new ArrayList<>();
                for (Object object : sType.getRestriction().getFacets()) {

                    if (object instanceof Pattern) {
                        Pattern pattern = (Pattern) object;
                        if (!restrictions.toString().equals("")) {
                            restrictions.append(", ");
                        }
                        restrictions.append("Pattern: ").append(pattern.getValue());
                    }
                    if (object instanceof JAXBElement) {
                        @SuppressWarnings("rawtypes")
                        JAXBElement jaxb = (JAXBElement) object;
                        Facet facet = (Facet) jaxb.getValue();
                        if (!restrictions.toString().equals("")) {
                            restrictions.append(", ");
                        }
                        if (jaxb.getName().getLocalPart().equals("enumeration")) {
                            enumeration.add(facet.getValue());
                        } else {
                            restrictions.append(jaxb.getName().getLocalPart()).append(": ").append(facet.getValue());
                        }
                    }
                }// end for

                if (!enumeration.isEmpty()) {
                    if (!restrictions.toString().equals("")) {
                        restrictions.append(", ");
                    }
                    restrictions.append("Enumeration: ");
                    for (String entry : enumeration) {
                        restrictions.append(entry).append(", ");
                    }
                    restrictions = new StringBuilder(restrictions.substring(0, restrictions.length() - 2));
                }


                //elementList.remove(schemaElement);
                schemaElement.setRestrictions(restrictions.toString());
                //elementList.add(schemaElement);

            }// end if (sType.getRestriction().getFacets() != null)
        }// end if (sType.getRestriction() != null)
    }

    private void getAllParticles(ComplexType cType) {

        if (cType.getSequence() != null) {

            for (Object object : cType.getSequence().getParticle()) {
                @SuppressWarnings("rawtypes")
                JAXBElement jaxb = (JAXBElement) object;
                //System.out.println(getLineNumber() + "\t" + cType.getName() + "\t" + jaxb.getValue() + "\t"); //TODO

                if (jaxb.getValue() instanceof LocalElement) {
                    LocalElement lElement = (LocalElement) jaxb.getValue();
                    getSequenceElementType(lElement, cType);
                } else if (jaxb.getValue() instanceof ExplicitGroup) {
                    ExplicitGroup eGroup = (ExplicitGroup) jaxb.getValue();
                    for (Object object2 : eGroup.getParticle()) {
                        @SuppressWarnings("rawtypes")
                        JAXBElement jaxb2 = (JAXBElement) object2;
                        LocalElement lElement = (LocalElement) jaxb2.getValue();
                        int listSize = elementList.size();
                        getSequenceElementType(lElement, cType);
                        if (elementList.size() > listSize) {
                            elementList.get(listSize).setNecessity("C");
                        }
                    }
                }

            }// end for
        }// end if(cType.getSequence() != null)

        else if (cType.getComplexContent() != null && cType.getComplexContent().getExtension() != null) {
            //System.out.println(getLineNumber() + "\t" + cType.getName() + "\t" + cType.getComplexContent().getExtension().getBase()); //TODO
            SchemaElement sElement = new SchemaElement(rootName.substring(0, rootName.length() - 1));
            sElement.setType(cType.getComplexContent().getExtension().getBase().getLocalPart());
            if (cType.getComplexContent().getExtension().getBase().getNamespaceURI().equals(schema.getTargetNamespace())) {
                getSpecificElementFromSchema(sElement);
                elementList.remove(sElement);
            } else if (namespaceMap.containsKey(cType.getComplexContent().getExtension().getBase().getNamespaceURI())) {
                GetAllElements getAllElements = new GetAllElements(sElement, namespaceMap.get(cType.getComplexContent().getExtension().getBase().getNamespaceURI()), rootName, elementList);
                elementList.addAll(getAllElements.getElementList());
                elementList.remove(sElement);
            }

            for (Object object : cType.getComplexContent().getExtension().getSequence().getParticle()) {
                @SuppressWarnings("rawtypes")
                JAXBElement jaxb = (JAXBElement) object;
                LocalElement lElement = (LocalElement) jaxb.getValue();
                getSequenceElementType(lElement, cType);

            }
        }
    }

    private void createNamespaceMap() {

        for (int i = 0; i < schema.getIncludeOrImportOrRedefine().size(); i++) {
            if (schema.getIncludeOrImportOrRedefine().get(i).getClass() == org.w3._2001.xmlschema.Import.class) {
                Import schemaImport = (Import) schema.getIncludeOrImportOrRedefine().get(i);
                namespaceMap.put(schemaImport.getNamespace(), schemaImport.getSchemaLocation());
            }
        }
    }// end createNamespaceMap()

    private void getSequenceElementType(LocalElement lElement, ComplexType cType) {
        String fullName = null;
        SchemaElement schemaElement = new SchemaElement();
        QName type = null;

        if (lElement.getName() != null) {
            fullName = rootName + lElement.getName();
            schemaElement = new SchemaElement(fullName, lElement.getMinOccurs(), lElement.getMaxOccurs());
            if (lElement.getSimpleType() != null) {
                getRestrictions(schemaElement, lElement.getSimpleType());
                type = lElement.getSimpleType().getRestriction().getBase();
            } else {
                schemaElement.setType(lElement.getType().getLocalPart());
                type = lElement.getType();
            }

        } else if (lElement.getRef() != null) {
            fullName = rootName + lElement.getRef().getLocalPart();
            schemaElement = new SchemaElement(fullName, lElement.getMinOccurs(), lElement.getMaxOccurs());
            schemaElement.setType(lElement.getRef().getLocalPart());
            type = lElement.getRef();
        }

        try {
            Documentation documentation = (Documentation) cType.getAnnotation().getAppinfoOrDocumentation().get(0);
            schemaElement.setDescription(documentation.getContent().get(0).toString());
        } catch (NullPointerException | IndexOutOfBoundsException ignored) {

        }

        assert type != null;
        if (type.getNamespaceURI().equals(NAMESPACE_URI)) {
            //System.out.println(getLineNumber() + "\t" + schemaElement.toString() + "simple type");//TODO
            schemaElement.setSimpleType(type.getLocalPart());

            if (!TYPES.contains(schemaElement.getType()) && isElementInSchema(schemaElement.getType())) {
                getChildElements(schemaElement);
            } else {
                //elementList.add(schemaElement);
                String rootNameBackup = rootName;
                rootName += schemaElement.getName().substring(schemaElement.getName().lastIndexOf("/") + 1) + "/";
                getChildElements(schemaElement);
                rootName = rootNameBackup;
            }

            elementList.add(schemaElement);
        } else if (type.getNamespaceURI().equals(schema.getTargetNamespace())) {
            elementList.add(schemaElement);
            String rootNameBackup = rootName;
            rootName += schemaElement.getName().substring(schemaElement.getName().lastIndexOf("/") + 1) + "/";
            getChildElements(schemaElement);
            rootName = rootNameBackup;
        } else if (namespaceMap.containsKey(type.getNamespaceURI())) {
            String namespacePath = namespaceMap.get(type.getNamespaceURI());
            String path = fileName.substring(0, fileName.lastIndexOf("/") + 1) + namespacePath;
            GetAllElements getAllElements = new GetAllElements(schemaElement, path, fullName + "/", elementList);
            elementList = getAllElements.getElementList();
        }
    }

    private void printElementList(ArrayList<SchemaElement> elementList) {
        try {
            //System.out.println( getLineNumber() + "*************************");
            //System.out.println( getLineNumber() + "\t" + "List of all child elements from parent: " + elementList.get(0).toString().substring(0, elementList.get(0).toString().indexOf(" ")));
        } catch (IndexOutOfBoundsException ignored) {
        }
        for (@SuppressWarnings("unused") SchemaElement element : elementList) {
            //System.out.println(getLineNumber() + "\t" + element.toString()); // TODO
        }
    }

    private ArrayList<SchemaElement> getElementList() {
        return elementList;
    }

    public ArrayList<Operation> getOperationList() {
        return operationList;
    }


    /*******************
     *  static methods
     *******************/

    // returns current line number
    private static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

}// end class
