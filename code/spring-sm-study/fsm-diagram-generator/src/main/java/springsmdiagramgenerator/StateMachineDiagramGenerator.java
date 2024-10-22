package springsmdiagramgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import springsmstudy01.SimplestStateMachineConfiguration;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class StateMachineDiagramGenerator {

    @Autowired
    private StateMachine<String, String> stateMachine;

    public static void main(String[] args) throws Exception {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                StateMachineDiagramGenerator.class,
                SimplestStateMachineConfiguration.class)) {
            context.getBean(StateMachineDiagramGenerator.class).generateDiagram();
        }
    }

    public void generateDiagram() throws Exception {
        String diagramSource = Utils.generatePlantUMLDiagramSource(stateMachine, true);
        Files.writeString(Paths.get("../output/generated_sm_diagram.txt"), diagramSource);
        Utils.generatePlantUMLDiagramCL("../output/generated_sm_diagram.txt", null);
        Utils.generatePlantUMLDiagram(diagramSource, "../output/generated_sm_diagram_Lib.png");
    }
}
