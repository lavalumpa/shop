package admincreation;

import com.shopstuff.shop.security.PasswordEncoderConfiguration;
import com.shopstuff.shop.user.Role;
import com.shopstuff.shop.user.User;
import com.shopstuff.shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackageClasses = {PasswordEncoderConfiguration.class})
@EnableJpaRepositories("com.shopstuff.shop.user")
@EntityScan("com.shopstuff.shop.user")
@EnableJpaAuditing
public class AdminCreation implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("3 arguments are required, name, email and password ");
            return;
        }
        var application = new SpringApplication(AdminCreation.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        var encodedPassword = passwordEncoder.encode(args[2]);
        if (userRepository.existsByEmail(args[1])|| userRepository.existsByName(args[0])){
            System.out.println("User with email and/or name exists already");
            return;
        }
        var user = User.builder()
                .name(args[0])
                .password(encodedPassword)
                .email(args[1])
                .roles(Set.of(Role.ADMIN))
                .build();
        var createdUser=userRepository.save(user);
        System.out.println("Admin created " + createdUser.getName());
    }
}
