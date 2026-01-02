package dev.ankis.adk.agents.complex;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.web.AdkWebServer;

public class SearchAgentAsTool {
    public static void main(String[] args) {
        // Initialize the agent
        SearchAgent searchAgent = new SearchAgent();

        // Wrap the agent as a tool
        AgentTool searchAgentTool = AgentTool.create(searchAgent.getAgent());

        // Define the main agent
        AdkWebServer.start(
                LlmAgent.builder()
                        .name("researcher-agent")
                        .description("Main agent for answering complex and up-to-date questions.")
                        .instruction("""
                                You are a sophisticated research assistant. 
                                When the user asks a question that requires up-to-date or external information,
                                you MUST use the 'news-search-agent' as tool to get the facts before answering. 
                                After the tool return the results, synthesize the final answer for the user.
                                """)
                        .model("gemini-2.5-flash")
                        .tools(searchAgentTool)
                        .build()
        );
    }
}
