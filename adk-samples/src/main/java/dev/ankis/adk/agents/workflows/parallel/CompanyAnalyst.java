package dev.ankis.adk.agents.workflows.parallel;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.GoogleSearchTool;
import com.google.adk.web.AdkWebServer;

import java.time.LocalDate;

public class CompanyAnalyst {
    public static void main(String[] args) {
        ParallelAgent marketResearchAgent = getMarketResearchAgent();
        LlmAgent reportComplierAgent = getReportCompilerAgent();
        LlmAgent stockRecommendationAgent = getStockRecommendationAgent();
        AdkWebServer.start(
                SequentialAgent.builder()
                        .name("company-analyst")
                        .description("Collects various information about the company and issue buy or sell recommendation.")
                        .subAgents(marketResearchAgent, reportComplierAgent, stockRecommendationAgent)
                        .build()
        );
    }

    private static LlmAgent getCompanyProfilingAgent() {
        return LlmAgent.builder()
                .name("company-profiling-agent")
                .description("Agent to analyse the profile of the company.")
                .model("gemini-2.5-flash")
                .instruction("""
                        You are an expert in creating the profile of the companies.
                        Your job is to provide the brief overview of the company. Include its mission,
                        vision and impact of current macro economic conditions.
                        Use the `google_search` tool to find the information.
                        
                        **DON'T INCLUDE ANY ADDITIONAL COMMENTARY**
                        
                        Today is 
                        """ + LocalDate.now())
                .tools(new GoogleSearchTool())
                .outputKey("profile")
                .build();
    }

    private static LlmAgent getNewsFinderAgent() {
        return LlmAgent.builder()
                .name("news-finder-agent")
                .description("Finds the latest news about the company")
                .instruction("""
                        Your role is to find the top 3-4 recent news headlines for the given company.
                        Use the `google_search` tool to find the information.
                        Present the results as a simple bulleted list.
                        
                        **DON'T INCLUDE ANY ADDITIONAL COMMENTARY**
                        
                        Today is 
                        """+ LocalDate.now())
                .model("gemini-2.5-flash")
                .tools(new GoogleSearchTool())
                .outputKey("news")
                .build();
    }

    private static LlmAgent getFinancialAnalystAgent() {
        return LlmAgent.builder()
                .name("financial-analyst-agent")
                .description("Analyzes the financial information for the company.")
                .model("gemini-2.5-flash")
                .instruction("""
                        Your role is to provide a snapshot of the given company's recent financial performance.
                        Focus on stock trends or recent earnings reports.
                        Use the Google Search Tool.
                        
                        **DON'T INCLUDE ANY ADDITIONAL COMMENTARY**
                        
                        Today is 
                        """+ LocalDate.now())
                .tools(new GoogleSearchTool())
                .outputKey("financials")
                .build();
    }

    private static ParallelAgent getMarketResearchAgent() {
        return ParallelAgent.builder()
                .name("market-research-agent")
                .description("Performs the comprehensive market research on the company.")
                .subAgents(getCompanyProfilingAgent(), getNewsFinderAgent(), getFinancialAnalystAgent())
                .build();
    }

    private static LlmAgent getReportCompilerAgent() {
        return LlmAgent.builder()
                .name("report-complier")
                .description("Compiles a final market research report.")
                .instruction("""
                        Your role is to synthesize the provided information into a coherent market research report.
                        Combine the company profile, latest news, and financial analysis into a single, well-formatted report.
                        
                        ## Company Profile
                        {profile}
                        
                        ## Latest News
                        {news}
                        
                        ## Financial Snapshot
                        {financials}
                        
                        **DON'T INCLUDE ANY ADDITIONAL COMMENTARY**
                        """)
                .model("gemini-2.5-flash")
                .outputKey("financial-report")
                .build();
    }

    private static LlmAgent getStockRecommendationAgent() {
        return LlmAgent.builder()
                .name("stock-recommendation-agent")
                .description("Issues the buy or sell recommendation")
                .instruction("""
                        You are an expert issuing buy and sell recommendation by analyzing the company profile, financials
                        and latest compnay news.
                        
                        Your role is to analyse the provided information and issue the buy or sell recommendation/ 
                        
                        # Company Information
                        {financial-report}
                        
                        Always provide the answer in the format:
                        - Recommendation: {Buy, Sell, Hold}
                        - Strengths: {Top 3-5 Strengths}
                        - Weakness : {Top 3-5 Weaknesses}
                        
                        **DON'T INCLUDE ANY ADDITIONAL COMMENTARY**
                        """)
                .model("gemini-2.5-flash")
                .build();
    }


}
