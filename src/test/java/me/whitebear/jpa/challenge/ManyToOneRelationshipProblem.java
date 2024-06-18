package me.whitebear.jpa.challenge;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Set;
import me.whitebear.jpa.comment.Comment;
import me.whitebear.jpa.comment.CommentRepository;
import me.whitebear.jpa.mention.CommentMention;
import me.whitebear.jpa.user.User;
import me.whitebear.jpa.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManyToOneRelationshipProblem {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

    static String USERNAME = "username";
    static String PASSWORD = "password";
    static String IMAGE_URL = "https://www.naver.com";

    @Test
    @Order(1)
    @Transactional
    @DisplayName("다대일 관계 문제발생 예제 1")
    void test1() {

        /**
         * CommentMention 생성자에 연관관계 매핑 메서드 넣기 전 (실패 )
         */

        User user = new User(USERNAME, PASSWORD, IMAGE_URL);
        entityManager.persist(user);

        Comment comment = new Comment("comment message");
        comment.setUser(user);
        entityManager.persist(comment);

        CommentMention commentMention = new CommentMention(user, comment);

        System.out.println("==============================");
        Set<CommentMention> commentMentions = user.getCommentMentions();
        for (var mention : commentMentions) {
            System.out.println(mention.getUser().getUsername());
        }
        System.out.println("===============================");

        User findUser = entityManager.find(User.class, user.getId());
        Set<CommentMention> findCommentMentions = findUser.getCommentMentions();
        System.out.println("findCommentMentions.size() = " + findCommentMentions.size());

    }

    @Test
    @Order(2)
    @Transactional
    @DisplayName("다대일 관계 문제발생 예제 2 + 해결")
    void test2() {

        /**
         * CommentMnetion 생성자에 연관관계 매핑 메서드 추가 (성공)
         */

        // user insert
        User user = new User(USERNAME + "1", PASSWORD, IMAGE_URL);
        entityManager.persist(user);

        // comment insert
        Comment comment = new Comment("comment message");
        comment.setUser(user);
        entityManager.persist(comment);

        CommentMention commentMention = new CommentMention(user, comment);

        entityManager.flush();
        entityManager.clear();

        System.out.println("==============================");
        Set<CommentMention> commentMentions = user.getCommentMentions();
        for (var mention : commentMentions) {
            System.out.println(mention.getUser().getUsername());
        }
        System.out.println("===============================");

        comment.setMessage("new message");

        User findUser = entityManager.find(User.class, user.getId());
        Set<CommentMention> findCommentMentions = findUser.getCommentMentions();
        System.out.println("findCommentMentions.size() = " + findCommentMentions.size());

    }
}
