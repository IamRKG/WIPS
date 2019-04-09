package ford._interface.application.attachment.v1;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.Holder;
import ford.application.attachment.v1.ApplicationAttachmentInfoType;
import ford.application.attachment.v1.AttachmentSearchCriteriaType;
import ford.application.attachment.v1.AttachmentSearchResultsType;

public class ApplicationAttachmentProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private ford._interface.application.attachment.v1.ApplicationAttachmentService _service = null;
        private ford._interface.application.attachment.v1.IApplicationAttachment _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new ford._interface.application.attachment.v1.ApplicationAttachmentService(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            _service = new ford._interface.application.attachment.v1.ApplicationAttachmentService();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getApplicationAttachment();
        }

        public ford._interface.application.attachment.v1.IApplicationAttachment getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("", "ApplicationAttachment");
                _dispatch = _service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

                String proxyEndpointUrl = getEndpoint();
                BindingProvider bp = (BindingProvider) _dispatch;
                String dispatchEndpointUrl = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                if (!dispatchEndpointUrl.equals(proxyEndpointUrl))
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, proxyEndpointUrl);
            }
            return _dispatch;
        }

        public String getEndpoint() {
            BindingProvider bp = (BindingProvider) _proxy;
            return (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }

        public void setEndpoint(String endpointUrl) {
            BindingProvider bp = (BindingProvider) _proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

            if (_dispatch != null ) {
                bp = (BindingProvider) _dispatch;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            }
        }

        public void setMTOMEnabled(boolean enable) {
            SOAPBinding binding = (SOAPBinding) ((BindingProvider) _proxy).getBinding();
            binding.setMTOMEnabled(enable);
        }
    }

    public ApplicationAttachmentProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public ApplicationAttachmentProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public void upload(Holder<ApplicationAttachmentInfoType> applicationAttachmentInfo) {
        _getDescriptor().getProxy().upload(applicationAttachmentInfo);
    }

    public void update(Holder<ApplicationAttachmentInfoType> applicationAttachmentInfo) {
        _getDescriptor().getProxy().update(applicationAttachmentInfo);
    }

    public void download(Holder<ApplicationAttachmentInfoType> applicationAttachmentInfo) {
        _getDescriptor().getProxy().download(applicationAttachmentInfo);
    }

    public void delete(Holder<ApplicationAttachmentInfoType> applicationAttachmentInfo) {
        _getDescriptor().getProxy().delete(applicationAttachmentInfo);
    }

    public AttachmentSearchResultsType search(AttachmentSearchCriteriaType attachmentSearchCriteria) {
        return _getDescriptor().getProxy().search(attachmentSearchCriteria);
    }

}