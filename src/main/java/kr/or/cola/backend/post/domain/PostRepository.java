package kr.or.cola.backend.post.domain;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p ORDER BY p.id DESC")
    List<Post> findAllDesc();

    Page<Post> findByPostType(PostType postType, Pageable pageable);
}
