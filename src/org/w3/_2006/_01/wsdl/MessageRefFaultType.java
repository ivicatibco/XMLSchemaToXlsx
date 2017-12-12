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
 * <p>Java class for MessageRefFaultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageRefFaultType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2006/01/wsdl}ExtensibleDocumentedType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{http://www.w3.org/2006/01/wsdl}feature"/>
 *         &lt;element ref="{http://www.w3.org/2006/01/wsdl}property"/>
 *         &lt;any processContents='lax' namespace='##other'/>
 *       &lt;/choice>
 *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="messageLabel" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageRefFaultType", propOrder = {
    "featureOrPropertyOrAny"
})
public class MessageRefFaultType
    extends ExtensibleDocumentedType
{

    @XmlElementRefs({
        @XmlElementRef(name = "feature", namespace = "http://www.w3.org/2006/01/wsdl", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "property", namespace = "http://www.w3.org/2006/01/wsdl", type = JAXBElement.class, required = false)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> featureOrPropertyOrAny;
    @XmlAttribute(name = "ref", required = true)
    protected QName ref;
    @XmlAttribute(name = "messageLabel")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String messageLabel;

    /**
     * Gets the value of the featureOrPropertyOrAny property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the featureOrPropertyOrAny property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFeatureOrPropertyOrAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link FeatureType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyType }{@code >}
     * {@link Object }
     * {@link Element }
     * 
     * 
     */
    public List<Object> getFeatureOrPropertyOrAny() {
        if (featureOrPropertyOrAny == null) {
            featureOrPropertyOrAny = new ArrayList<Object>();
        }
        return this.featureOrPropertyOrAny;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setRef(QName value) {
        this.ref = value;
    }

    /**
     * Gets the value of the messageLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageLabel() {
        return messageLabel;
    }

    /**
     * Sets the value of the messageLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageLabel(String value) {
        this.messageLabel = value;
    }

}
