package com.example.questionbank.infrastructure.jpa;

import com.example.questionbank.domain.model.*;
import com.example.questionbank.infrastructure.jpa.record.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Converts persistence records to and from the persistence-independent domain model. */
public final class QuestionMapper {
    private QuestionMapper() { }

    /** Converts a domain question to the matching JPA subtype. */
    public static QuestionRecord toRecord(Question question, UserRecord author, TopicRecord topic,
                                          Set<CompetencyRecord> competencies) {
        QuestionRecord record;
        if (question instanceof MultipleChoiceQuestion multipleChoice) {
            MultipleChoiceQuestionRecord multipleChoiceRecord = new MultipleChoiceQuestionRecord(
                    question.getStatement(), question.getDifficulty(), question.getStatus(), author, topic);
            multipleChoice.getOptions().forEach(option -> multipleChoiceRecord.addOption(
                    new AnswerOptionRecord(option.text(), option.correct())));
            record = multipleChoiceRecord;
        } else if (question instanceof TrueFalseQuestion trueFalse) {
            record = new TrueFalseQuestionRecord(question.getStatement(), question.getDifficulty(),
                    question.getStatus(), author, topic, trueFalse.isExpectedAnswer());
        } else {
            throw new IllegalArgumentException("Unsupported question type: " + question.getClass().getName());
        }
        record.getCompetencies().addAll(competencies);
        if (question.getMetadata() != null) {
            QuestionMetadata metadata = question.getMetadata();
            record.setMetadata(new QuestionMetadataRecord(metadata.cognitiveLevel(),
                    metadata.estimatedMinutes(), metadata.editorialNotes()));
        }
        return record;
    }

    /** Converts a JPA question subtype to its domain representation. */
    public static Question toDomain(QuestionRecord record) {
        Set<Long> competencyIds = record.getCompetencies().stream()
                .map(CompetencyRecord::getId).collect(Collectors.toCollection(LinkedHashSet::new));
        QuestionMetadataRecord metadataRecord = record.getMetadata();
        QuestionMetadata metadata = metadataRecord == null ? null : new QuestionMetadata(
                metadataRecord.getCognitiveLevel(), metadataRecord.getEstimatedMinutes(),
                metadataRecord.getEditorialNotes());
        if (record instanceof MultipleChoiceQuestionRecord multipleChoice) {
            List<AnswerOption> options = multipleChoice.getOptions().stream()
                    .map(option -> new AnswerOption(option.getId(), option.getText(), option.isCorrect())).toList();
            return new MultipleChoiceQuestion(record.getId(), record.getStatement(), record.getDifficulty(),
                    record.getStatus(), record.getAuthor().getId(), record.getTopic().getId(),
                    competencyIds, metadata, options);
        }
        if (record instanceof TrueFalseQuestionRecord trueFalse) {
            return new TrueFalseQuestion(record.getId(), record.getStatement(), record.getDifficulty(),
                    record.getStatus(), record.getAuthor().getId(), record.getTopic().getId(),
                    competencyIds, metadata, trueFalse.isExpectedAnswer());
        }
        throw new IllegalStateException("Unsupported persisted question type: " + record.getClass().getName());
    }
}
