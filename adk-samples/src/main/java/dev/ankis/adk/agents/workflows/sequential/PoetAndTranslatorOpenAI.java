package dev.ankis.adk.agents.workflows.sequential;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.models.langchain4j.LangChain4j;
import com.google.adk.web.AdkWebServer;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class PoetAndTranslatorOpenAI {
    public static void main(String[] args) {
        ChatModel openAiChatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .build();

        LlmAgent poet = LlmAgent.builder()
                .name("poet-agent")
                .description("Agent to write poems in english")
                .model(new LangChain4j(openAiChatModel))
                .instruction("""
                        You are a talented poet, who writes short and beautiful poems.
                        """)
                .outputKey("poem")
                .build();

        LlmAgent translator = LlmAgent.builder()
                .name("translator-agent")
                .description("English to French Translator")
                .model(new LangChain4j(openAiChatModel))
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
