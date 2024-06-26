package me.whitebear.jpa.challenge;

import me.whitebear.jpa.configuration.JPAConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JPAConfiguration.class)
public class ObjectCannotChangeProblem {


}
