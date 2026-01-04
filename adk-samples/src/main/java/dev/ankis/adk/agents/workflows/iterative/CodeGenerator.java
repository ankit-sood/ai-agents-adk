package dev.ankis.adk.agents.workflows.iterative;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LlmAgentConfig;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.ExitLoopTool;
import com.google.adk.web.AdkWebServer;

public class CodeGenerator {
    public static void main(String[] args) {
        LoopAgent codeRefinerLoopAgent = getCodeRefinerLoopAgent();

        var finalPresenter = LlmAgent.builder()
                .name("final-presenter")
                .description("Presents the final, accepted code to the user.")
                .instruction("""
                    The code has been successfully generated and reviewed.
                    Present the final version of the code to the user in a clear format.

                    Final Code:
                    {generated-code}
                """)
                .model("gemini-2.5-flash")
                .build();

        AdkWebServer.start(SequentialAgent.builder()
                .name("code-refiner-assistant")
                .description("Manages the full code generation and refinement process.")
                .subAgents(
                        codeRefinerLoopAgent,
                        finalPresenter)
                .build());

    }

    private static LoopAgent getCodeRefinerLoopAgent() {
        return LoopAgent.builder()
                .name("code-refiner-loop")
                .description("Iteratively generates and reviews code until it is correct.")
                .subAgents(getCodeGeneratorAgent(), getCodeReviewerAgent())
                .maxIterations(3)
                .build();
    }

    private static LlmAgent getCodeGeneratorAgent() {
        return LlmAgent.builder()
                .name("code-generator-agent")
                .description("Agent to write and refine the java code")
                .model("gemini-2.5-flash")
                .instruction("""
                        You are an expert in writting java code based on user's request.
                        In the first turn, write the initial version of the code. In subsequent turns, you will receive
                        the feedback on your code. Your task is to refine the code based on this feedback.
                        
                        Previous Feedback (if any):
                        {feedback?}
                        """)
                .outputKey("generated-code")
                .build();
    }

    private static LlmAgent getCodeReviewerAgent() {
        return LlmAgent.builder()
                .name("code-reviewer-agent")
                .description("Agent to review the java code and decides if it's complete or need more work.")
                .model("gemini-2.5-flash")
                .instruction("""
                        Your role is to act as a senior code reviewer.
                        Analyze the provided Java code for correctness, style (**JAVA 17 as baseline**), and potential bugs.
                        
                        Code to review:
                        {generated-code}
                        
                        If the code is perfect and meets the user's request,
                        you MUST call the `exit_loop` tool.
                        
                        Otherwise, provide constructive feedback for the `code-generator-agent` to improve the code.
                        """)
                .outputKey("feedback")
                .tools(ExitLoopTool.INSTANCE)
                .build();
    }
}
