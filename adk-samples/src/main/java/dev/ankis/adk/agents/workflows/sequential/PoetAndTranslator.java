package dev.ankis.adk.agents.workflows.sequential;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.web.AdkWebServer;

public class PoetAndTranslator {
    public static void main(String[] args) {
        LlmAgent poet = LlmAgent.builder()
                .name("poet-agent")
                .description("Agent to write poems in english")
                .model("gemini-2.5-flash")
                .instruction("""
                        You are a talented poet, who writes short and beautiful poems.
                        """)
                .outputKey("poem")
                .build();

        LlmAgent translator = LlmAgent.builder()
                .name("translator-agent")
                .description("English to French Translator")
                .model("gemini-2.5-flash")
                .instruction("""
                        As an expert English-French translator, your role is to translate the following poem into French,
                        ensuring the poem still rhymes even after translation:
                        
                        {poem}
                        """)
                .outputKey("translatedPoem")
                .build();

        AdkWebServer.start(
                SequentialAgent.builder()
                        .name("poet-and-translator-agent")
                        .subAgents(poet, translator)
                        .build()
        );
    }
}
