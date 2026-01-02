package dev.ankis.adk.agents.complex;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import com.google.adk.web.AdkWebServer;
import dev.ankis.adk.agents.complex.tools.StockTickerTools;

import java.util.Map;

public class StockTicker {
    static Map<String, String> stockPrices = Map.of("GOOG", "300");

    public static void main(String[] args) {
        AdkWebServer.start(
                LlmAgent.builder()
                        .name("StockTickerAgent")
                        .description("Agent to get stock information based on stock ticker or company name.")
                        .instruction("""
                                You are stock exchange ticker expert.
                                When asked about the stock price of a company, use the `lookup_stock_ticker` tool to 
                                find the information.
                                """)
                        .model("gemini-2.5-flash")
                        .tools(FunctionTool.create(StockTickerTools.class, "lookupStockTicker"))
                        .build()
        );
    }
}
