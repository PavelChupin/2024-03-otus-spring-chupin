package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (final InputStreamReader in = new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader()
                .getResourceAsStream(fileNameProvider.getTestFileName())), StandardCharsets.UTF_8)) {

            // Использовать CsvToBean
            // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
            // Использовать QuestionReadException
            // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/

            final CsvToBean<QuestionDto> csv = new CsvToBeanBuilder(in)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build();

            return csv.parse().stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }
}
