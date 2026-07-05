/*
 * Hands on 3 - Fetch quiz attempt details using HQL
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class QuizAttemptDetailsDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class User {
        private int id;
        private String username;
        User(int id, String username) { this.id = id; this.username = username; }
        String getUsername() { return username; }
    }

    static class QOption {
        private int id;
        private String text;
        private double score;
        QOption(int id, String text, double score) { this.id = id; this.text = text; this.score = score; }
        String getText() { return text; }
        double getScore() { return score; }
    }

    static class Question {
        private int id;
        private String text;
        private List<QOption> options = new ArrayList<>();
        Question(int id, String text) { this.id = id; this.text = text; }
        String getText() { return text; }
        List<QOption> getOptions() { return options; }
    }

    static class Attempt {
        private int id;
        private User user;
        private LocalDate attemptedDate;
        private List<Question> questions = new ArrayList<>();
        private Set<Integer> selectedOptionIds = new LinkedHashSet<>();
        Attempt(int id, User user, LocalDate attemptedDate) {
            this.id = id;
            this.user = user;
            this.attemptedDate = attemptedDate;
        }
        User getUser() { return user; }
        LocalDate getAttemptedDate() { return attemptedDate; }
        List<Question> getQuestions() { return questions; }
        boolean isSelected(int optionId) { return selectedOptionIds.contains(optionId); }
    }

    static class AttemptRepository {
        private final List<Attempt> table = new ArrayList<>();
        void save(Attempt attempt) { table.add(attempt); }
        Attempt getAttempt(int userId, int attemptId) {
            for (Attempt attempt : table) {
                if (attempt.id == attemptId && attempt.user.id == userId) {
                    return attempt;
                }
            }
            return null;
        }
    }

    static class AttemptService {
        private final AttemptRepository attemptRepository;
        AttemptService(AttemptRepository attemptRepository) { this.attemptRepository = attemptRepository; }
        Attempt getAttempt(int userId, int attemptId) { return attemptRepository.getAttempt(userId, attemptId); }
    }

    private static final Logger LOGGER = new Logger("QuizAttemptDetailsDemo");
    private static AttemptService attemptService;

    private static void testGetAttemptDetail() {
        LOGGER.info("Start");
        Attempt attempt = attemptService.getAttempt(1, 1);
        System.out.println("User: " + attempt.getUser().getUsername());
        System.out.println("Attempted Date: " + attempt.getAttemptedDate());
        for (Question question : attempt.getQuestions()) {
            System.out.println(question.getText());
            int index = 1;
            for (QOption option : question.getOptions()) {
                System.out.printf("%d) %-15s %-6s %s%n", index++, option.getText(),
                        option.getScore(), attempt.isSelected(option.id));
            }
        }
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        User user = new User(1, "jane.doe");

        Question q1 = new Question(1, "What is the extension of the hyper text markup language file?");
        q1.getOptions().add(new QOption(1, ".xhtm", 0.0));
        q1.getOptions().add(new QOption(2, ".ht", 0.0));
        q1.getOptions().add(new QOption(3, ".html", 1.0));
        q1.getOptions().add(new QOption(4, ".htmx", 0.0));

        Question q2 = new Question(2, "What is the maximum level of heading tag can be used in a HTML page?");
        q2.getOptions().add(new QOption(5, "5", 0.0));
        q2.getOptions().add(new QOption(6, "3", 0.0));
        q2.getOptions().add(new QOption(7, "4", 0.0));
        q2.getOptions().add(new QOption(8, "6", 1.0));

        Question q3 = new Question(3, "The HTML document itself begins with <html> and ends </html>. State True of False");
        q3.getOptions().add(new QOption(9, "false", 0.0));
        q3.getOptions().add(new QOption(10, "true", 1.0));

        Question q4 = new Question(4, "Choose the right option to store text value value in a variable");
        q4.getOptions().add(new QOption(11, "'John'", 0.5));
        q4.getOptions().add(new QOption(12, "John", 0.0));
        q4.getOptions().add(new QOption(13, "\"John\"", 0.5));
        q4.getOptions().add(new QOption(14, "/John/", 0.0));

        Attempt attempt = new Attempt(1, user, LocalDate.of(2026, 6, 20));
        attempt.getQuestions().add(q1);
        attempt.getQuestions().add(q2);
        attempt.getQuestions().add(q3);
        attempt.getQuestions().add(q4);
        attempt.selectedOptionIds.add(3);
        attempt.selectedOptionIds.add(6);
        attempt.selectedOptionIds.add(10);
        attempt.selectedOptionIds.add(11);

        AttemptRepository attemptRepository = new AttemptRepository();
        attemptRepository.save(attempt);
        attemptService = new AttemptService(attemptRepository);

        testGetAttemptDetail();
    }
}
