package dev.ankis.adk.agents.complex.tools;

import com.google.adk.tools.Annotations;

import java.util.Map;

public class StockTickerTools {
    static Map<String, String> stockPrices = Map.of("GOOG", "300");

    @Annotations.Schema(
            name = "lookup_stock_ticker",
            description = "Lookup stock price for a given company or ticker"
    )
    public static Map<String, String> lookupStockTicker(
            @Annotations.Schema(
                    name = "company_name_or_stock_ticker",
                    description = "The company name or stock ticker"
            )
            String ticker
    ) {
        return Map.of(ticker, stockPrices.getOrDefault(ticker, "500"));
    }
}
