package com.sample.spring.answers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnswerSheetService {
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final Map<Long, AnswerSheet> storage = new ConcurrentHashMap<>();
    private final Path baseDir = Paths.get("data", "answer-sheets");

    public AnswerSheet store(MultipartFile file,
                             String studentName,
                             String subject,
                             LocalDate takenAt) throws IOException {
        Files.createDirectories(baseDir);
        long id = idGenerator.incrementAndGet();
        String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElse("answer-sheet");
        String safeFilename = id + "_" + Paths.get(originalFilename).getFileName().toString();
        Path targetPath = baseDir.resolve(safeFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        AnswerSheet sheet = new AnswerSheet(
            id,
            studentName,
            subject,
            takenAt,
            Instant.now(),
            targetPath.toString(),
            originalFilename,
            file.getContentType()
        );
        storage.put(id, sheet);
        return sheet;
    }

    public Optional<AnswerSheet> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<AnswerSheet> findAll(Optional<YearMonth> monthFilter, Optional<String> subjectFilter) {
        return storage.values().stream()
            .filter(sheet -> monthFilter.map(month -> YearMonth.from(sheet.getTakenAt()).equals(month)).orElse(true))
            .filter(sheet -> subjectFilter
                .map(subject -> sheet.getSubject().equalsIgnoreCase(subject))
                .orElse(true))
            .sorted(Comparator.comparing(AnswerSheet::getUploadedAt).reversed())
            .collect(Collectors.toList());
    }

    public AnswerSheet updateFeedback(long id, String feedback) {
        AnswerSheet sheet = storage.get(id);
        if (sheet == null) {
            throw new IllegalArgumentException("Answer sheet not found");
        }
        sheet.setFeedback(feedback);
        return sheet;
    }
}
