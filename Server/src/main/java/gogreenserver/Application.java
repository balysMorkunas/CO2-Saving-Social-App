package gogreenserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.Arrays;

@SpringBootApplication
@EntityScan( basePackages = {"model"} )
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Fixing Interfaces that is getting called just before run().
     * @param ctx an ApplicatonContext, the context
     *            of a application that should be generated before run()
     * @return the beans provided by Spring Boot
     */
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            new File(System.getProperty("user.home") + "\\GoGreenServerStorage\\profile").mkdirs();
            new File(System.getProperty("user.home") + "\\GoGreenServerStorage\\actions").mkdirs();
            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }
}
