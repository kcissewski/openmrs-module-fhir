package org.openmrs.module.fhir.api.strategies.questionnaire;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.openmrs.Form;
import org.openmrs.api.APIException;
import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.module.fhir.api.util.FHIRQuestionnaireUtil;
import org.openmrs.module.fhir.api.util.FHIRUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("DefaultQuestionnaireStrategy")
public class QuestionnaireStrategy implements GenericQuestionnaireStrategy {

    @Override
    public Questionnaire getQuestionnaire(String uuid) {
        Form form = getFormService().getFormByUuid(uuid);

        if (form == null) {
            return null;
        }
        return FHIRQuestionnaireUtil.generateQuestionnaire(form);
    }

    @Override
    public Questionnaire createQuestionnaire(Questionnaire questionnaire) {
        Form form = FHIRQuestionnaireUtil.generateForm(questionnaire);

        try {
            form = getFormService().saveForm(form);
        } catch (APIException e) {
            throw new UnprocessableEntityException(
                    "The request cannot be processed due to the following issues \n" + e.getMessage());
        }

        return FHIRQuestionnaireUtil.generateQuestionnaire(form);
    }

    private FormService getFormService() {
        return Context.getFormService();
    }
}
