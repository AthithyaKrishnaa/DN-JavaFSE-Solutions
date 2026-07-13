/*
 * Exercise 4 - Resilient Microservices with Circuit Breaker
 */
import java.util.ArrayDeque;
import java.util.Deque;

public class PaymentCircuitBreakerDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void warn(String msg) { print("WARN", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    enum State { CLOSED, OPEN, HALF_OPEN }

    // resilience4j.circuitbreaker.instances.thirdPartyPaymentApi.*
    static class CircuitBreakerConfig {
        final int slidingWindowSize = 5;
        final int failureRateThreshold = 50;
        final int slowCallDurationThresholdMs = 2000;
        final int slowCallRateThreshold = 50;
    }

    // @CircuitBreaker(name = "thirdPartyPaymentApi", fallbackMethod = "paymentFallback")
    static class ThirdPartyPaymentClient {
        private final Logger logger;
        ThirdPartyPaymentClient(Logger logger) { this.logger = logger; }

        // Simulates calling a slow/unreliable third-party payment gateway
        CallResult charge(String transactionId, int simulatedLatencyMs, boolean succeeds) {
            logger.info("calling third-party API POST https://payments.example.com/charge txnId=" + transactionId
                    + " (latency=" + simulatedLatencyMs + "ms)");
            return new CallResult(succeeds, simulatedLatencyMs);
        }
    }

    static class CallResult {
        final boolean succeeded;
        final int latencyMs;
        CallResult(boolean succeeded, int latencyMs) { this.succeeded = succeeded; this.latencyMs = latencyMs; }
    }

    // FallbackEventMonitor - records CircuitBreakerEvent.Type.NOT_PERMITTED / ERROR for monitoring
    static class FallbackEventMonitor {
        private int fallbackInvocations = 0;
        private final Logger logger;
        FallbackEventMonitor(Logger logger) { this.logger = logger; }

        void recordFallback(String circuitId, String reason) {
            fallbackInvocations++;
            logger.warn("fallback event #" + fallbackInvocations + " for circuit '" + circuitId + "': " + reason);
        }

        void printSummary() {
            logger.info("monitoring summary: total fallback events=" + fallbackInvocations);
        }
    }

    static class PaymentCircuitBreaker {
        private final String id;
        private final CircuitBreakerConfig config;
        private final Deque<CallResult> callResults = new ArrayDeque<>();
        private State state = State.CLOSED;
        private final Logger logger;
        private final FallbackEventMonitor monitor;

        PaymentCircuitBreaker(String id, CircuitBreakerConfig config, Logger logger, FallbackEventMonitor monitor) {
            this.id = id;
            this.config = config;
            this.logger = logger;
            this.monitor = monitor;
        }

        // processPayment(...) with @CircuitBreaker, falls back to paymentFallback(...) on open circuit or failure
        String processPayment(ThirdPartyPaymentClient client, String transactionId, int latencyMs, boolean succeeds) {
            if (state == State.OPEN) {
                monitor.recordFallback(id, "circuit OPEN, call short-circuited for txnId=" + transactionId);
                return paymentFallback(transactionId);
            }

            CallResult result = client.charge(transactionId, latencyMs, succeeds);
            boolean isSlow = result.latencyMs >= config.slowCallDurationThresholdMs;
            recordResult(result);

            if (!result.succeeded) {
                monitor.recordFallback(id, "downstream call failed for txnId=" + transactionId);
                return paymentFallback(transactionId);
            }
            if (isSlow) {
                logger.warn("call for txnId=" + transactionId + " exceeded slow-call threshold ("
                        + result.latencyMs + "ms >= " + config.slowCallDurationThresholdMs + "ms)");
            }
            return "payment confirmed for txnId=" + transactionId;
        }

        // paymentFallback(...) -- Resilience4j fallback method
        private String paymentFallback(String transactionId) {
            return "fallback: payment for txnId=" + transactionId + " queued for retry";
        }

        private void recordResult(CallResult result) {
            if (callResults.size() == config.slidingWindowSize) {
                callResults.removeFirst();
            }
            callResults.addLast(result);

            if (callResults.size() < config.slidingWindowSize) {
                return;
            }

            long failures = callResults.stream().filter(r -> !r.succeeded).count();
            long slowCalls = callResults.stream().filter(r -> r.latencyMs >= config.slowCallDurationThresholdMs).count();
            int failureRate = (int) ((failures * 100) / callResults.size());
            int slowCallRate = (int) ((slowCalls * 100) / callResults.size());
            logger.info("circuit '" + id + "' window full: failureRate=" + failureRate
                    + "%, slowCallRate=" + slowCallRate + "%");

            if (failureRate >= config.failureRateThreshold || slowCallRate >= config.slowCallRateThreshold) {
                state = State.OPEN;
                logger.warn("circuit '" + id + "' thresholds breached, transitioning to OPEN");
            }
        }
    }

    private static final Logger LOGGER = new Logger("PaymentCircuitBreakerDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        CircuitBreakerConfig config = new CircuitBreakerConfig();
        LOGGER.info("thirdPartyPaymentApi config: slidingWindowSize=" + config.slidingWindowSize
                + ", failureRateThreshold=" + config.failureRateThreshold
                + ", slowCallDurationThresholdMs=" + config.slowCallDurationThresholdMs);

        FallbackEventMonitor monitor = new FallbackEventMonitor(LOGGER);
        ThirdPartyPaymentClient client = new ThirdPartyPaymentClient(LOGGER);
        PaymentCircuitBreaker circuitBreaker = new PaymentCircuitBreaker("thirdPartyPaymentApi", config, LOGGER, monitor);

        Object[][] transactions = {
                { "TXN-1001", 300, true },
                { "TXN-1002", 2500, true },
                { "TXN-1003", 2800, true },
                { "TXN-1004", 3000, false },
                { "TXN-1005", 2600, true },
                { "TXN-1006", 400, true },
        };

        for (Object[] txn : transactions) {
            String result = circuitBreaker.processPayment(client, (String) txn[0], (int) txn[1], (boolean) txn[2]);
            LOGGER.info("result=" + result);
        }

        monitor.printSummary();
        LOGGER.info("End");
    }
}
