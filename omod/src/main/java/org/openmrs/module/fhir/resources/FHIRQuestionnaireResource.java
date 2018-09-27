package org.openmrs.module.fhir.resources;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.openmrs.api.context.Context;
import org.openmrs.module.fhir.api.QuestionnaireService;

public class FHIRQuestionnaireResource {

    public Questionnaire getQuestionnaireById(String uuid) {
        Questionnaire questionnaire = getQuestionnaireService().getQuestionnaire(uuid);
        if (questionnaire == null) {
            throw new ResourceNotFoundException("Questionnaire is not found for given Id " + uuid);
        }
        return questionnaire;
    }

    public Questionnaire createQuestionnaire(Questionnaire questionnaire) {
        return getQuestionnaireService().createQuestionnaire(questionnaire);
    }

    private QuestionnaireService getQuestionnaireService() {
        return Context.getService(QuestionnaireService.class);
    }
}
