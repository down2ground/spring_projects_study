package springsmdiagramgenerator;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Utils {

    private static String PLANTUML_JAR = System.getenv("PLANTUML_JAR");

    public static String generatePlantUMLDiagramSource(StateMachine<String, String> stateMachine,
                                                boolean addEndStates) {
        Set<State<String, String>> statesWithOutgoingTransitions = new HashSet<>();
        StringBuilder buffer = new StringBuilder("@startuml\n");
        Optional.ofNullable(stateMachine.getInitialState())
                .map(State::getId)
                .ifPresent((id) -> buffer.append("[*] -> ").append(id).append("\n"));
        stateMachine.getTransitions().forEach(transition -> {
            buffer.append(transition.getSource().getId()).append(" --> ")
                    .append(transition.getTarget().getId())
                    .append(" : ").append(transition.getTrigger().getEvent())
                    .append("\n");
            if (addEndStates) {
                statesWithOutgoingTransitions.add(transition.getSource());
            }
        });
        if (addEndStates) {
            stateMachine.getStates().stream()
                    .filter(state -> !statesWithOutgoingTransitions.contains(state))
                    .forEach(endState -> {
                        String endStateId = endState.getId();
                        buffer.append(endStateId).append(" -> [*]\n");
                    });
        }
        buffer.append("@enduml");
        return buffer.toString();
    }

    public static void generatePlantUMLDiagramCL(String sourceFile, String outputDir)
            throws Exception {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(PLANTUML_JAR);
        if (outputDir != null) {
            command.add("-o");
            command.add(outputDir);
        }
        command.add(sourceFile);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
            System.out.println(exitCode == 0 ? "Diagram generated successfully." :
                    "Error in generating diagram. Exit code: " + exitCode);
    }

    public static void generatePlantUMLDiagram(String diagramSource, String outputPath)
            throws IOException {
        SourceStringReader reader = new SourceStringReader(diagramSource);
        FileOutputStream output = new FileOutputStream(outputPath);
        reader.generateImage(output, new FileFormatOption(FileFormat.PNG, false));
    }
}
