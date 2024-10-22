package springsmstudy01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class Main {

    @Autowired
    private StateMachine<String, String> stateMachine;

    public static void main(String[] args) throws Exception {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                Main.class, SimplestStateMachineConfiguration.class)) {
            context.getBean(Main.class).runDemo();
        }
    }

    public void runDemo() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("| Event | State | Comment\n");
        stringBuilder.append("| ----- | :-----: | -----\n");
        stringBuilder.append("| | ").append(stateMachine.getState())
                .append(" | SM is not started yet\n");

        stateMachine.startReactively().subscribe();
        sendEventAndPrintState(stringBuilder, null, "initial state");

        sendEventAndPrintState(stringBuilder, "hungry", "");
        sendEventAndPrintState(stringBuilder, "full", "");
        sendEventAndPrintState(stringBuilder, "tired", "");
        sendEventAndPrintState(stringBuilder, "vacation_ended", "no such transition");
        sendEventAndPrintState(stringBuilder, "hungry", "");
        sendEventAndPrintState(stringBuilder, "full", "");
        sendEventAndPrintState(stringBuilder, "got_lazy", "no such event");
        sendEventAndPrintState(stringBuilder, "vacation_ended", "");

        System.out.println(stringBuilder);
        Files.write(Paths.get("../output/simple_sm_output.txt"),
                stringBuilder.toString().getBytes());
    } // runDemo()

    private void sendEventAndPrintState(StringBuilder stringBuilder, String event,
                                        String comment) {
        if (event != null) {
            Message<String> message = MessageBuilder.withPayload(event).build();
            stateMachine.sendEvent(Mono.just(message)).subscribe();
        }
        stringBuilder.append("| ")
                .append(event == null ? " " : event).append(" | ")
                .append(stateMachine.getState().getId()).append(" | ")
                .append(comment).append("\n");
    }
}
