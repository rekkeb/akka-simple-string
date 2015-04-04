package akka.spring;

import akka.actor.ActorSystem;
import akka.actor.Stringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static akka.spring.SpringExtension.SpringExtProvider;

/**
 * The application configuration.
 */
@Configuration
@ComponentScan(basePackages = "akka")
public class AppConfiguration {

    // the application context is needed to initialize the Akka Spring Extension
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Actor system singleton for this application.
     */
    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("AkkaSpringReverser");
        // initialize the application context in the Akka Spring Extension
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    @Scope("prototype")
    public Stringer stringer(){
        return new Stringer("This string is going to be split and reversed");
    }
}