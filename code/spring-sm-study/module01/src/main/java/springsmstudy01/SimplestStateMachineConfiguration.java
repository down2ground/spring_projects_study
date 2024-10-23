package springsmstudy01;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Set;

@Configuration
@EnableStateMachine
public class SimplestStateMachineConfiguration
        extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states)
            throws Exception {
        states.withStates()
                .initial("Vacation")
                .end("Working")
                .states(Set.of("Eating", "Relaxing", "Sleeping"));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {
        transitions.withExternal().source("Vacation").target("Eating").event("hungry")
                .and().withExternal().source("Eating").target("Relaxing").event("full")
                .and().withExternal().source("Relaxing").target("Eating").event("hungry")
                .and().withExternal().source("Relaxing").target("Sleeping").event("tired")
                .and().withExternal().source("Sleeping").target("Eating").event("hungry")
                .and().withExternal().source("Relaxing").target("Working").event("vacation_ended");
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config)
            throws Exception {
        config.withConfiguration()
                .listener(new CustomStateMachineListener<>());
    }
}
