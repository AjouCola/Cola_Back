package kr.or.cola.backend.post.post.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kr.or.cola.backend.comment.domain.Comment;
import kr.or.cola.backend.common.BaseTimeEntity;
import kr.or.cola.backend.post.post_tag.domain.PostTag;
import kr.or.cola.backend.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostTag> postTags = new ArrayList<>();

    @Builder
    public Post(String title, String content, User user, PostType postType) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.postType = postType;
    }

    public void updateContents(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addPostTags(List<PostTag> postTags) {
        this.postTags.addAll(postTags);
    }

    public void updatePostTags(List<PostTag> updatePostTags) {
        this.postTags.removeIf(postTag ->
            !updatePostTags.contains(postTag)
        );
        this.postTags.addAll(updatePostTags.stream()
            .filter(postTag -> !this.postTags.contains(postTag))
            .collect(Collectors.toList())
        );
    }
}
