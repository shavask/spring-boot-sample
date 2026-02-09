package com.sample.spring.answers;

import java.time.Instant;
import java.time.LocalDate;

public class AnswerSheetResponse {
    private final long id;
    private final String studentName;
    private final String subject;
    private final LocalDate takenAt;
    private final Instant uploadedAt;
    private final String originalFilename;
    private final String contentType;
    private final String feedback;

    public AnswerSheetResponse(long id,
                               String studentName,
                               String subject,
                               LocalDate takenAt,
                               Instant uploadedAt,
                               String originalFilename,
                               String contentType,
                               String feedback) {
        this.id = id;
        this.studentName = studentName;
        this.subject = subject;
        this.takenAt = takenAt;
        this.uploadedAt = uploadedAt;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.feedback = feedback;
    }

    public long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getTakenAt() {
        return takenAt;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFeedback() {
        return feedback;
    }

    public static AnswerSheetResponse from(AnswerSheet sheet) {
        return new AnswerSheetResponse(
            sheet.getId(),
            sheet.getStudentName(),
            sheet.getSubject(),
            sheet.getTakenAt(),
            sheet.getUploadedAt(),
            sheet.getOriginalFilename(),
            sheet.getContentType(),
            sheet.getFeedback()
        );
    }
}
