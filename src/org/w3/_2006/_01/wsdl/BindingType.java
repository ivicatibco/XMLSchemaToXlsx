//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.26 at 02:58:55 PM CEST 
//


package org.w3._2006._01.wsdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * <p>Java class for BindingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BindingType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2006/01/wsdl}ExtensibleDocumentedType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="operation" type="{http://www.w3.org/2006/01/wsdl}BindingOperationType"/>
 *         &lt;element name="fault" type="{http://www.w3.org/2006/01/wsdl}BindingFaultType"/>
 *         &lt;element ref="{http://www.w3.org/2006/01/wsdl}feature"/>
 *         &lt;element ref="{http://www.w3.org/2006/01/wsdl}property"/>
 *         &lt;any processContents='lax' namespace='##other'/>
 *       &lt;/choice>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="interface" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BindingType", propOrder = {
    "operationOrFaultOrFeature"
})
public class BindingType
    extends ExtensibleDocumentedType
{

    @XmlElementRefs({
        @XmlElementRef(name = "operation", namespace = "http://www.w3.org/2006/01/wsdl", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "fault", namespace = "http://www.w3.org/2006/01/wsdl", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "feature", namespace = "http://www.w3.org/2006/01/wsdl", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "property", namespace = "http://www.w3.org/2006/01/wsdl", type = JAXBElement.class, required = false)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> operationOrFaultOrFeature;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlAttribute(name = "type", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String type;
    @XmlAttribute(name = "interface")
    protected QName _interface;

    /**
     * Gets the value of the operationOrFaultOrFeature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operationOrFaultOrFeature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperationOrFaultOrFeature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link BindingFaultType }{@code >}
     * {@link JAXBElement }{@code <}{@link FeatureType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyType }{@code >}
     * {@link Object }
     * {@link Element }
     * {@link JAXBElement }{@code <}{@link BindingOperationType }{@code >}
     * 
     * 
     */
    public List<Object> getOperationOrFaultOrFeature() {
        if (operationOrFaultOrFeature == null) {
            operationOrFaultOrFeature = new ArrayList<Object>();
        }
        return this.operationOrFaultOrFeature;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setInterface(QName value) {
        this._interface = value;
    }

}
