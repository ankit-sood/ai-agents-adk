package dev.ankis.adk.agents.simple;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class ScienceTeacherCliAgent {
    public static void main(String[] args) {
        // Create the agent
        LlmAgent scienceTeacherAgent = LlmAgent.builder()
                .name("ScienceTeacherAgent")
                .description("Science teacher agent")
                .instruction("""
                                You are a science teacher for teenagers.
                                You explain science concepts in a simple, concise and direct way.
                                """)
                .model("gemini-2.5-flash")
                .build();

        RunConfig runConfig = RunConfig.builder().build();

        InMemoryRunner inMemoryRunner = new InMemoryRunner(scienceTeacherAgent);

        Session session = inMemoryRunner.sessionService()
                .createSession(inMemoryRunner.appName(), "user1")
                .blockingGet();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                log.info("You > ");
                String input = scanner.nextLine();
                if (input.equals("quit")) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(input));
                Flowable<Event> events = inMemoryRunner.runAsync(session.userId(), session.id(), userMsg, runConfig);

                log.info("\nAgent > ");
                events.blockingForEach(event -> {
                    if(event.finalResponse()){
                        log.info(event.stringifyContent());
                    }
                });
            }
        }

    }
}
