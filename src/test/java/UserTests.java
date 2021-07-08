import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.pucmm.eict.AppModule;
import edu.pucmm.eict.DbModule;
import edu.pucmm.eict.bootstrap.DataBootstrap;
import edu.pucmm.eict.common.CommonModule;
import edu.pucmm.eict.users.User;
import edu.pucmm.eict.users.UserAlreadyExistsException;
import edu.pucmm.eict.users.UserForm;
import edu.pucmm.eict.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserTests {

    private final UserService userService;

    public UserTests() {
        Injector injector = Guice.createInjector(new AppModule(), new DbModule(), new CommonModule());
        injector.getInstance(DataBootstrap.class).inserts();
        userService = injector.getInstance(UserService.class);
    }

    @Test
    @DisplayName("Checking if service creates and user. ")
    public void createOneTest() {
        UserForm f1 = new UserForm();
        f1.setName("User1");
        f1.setLastname("User1");
        f1.setUsername("User1");
        f1.setPassword("User1");
        f1.setEmail("user1@email.com");
        f1.setRoles(List.of("APP_USER"));
        User created = userService.create(f1);
        User fetch = userService.findByUsername("User1").orElseThrow(EntityNotFoundException::new);
        assertEquals(fetch.getId(), created.getId());
    }

    @Test
    @DisplayName("Checking the integrity of the data model User. ")
    public void checkIntegrity() {
        UserForm f1 = new UserForm();
        f1.setName("User1");
        f1.setLastname("User1");
        f1.setUsername("User1");
        f1.setPassword("User1");
        f1.setEmail("user1@email.com");
        f1.setRoles(List.of("APP_USER"));

        UserForm f2 = new UserForm();
        f2.setName("User2");
        f2.setLastname("User2");
        f2.setUsername("User1");
        f2.setPassword("User2");
        f2.setEmail("user1@email.com");
        f2.setRoles(List.of("APP_USER"));


        UserForm f3 = new UserForm();
        f3.setName("User3");
        f3.setLastname("User3");
        f3.setUsername("User3");
        f3.setPassword("User3");
        f3.setEmail("user1@email.com");
        f3.setRoles(List.of("APP_USER"));

        UserForm f4 = new UserForm();
        f4.setName("User4");
        f4.setLastname("User4");
        f4.setUsername("User4");
        f4.setPassword("User4");
        f4.setEmail("user4@email.com");
        f4.setRoles(List.of("APP_USER"));

        userService.create(f1);


        Exception ex1 = assertThrows(UserAlreadyExistsException.class, () -> {
            User created = userService.create(f2);
        });

        log.warn(ex1.getMessage());

        Exception ex2 = assertThrows(UserAlreadyExistsException.class, () -> {
            User created = userService.create(f3);
        });

        log.warn(ex2.getMessage());

        userService.create(f4);
    }

    @Test
    @DisplayName("Check that service fetches actives users.")
    public void fetchUsers() {

        UserForm f1 = new UserForm();
        f1.setName("User1");
        f1.setLastname("User1");
        f1.setUsername("User1");
        f1.setPassword("User1");
        f1.setEmail("user1@email.com");
        f1.setRoles(List.of("APP_USER"));

        UserForm f2 = new UserForm();
        f2.setName("User2");
        f2.setLastname("User2");
        f2.setUsername("User2");
        f2.setPassword("User2");
        f2.setEmail("User2@email.com");
        f2.setRoles(List.of("APP_USER"));


        UserForm f3 = new UserForm();
        f3.setName("User3");
        f3.setLastname("User3");
        f3.setUsername("User3");
        f3.setPassword("User3");
        f3.setEmail("User3@email.com");
        f3.setRoles(List.of("APP_USER"));

        UserForm f4 = new UserForm();
        f4.setName("User4");
        f4.setLastname("User4");
        f4.setUsername("User4");
        f4.setPassword("User4");
        f4.setEmail("user4@email.com");
        f4.setRoles(List.of("APP_USER"));

        List<UserForm> formList = List.of(f1, f2, f3, f4);
        var usersCreated = formList.stream().map(userService::create).collect(Collectors.toList());
        userService.delete("User4");
        userService.delete("User3");

        var users = userService.findAll();
        users.forEach(user -> {
            assertEquals(0L, user.getDeletedAt());
        });
    }
}