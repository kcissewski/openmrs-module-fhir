package org.openmrs.module.fhir.api.strategies.questionnaire;

import org.hl7.fhir.dstu3.model.Questionnaire;

public interface GenericQuestionnaireStrategy {

    Questionnaire getQuestionnaire(String uuid);

    Questionnaire createQuestionnaire(Questionnaire questionnaire);

    Questionnaire updateQuestionnaire(Questionnaire questionnaire, String uuid);
}
