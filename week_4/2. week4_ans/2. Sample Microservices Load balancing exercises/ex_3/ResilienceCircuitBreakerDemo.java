/*
 * Hands on 3 - Resilience Patterns in an API Gateway
 */
import java.util.ArrayDeque;
import java.util.Deque;

public class ResilienceCircuitBreakerDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    enum State { CLOSED, OPEN, HALF_OPEN }

    // resilience4j.circuitbreaker.instances.exampleCircuitBreaker.*
    static class CircuitBreakerConfig {
        final boolean registerHealthIndicator = true;
        final int slidingWindowSize = 10;
        final int failureRateThreshold = 50;
    }

    // Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer()
    static class CircuitBreaker {
        private final String id;
        private final CircuitBreakerConfig config;
        private final Deque<Boolean> callResults = new ArrayDeque<>();
        private State state = State.CLOSED;
        private final Logger logger;

        CircuitBreaker(String id, CircuitBreakerConfig config, Logger logger) {
            this.id = id;
            this.config = config;
            this.logger = logger;
        }

        String call(boolean callSucceeds) {
            if (state == State.OPEN) {
                logger.info("circuit '" + id + "' is OPEN, call short-circuited");
                return "fallback response";
            }

            if (callResults.size() == config.slidingWindowSize) {
                callResults.removeFirst();
            }
            callResults.addLast(callSucceeds);

            long failures = callResults.stream().filter(succeeded -> !succeeded).count();
            int failureRate = (int) ((failures * 100) / callResults.size());
            logger.info("circuit '" + id + "' recorded call success=" + callSucceeds
                    + ", window=" + callResults.size() + "/" + config.slidingWindowSize
                    + ", failureRate=" + failureRate + "%");

            if (callResults.size() == config.slidingWindowSize && failureRate >= config.failureRateThreshold) {
                state = State.OPEN;
                logger.info("circuit '" + id + "' failureRate " + failureRate
                        + "% reached threshold " + config.failureRateThreshold + "%, transitioning to OPEN");
                return "fallback response";
            }

            return callSucceeds ? "downstream response" : "downstream error";
        }

        void attemptReset() {
            state = State.HALF_OPEN;
            callResults.clear();
            logger.info("circuit '" + id + "' wait duration elapsed, transitioning to HALF_OPEN");
        }

        void closeAfterSuccessfulTrial() {
            state = State.CLOSED;
            logger.info("circuit '" + id + "' trial call succeeded, transitioning to CLOSED");
        }
    }

    private static final Logger LOGGER = new Logger("ResilienceCircuitBreakerDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        CircuitBreakerConfig config = new CircuitBreakerConfig();
        LOGGER.info("exampleCircuitBreaker config: registerHealthIndicator=" + config.registerHealthIndicator
                + ", slidingWindowSize=" + config.slidingWindowSize
                + ", failureRateThreshold=" + config.failureRateThreshold);

        CircuitBreaker circuitBreaker = new CircuitBreaker("exampleCircuitBreaker", config, LOGGER);

        boolean[] callOutcomes = { true, true, false, false, false, false, false, true, false, false };
        for (boolean outcome : callOutcomes) {
            LOGGER.info("curl command: curl -s http://localhost:8080/example/orders");
            String response = circuitBreaker.call(outcome);
            LOGGER.info("response=" + response);
        }

        LOGGER.info("curl command: curl -s http://localhost:8080/example/orders");
        LOGGER.info("response=" + circuitBreaker.call(true));

        circuitBreaker.attemptReset();
        LOGGER.info("curl command: curl -s http://localhost:8080/example/orders");
        boolean trialSucceeds = true;
        if (trialSucceeds) {
            circuitBreaker.closeAfterSuccessfulTrial();
            LOGGER.info("response=downstream response");
        }

        LOGGER.info("End");
    }
}
