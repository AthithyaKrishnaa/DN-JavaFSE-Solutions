/*
 * Hands on 2 - Write queries on stock table using Query Methods
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StockQueryMethodsDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class Stock {
        private int stId;
        private String stCode;
        private LocalDate stDate;
        private double stOpen;
        private double stClose;
        private long stVolume;

        Stock(int stId, String stCode, LocalDate stDate, double stOpen, double stClose, long stVolume) {
            this.stId = stId;
            this.stCode = stCode;
            this.stDate = stDate;
            this.stOpen = stOpen;
            this.stClose = stClose;
            this.stVolume = stVolume;
        }

        String getStCode() { return stCode; }
        LocalDate getStDate() { return stDate; }
        double getStOpen() { return stOpen; }
        double getStClose() { return stClose; }
        long getStVolume() { return stVolume; }

        @Override
        public String toString() {
            return "Stock [stCode=" + stCode + ", stDate=" + stDate + ", stOpen=" + stOpen
                    + ", stClose=" + stClose + ", stVolume=" + stVolume + "]";
        }
    }

    static class StockRepository {
        private final List<Stock> table = new ArrayList<>();

        void save(Stock stock) { table.add(stock); }

        List<Stock> findByStCodeAndStDateBetween(String code, LocalDate start, LocalDate end) {
            List<Stock> result = new ArrayList<>();
            for (Stock stock : table) {
                if (stock.getStCode().equals(code)
                        && !stock.getStDate().isBefore(start)
                        && !stock.getStDate().isAfter(end)) {
                    result.add(stock);
                }
            }
            return result;
        }

        List<Stock> findByStCodeAndStOpenGreaterThanOrStCodeAndStCloseGreaterThan(
                String code1, double open, String code2, double close) {
            List<Stock> result = new ArrayList<>();
            for (Stock stock : table) {
                if (stock.getStCode().equals(code1)
                        && (stock.getStOpen() > open || stock.getStClose() > close)) {
                    result.add(stock);
                }
            }
            return result;
        }

        List<Stock> findTop3ByOrderByStVolumeDesc() {
            List<Stock> sorted = new ArrayList<>(table);
            sorted.sort(Comparator.comparingLong(Stock::getStVolume).reversed());
            return sorted.subList(0, Math.min(3, sorted.size()));
        }

        List<Stock> findTop3ByStCodeOrderByStCloseAsc(String code) {
            List<Stock> filtered = new ArrayList<>();
            for (Stock stock : table) {
                if (stock.getStCode().equals(code)) {
                    filtered.add(stock);
                }
            }
            filtered.sort(Comparator.comparingDouble(Stock::getStClose));
            return filtered.subList(0, Math.min(3, filtered.size()));
        }

        void seed() {
            int id = 1;
            String[][] fbSeptember = {
                {"2019-09-03", "184.00", "182.39", "9779400"},
                {"2019-09-04", "184.65", "187.14", "11308000"},
                {"2019-09-05", "188.53", "190.90", "13876700"},
                {"2019-09-06", "190.21", "187.49", "15226800"},
                {"2019-09-09", "187.73", "188.76", "14722400"},
                {"2019-09-10", "187.44", "186.17", "15455900"},
                {"2019-09-11", "186.46", "188.49", "11761700"},
                {"2019-09-12", "189.86", "187.47", "11419800"},
                {"2019-09-13", "187.33", "187.19", "11441100"},
                {"2019-09-16", "186.93", "186.22", "8444800"},
                {"2019-09-17", "186.66", "188.08", "9671100"},
                {"2019-09-18", "188.09", "188.14", "9681900"},
                {"2019-09-19", "188.66", "190.14", "10392700"},
                {"2019-09-20", "190.66", "189.93", "19934200"},
                {"2019-09-23", "189.34", "186.82", "13327600"},
                {"2019-09-24", "187.98", "181.28", "18546600"},
                {"2019-09-25", "181.45", "182.80", "18068300"},
                {"2019-09-26", "181.33", "180.11", "16083300"},
                {"2019-09-27", "180.49", "177.10", "14656200"}
            };
            for (String[] row : fbSeptember) {
                save(new Stock(id++, "FB", LocalDate.parse(row[0]), Double.parseDouble(row[1]),
                        Double.parseDouble(row[2]), Long.parseLong(row[3])));
            }

            String[][] googlHighPrice = {
                {"2019-04-22", "1236.67", "1253.76", "954200"},
                {"2019-04-23", "1256.64", "1270.59", "1593400"},
                {"2019-04-24", "1270.59", "1260.05", "1169800"},
                {"2019-04-25", "1270.30", "1267.34", "1567200"},
                {"2019-04-26", "1273.38", "1277.42", "1361400"},
                {"2019-04-29", "1280.51", "1296.20", "3618400"},
                {"2019-10-17", "1251.40", "1252.80", "1047900"}
            };
            for (String[] row : googlHighPrice) {
                save(new Stock(id++, "GOOGL", LocalDate.parse(row[0]), Double.parseDouble(row[1]),
                        Double.parseDouble(row[2]), Long.parseLong(row[3])));
            }

            String[][] fbTopVolume = {
                {"2019-01-31", "165.60", "166.69", "77233600"},
                {"2018-10-31", "155.00", "151.79", "60101300"},
                {"2018-12-19", "141.21", "133.24", "57404900"}
            };
            for (String[] row : fbTopVolume) {
                save(new Stock(id++, "FB", LocalDate.parse(row[0]), Double.parseDouble(row[1]),
                        Double.parseDouble(row[2]), Long.parseLong(row[3])));
            }

            String[][] nflxLowest = {
                {"2018-12-24", "242.00", "233.88", "9547600"},
                {"2018-12-21", "263.83", "246.39", "21397600"},
                {"2018-12-26", "233.92", "253.67", "14402700"}
            };
            for (String[] row : nflxLowest) {
                save(new Stock(id++, "NFLX", LocalDate.parse(row[0]), Double.parseDouble(row[1]),
                        Double.parseDouble(row[2]), Long.parseLong(row[3])));
            }
        }
    }

    private static final Logger LOGGER = new Logger("StockQueryMethodsDemo");
    private static StockRepository stockRepository;

    private static void testFindByStCodeAndStDateBetween() {
        LOGGER.info("Start");
        List<Stock> stocks = stockRepository.findByStCodeAndStDateBetween(
                "FB", LocalDate.of(2019, 9, 1), LocalDate.of(2019, 9, 30));
        LOGGER.debug("stocks={}", stocks);
        LOGGER.info("End");
    }

    private static void testFindByStCodeAndPriceGreaterThan() {
        LOGGER.info("Start");
        List<Stock> stocks = stockRepository.findByStCodeAndStOpenGreaterThanOrStCodeAndStCloseGreaterThan(
                "GOOGL", 1250, "GOOGL", 1250);
        LOGGER.debug("stocks={}", stocks);
        LOGGER.info("End");
    }

    private static void testFindTop3ByOrderByStVolumeDesc() {
        LOGGER.info("Start");
        List<Stock> stocks = stockRepository.findTop3ByOrderByStVolumeDesc();
        LOGGER.debug("stocks={}", stocks);
        LOGGER.info("End");
    }

    private static void testFindTop3ByStCodeOrderByStCloseAsc() {
        LOGGER.info("Start");
        List<Stock> stocks = stockRepository.findTop3ByStCodeOrderByStCloseAsc("NFLX");
        LOGGER.debug("stocks={}", stocks);
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        stockRepository = new StockRepository();
        stockRepository.seed();

        testFindByStCodeAndStDateBetween();
        testFindByStCodeAndPriceGreaterThan();
        testFindTop3ByOrderByStVolumeDesc();
        testFindTop3ByStCodeOrderByStCloseAsc();
    }
}
