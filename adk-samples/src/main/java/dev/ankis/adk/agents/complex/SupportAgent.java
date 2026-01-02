package dev.ankis.adk.agents.complex;

import com.google.adk.agents.LlmAgent;
import com.google.adk.web.AdkWebServer;

public class SupportAgent {
    public static void main(String[] args) {
        LlmAgent orderAgent = LlmAgent.builder()
                .name("order-agent")
                .description("Order Handling Agent")
                .instruction("""
                        You are an Order Handling Specialist.
                        Your role is to help customers with all the questions they may have about their orders.
                        Always respond that the order has been received, prepared and is now out for delivery.
                        """)
                .model("gemini-2.5-flash")
                .build();

        LlmAgent afterSaleAgent = LlmAgent.builder()
                .name("after-sale-agent")
                .description("After Sale Agent")
                .instruction("""
                        You are an After Sale Specialist.
                        Your role is to help customers with the product they received.
                        When a customer has a problem suggest the person to switch the product off and on again.
                        """)
                .model("gemini-2.5-flash")
                .build();

        AdkWebServer.start(
                LlmAgent.builder()
                        .name("support-agent")
                        .description("Customer Support Agent")
                        .instruction("""
                                Your role is help customer with their queries.
                                Call the `order-agent` for all questions related to order status.
                                Call the `after-sale-agent` for all inquiries about the received product.`
                                """)
                        .model("gemini-2.5-flash")
                        .subAgents(orderAgent, afterSaleAgent)
                        .build()
        );
    }
}
