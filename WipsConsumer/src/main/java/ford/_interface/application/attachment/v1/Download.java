//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package ford._interface.application.attachment.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ford.application.attachment.v1.ApplicationAttachmentInfoType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:ford/Application/Attachment/v1.0}ApplicationAttachmentInfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "applicationAttachmentInfo"
})
@XmlRootElement(name = "Download")
public class Download {

    @XmlElement(name = "ApplicationAttachmentInfo", namespace = "urn:ford/Application/Attachment/v1.0", required = true)
    protected ApplicationAttachmentInfoType applicationAttachmentInfo;

    /**
     * Gets the value of the applicationAttachmentInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationAttachmentInfoType }
     *     
     */
    public ApplicationAttachmentInfoType getApplicationAttachmentInfo() {
        return applicationAttachmentInfo;
    }

    /**
     * Sets the value of the applicationAttachmentInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationAttachmentInfoType }
     *     
     */
    public void setApplicationAttachmentInfo(ApplicationAttachmentInfoType value) {
        this.applicationAttachmentInfo = value;
    }

}
