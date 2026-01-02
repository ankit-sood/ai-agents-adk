package dev.ankis.adk.agents.complex;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;
import com.google.genai.types.GoogleSearch;

import java.time.LocalDate;

public class SearchAgent {
    private LlmAgent agent;

    public SearchAgent() {
        agent = LlmAgent.builder()
                .name("news-search-agent")
                .description("Searches for recent events and provide a concise summary")
                .instruction("""
                        You are a concise information retrieval specialist.
                        Use the `google_search` tool to find the information.
                        Always provide the answer as a short, direct summary, without commentary.
                        Today is \
                        """ + LocalDate.now())
                .model("gemini-2.5-flash")
                .tools(new GoogleSearchTool())
                .build();
    }

    public LlmAgent getAgent() {
        return agent;
    }
}
