package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBookId(Long bookId);

    // Просто мой второй варинт с целью экспериментов
//    @Query("select cm from Comment cm where cm.book.id = :bookId")
//    List<Comment> findAllByBookId(@Param("bookId") Long bookId);
}