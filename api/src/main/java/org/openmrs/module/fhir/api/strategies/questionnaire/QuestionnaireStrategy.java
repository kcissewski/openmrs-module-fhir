package org.openmrs.module.fhir.api.strategies.questionnaire;

import org.hl7.fhir.dstu3.model.Questionnaire;
import org.openmrs.Form;
import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.module.fhir.api.util.FHIRQuestionnaireUtil;
import org.springframework.stereotype.Component;

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

    private FormService getFormService() {
        return Context.getFormService();
    }
}
