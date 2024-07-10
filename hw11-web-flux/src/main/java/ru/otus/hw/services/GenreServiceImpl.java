package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.mapper.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .map(GenreMapper::toDto)
                .switchIfEmpty(Flux.empty());
    }
}