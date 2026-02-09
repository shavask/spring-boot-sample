package com.sample.spring.answers;

import java.time.Instant;
import java.time.LocalDate;

public class AnswerSheet {
    private final long id;
    private final String studentName;
    private final String subject;
    private final LocalDate takenAt;
    private final Instant uploadedAt;
    private final String filePath;
    private final String originalFilename;
    private final String contentType;
    private String feedback;

    public AnswerSheet(long id,
                       String studentName,
                       String subject,
                       LocalDate takenAt,
                       Instant uploadedAt,
                       String filePath,
                       String originalFilename,
                       String contentType) {
        this.id = id;
        this.studentName = studentName;
        this.subject = subject;
        this.takenAt = takenAt;
        this.uploadedAt = uploadedAt;
        this.filePath = filePath;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
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

    public String getFilePath() {
        return filePath;
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

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
