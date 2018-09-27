package org.openmrs.module.fhir.api.impl;

import org.hl7.fhir.dstu3.model.Questionnaire;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.fhir.api.QuestionnaireService;
import org.openmrs.module.fhir.api.strategies.questionnaire.QuestionnaireStrategyUtil;

public class QuestionnaireServiceImpl extends BaseOpenmrsService implements QuestionnaireService {

    @Override
    public Questionnaire getQuestionnaire(String uuid) {
        return QuestionnaireStrategyUtil.getQuestionnaireStrategy().getQuestionnaire(uuid);
    }

    @Override
    public Questionnaire createQuestionnaire(Questionnaire questionnaire) {
        return QuestionnaireStrategyUtil.getQuestionnaireStrategy().createQuestionnaire(questionnaire);
    }

    @Override
    public Questionnaire updateQuestionnaire(Questionnaire questionnaire, String uuid) {
        return QuestionnaireStrategyUtil.getQuestionnaireStrategy().updateQuestionnaire(questionnaire, uuid);
    }
}
