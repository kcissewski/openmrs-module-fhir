package org.openmrs.module.fhir.providers;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openmrs.module.fhir.resources.FHIRQuestionnaireResource;

public class RestfulQuestionnaireResourceProvider implements IResourceProvider {

    private FHIRQuestionnaireResource questionnaireResource;

    public RestfulQuestionnaireResourceProvider() {
        questionnaireResource = new FHIRQuestionnaireResource();
    }


    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Questionnaire.class;
    }

    @Read()
    public Questionnaire getResourceById(@IdParam IdType id) {
        return questionnaireResource.getQuestionnaireById(id.getIdPart());
    }
}
