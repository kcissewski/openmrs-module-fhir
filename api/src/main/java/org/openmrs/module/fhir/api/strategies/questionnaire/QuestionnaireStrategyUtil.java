package org.openmrs.module.fhir.api.strategies.questionnaire;

import org.openmrs.api.context.Context;
import org.openmrs.module.fhir.api.util.FHIRUtils;

public final class QuestionnaireStrategyUtil {

    private QuestionnaireStrategyUtil() { }

    public static GenericQuestionnaireStrategy getQuestionnaireStrategy() {
        String strategy = FHIRUtils.getQuestionnaireStrategy();

        return strategy == null ? new QuestionnaireStrategy()
                : Context.getRegisteredComponent(strategy, GenericQuestionnaireStrategy.class);
    }
}
