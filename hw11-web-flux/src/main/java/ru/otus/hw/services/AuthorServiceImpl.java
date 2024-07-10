package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.services.mapper.AuthorMapper;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll()
                .map(AuthorMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }
}