package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final MutableAclService mutableAclService;

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found"
                        .formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#book, 'WRITE')")
    public Book update(@Param("book") Book book, String title, Long authorId, Long genreId) {
        final var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found"
                        .formatted(authorId)));
        final var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found"
                        .formatted(genreId)));
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book create(BookCreateDto bookCreateDto) {
        final var author = authorRepository.findById(bookCreateDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found"
                        .formatted(bookCreateDto.getAuthorId())));
        final var genre = genreRepository.findById(bookCreateDto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found"
                        .formatted(bookCreateDto.getGenreId())));

        final var book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setAuthor(author);
        book.setGenre(genre);

        final var bookResult = bookRepository.save(book);

        setGrant(bookResult);

        return bookResult;
    }

    private void setGrant(Book bookResult) {
        // Раздаем право на новую книгу
        // После создания объекта нужно создать ACL для него
        ObjectIdentity oid = new ObjectIdentityImpl(bookResult);

        // Можно только под пользователем запустивгим метод, нельзя делать из под роли.
        var acl = mutableAclService.createAcl(oid);
        //acl.setOwner(owner);

        // Разрешаем чтение
        acl.insertAce(acl.getEntries().size(), BasePermission.READ,
                new GrantedAuthoritySid("ROLE_LIBRARIAN"), true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ,
              new PrincipalSid("user"), true);

        // Обновим ACL
        this.mutableAclService.updateAcl(acl);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#book, 'DELETE')")
    public void delete(@Param("book") Book book) {
        bookRepository.delete(book);
        //bookRepository.deleteById(id);
    }
}