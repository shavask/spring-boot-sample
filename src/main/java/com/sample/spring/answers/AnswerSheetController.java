package com.sample.spring.answers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/answer-sheets")
public class AnswerSheetController {
    private final AnswerSheetService answerSheetService;

    public AnswerSheetController(AnswerSheetService answerSheetService) {
        this.answerSheetService = answerSheetService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AnswerSheetResponse uploadAnswerSheet(
        @RequestParam("file") MultipartFile file,
        @RequestParam("studentName") String studentName,
        @RequestParam("subject") String subject,
        @RequestParam("takenAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate takenAt
    ) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Answer sheet file is required");
        }
        try {
            AnswerSheet sheet = answerSheetService.store(file, studentName, subject, takenAt);
            return AnswerSheetResponse.from(sheet);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to store answer sheet", ex);
        }
    }

    @GetMapping
    public List<AnswerSheetResponse> listAnswerSheets(
        @RequestParam(name = "month", required = false) String month,
        @RequestParam(name = "subject", required = false) String subject
    ) {
        Optional<YearMonth> monthFilter = parseMonth(month);
        Optional<String> subjectFilter = Optional.ofNullable(subject).filter(value -> !value.trim().isEmpty());
        return answerSheetService.findAll(monthFilter, subjectFilter).stream()
            .map(AnswerSheetResponse::from)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AnswerSheetResponse getAnswerSheet(@PathVariable("id") long id) {
        AnswerSheet sheet = answerSheetService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer sheet not found"));
        return AnswerSheetResponse.from(sheet);
    }

    @PostMapping("/{id}/feedback")
    public AnswerSheetResponse updateFeedback(@PathVariable("id") long id,
                                              @RequestBody FeedbackRequest request) {
        if (request == null || request.getFeedback() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feedback text is required");
        }
        try {
            AnswerSheet sheet = answerSheetService.updateFeedback(id, request.getFeedback());
            return AnswerSheetResponse.from(sheet);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    private Optional<YearMonth> parseMonth(String month) {
        if (month == null || month.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(YearMonth.parse(month));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "month must be in YYYY-MM format", ex);
        }
    }
}
