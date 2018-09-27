package org.openmrs.module.fhir.api;

import org.hl7.fhir.dstu3.model.Questionnaire;

public interface QuestionnaireService {

    Questionnaire getQuestionnaire(String uuid);

    Questionnaire createQuestionnaire(Questionnaire questionnaire);

    Questionnaire updateQuestionnaire(Questionnaire questionnaire, String uuid);

    void deleteQuestionnaire(String uuid);
}
