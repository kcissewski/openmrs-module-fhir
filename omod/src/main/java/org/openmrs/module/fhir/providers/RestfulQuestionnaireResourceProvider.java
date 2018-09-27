package org.openmrs.module.fhir.providers;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openmrs.module.fhir.resources.FHIRQuestionnaireResource;

public class RestfulQuestionnaireResourceProvider implements IResourceProvider {

    private static final String SUCCESSFUL_CREATE_MESSAGE = "Questionnaire successfully created with id %s";
    private static final String SUCCESSFUL_UPDATE_MESSAGE = "Questionnaire successfully updated with id %s";

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

    @Create
    public MethodOutcome createQuestionnaire(@ResourceParam Questionnaire questionnaire) {
        Questionnaire createdQuestionnaire = questionnaireResource.createQuestionnaire(questionnaire);
        return createMethodOutcome(createdQuestionnaire.getId(), SUCCESSFUL_CREATE_MESSAGE);
    }

    @Update
    public MethodOutcome updateQuestionnaire(@ResourceParam Questionnaire questionnaire, @IdParam IdType id) {
        Questionnaire updatedQuestionnaire = questionnaireResource.updateQuestionnaire(questionnaire, id.getIdPart());
        return createMethodOutcome(updatedQuestionnaire.getId(), SUCCESSFUL_UPDATE_MESSAGE);
    }

    private MethodOutcome createMethodOutcome(String resourceId, String messagePattern) {
        MethodOutcome retVal = new MethodOutcome();
        retVal.setId(new IdType(Questionnaire.class.getSimpleName(), resourceId));

        OperationOutcome outcome = new OperationOutcome();
        CodeableConcept concept = new CodeableConcept();
        Coding coding = concept.addCoding();
        coding.setDisplay(String.format(messagePattern, resourceId));
        outcome.addIssue().setDetails(concept);
        retVal.setOperationOutcome(outcome);
        return retVal;
    }
}
