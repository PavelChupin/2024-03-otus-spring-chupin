package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        final TypedQuery<Comment> commentTypedQuery = em.createQuery("select cm " +
                "from Comment cm " +
                "where cm.book.id = :bookId", Comment.class);
        commentTypedQuery.setParameter("bookId", bookId);
        return commentTypedQuery.getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void deleteById(long id) {
        // Мой старый вариант
//        final Query query = em.createQuery("delete from Comment where id =: id");
//        query.setParameter("id", id);
//        query.executeUpdate();

        // Предложенный вариант на ревью
        final Comment comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        }
    }
}
