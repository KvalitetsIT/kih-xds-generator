package io.oth.xdsgenerator.model.phmr;

import dk.s4.hl7.cda.model.phmr.PHMRDocument;
import io.oth.xdsgenerator.model.DocumentMetadata;

public class PhmrAndMetadata {

    PHMRDocument phmr;

    DocumentMetadata documentMetadata;

    public PhmrAndMetadata(PHMRDocument phmr, DocumentMetadata metadata) {
        this.phmr = phmr;
        this.documentMetadata = metadata;
    }

    public PHMRDocument getPhmr() {
        return phmr;
    }

    public void setPhmr(PHMRDocument phmr) {
        this.phmr = phmr;
    }

    public DocumentMetadata getDocumentMetadata() {
        return documentMetadata;
    }

    public void setDocumentMetadata(DocumentMetadata documentMetadata) {
        this.documentMetadata = documentMetadata;
    }
}
