package org.openmrs.module.fhir.api.util;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.openmrs.Concept;
import org.openmrs.FieldAnswer;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public final class FHIRQuestionnaireUtil {

    private FHIRQuestionnaireUtil() { }

    public static Questionnaire generateQuestionnaire(Form form) {
        if (form == null) {
            return null;
        }
        Questionnaire questionnaire = new Questionnaire();

        questionnaire.setId(form.getUuid());
        questionnaire.setName(form.getName());
        questionnaire.setVersion(form.getVersion());
        if (form.getPublished()) {
            questionnaire.setApprovalDate(form.getDateChanged());
        } else {
            questionnaire.setApprovalDate(null);
        }
        questionnaire.setDescription(form.getDescription());

        questionnaire.setItem(generateQuestionnaireItemComponent(form.getFormFields()));

        questionnaire.setPublisher(form.getCreator().getUsername());

        questionnaire.setDate(form.getDateChanged());

        questionnaire.setStatus(getQuestionnaireStatus(form));

        return questionnaire;
    }

    public static Form generateForm(Questionnaire questionnaire) {
        Form form = new Form();

        form.setName(questionnaire.getName());
        form.setVersion(questionnaire.getVersion());
        form.setPublished(questionnaire.getApprovalDate() != null);
        setFormStatuses(form, questionnaire.getStatus());
        form.setDescription(questionnaire.getDescription());
        form.setCreator(getUserByUsername(questionnaire.getPublisher()));
        form.setDateCreated(new Date());

        return form;
    }

    public static Form updateForm(Form newForm, Form formToUpdate) {
        formToUpdate.setName(newForm.getName());
        formToUpdate.setVersion(newForm.getVersion());
        formToUpdate.setPublished(newForm.getPublished());
        formToUpdate.setDescription(newForm.getDescription());
        formToUpdate.setBuild(newForm.getBuild());
        formToUpdate.setEncounterType(newForm.getEncounterType());
        formToUpdate.setFormFields(newForm.getFormFields());
        formToUpdate.setRetired(newForm.getRetired());

        return formToUpdate;
    }

//region OpenMRS Methods
    private static void setFormStatuses(Form form, final Enumerations.PublicationStatus publicationStatus) {
        switch (publicationStatus) {
            case RETIRED:
                form.setPublished(true);
                form.setRetired(true);
                break;
            case ACTIVE:
                form.setPublished(true);
                form.setRetired(false);
                break;
            case DRAFT:
            case UNKNOWN:
            case NULL:
            default:
                form.setPublished(false);
                form.setRetired(false);
                break;
        }
    }

    private static User getUserByUsername(String username) {
        return Context.getUserService().getUserByUsername(username);
    }
//endregion

// region FHIR Methods
    private static Enumerations.PublicationStatus getQuestionnaireStatus(Form form) {
        boolean published = form.getPublished();
        boolean retired = form.getRetired();
        if (!published) {
            return Enumerations.PublicationStatus.DRAFT;
        } else if (published && !retired) {
            return Enumerations.PublicationStatus.ACTIVE;
        } else if (retired){
            return Enumerations.PublicationStatus.RETIRED;
        } else {
            return Enumerations.PublicationStatus.UNKNOWN;
        }
    }

    private static List<Questionnaire.QuestionnaireItemComponent> generateQuestionnaireItemComponent(Set<FormField> formFields) {
        List<Questionnaire.QuestionnaireItemComponent> itemComponents = new ArrayList<>();

        for (FormField formField : formFields) {
            Questionnaire.QuestionnaireItemComponent itemComponent = new Questionnaire.QuestionnaireItemComponent();

            itemComponent.setText(formField.getName());
            itemComponent.setType(Questionnaire.QuestionnaireItemType.NULL);
            itemComponent.setInitial(new StringType(formField.getField().getDefaultValue()));

            itemComponent.setExtension(getAnswerExtensions(formField.getField().getAnswers(), formField.getUuid()));//todo formField uuid or form uuid????\

            itemComponent.setLinkId(formField.getFormFieldId().toString());
            itemComponent.setRequired(formField.getRequired());

            itemComponents.add(itemComponent);
        }

        return itemComponents;
    }

    // todo not tested, leaving it for now and waiting for decisions to be made
    private static List<Extension> getAnswerExtensions(Set<FieldAnswer> answers, String parentUuid) {
        if (answers == null) {
            return null;
        }
        List<Extension> extensions = new ArrayList<>();
        for (FieldAnswer fieldAnswer : answers) {
            extensions.add(new Extension(String.format("/ws/rest/v1/field/%s/answer/%s", parentUuid, fieldAnswer.getUuid())));
        }

        Questionnaire.QuestionnaireItemEnableWhenComponent component = new Questionnaire.QuestionnaireItemEnableWhenComponent();
        component.setExtension(extensions);

        return extensions;
    }
//endregion
}
