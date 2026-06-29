// Exercise 3: Implementing the Builder Pattern
// Scenario: Creating complex Computer objects with multiple optional parts
// using the Builder Pattern.

class Computer {
    // Required
    private final String cpu;
    private final String ram;
    // Optional
    private final String storage;
    private final String gpu;
    private final boolean wifiEnabled;
    private final boolean bluetoothEnabled;

    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.gpu = builder.gpu;
        this.wifiEnabled = builder.wifiEnabled;
        this.bluetoothEnabled = builder.bluetoothEnabled;
    }

    public String toString() {
        return "Computer Configuration:" +
                "\n  CPU       : " + cpu +
                "\n  RAM       : " + ram +
                "\n  Storage   : " + storage +
                "\n  GPU       : " + gpu +
                "\n  WiFi      : " + wifiEnabled +
                "\n  Bluetooth : " + bluetoothEnabled;
    }

    public static class Builder {
        private String cpu;
        private String ram;
        private String storage = "256GB SSD";
        private String gpu = "Integrated Graphics";
        private boolean wifiEnabled = false;
        private boolean bluetoothEnabled = false;

        public Builder(String cpu, String ram) {
            this.cpu = cpu;
            this.ram = ram;
        }

        public Builder storage(String storage) {
            this.storage = storage;
            return this;
        }

        public Builder gpu(String gpu) {
            this.gpu = gpu;
            return this;
        }

        public Builder wifiEnabled(boolean wifiEnabled) {
            this.wifiEnabled = wifiEnabled;
            return this;
        }

        public Builder bluetoothEnabled(boolean bluetoothEnabled) {
            this.bluetoothEnabled = bluetoothEnabled;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== BUILDER PATTERN: COMPUTER ===");

        System.out.println("\nBuilding a Basic Office Computer:");
        Computer officeComputer = new Computer.Builder("Intel i3", "8GB")
                .build();
        System.out.println(officeComputer);

        System.out.println("\nBuilding a Gaming Computer:");
        Computer gamingComputer = new Computer.Builder("Intel i9", "32GB")
                .storage("2TB NVMe SSD")
                .gpu("NVIDIA RTX 4090")
                .wifiEnabled(true)
                .bluetoothEnabled(true)
                .build();
        System.out.println(gamingComputer);

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("The Builder Pattern allows step-by-step construction of");
        System.out.println("Computer objects with different optional configurations,");
        System.out.println("without needing multiple constructors.");
    }
}
