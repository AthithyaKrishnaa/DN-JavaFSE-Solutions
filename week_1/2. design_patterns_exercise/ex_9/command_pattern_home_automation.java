// Exercise 9: Implementing the Command Pattern
// Scenario: A home automation system where commands can be issued to
// turn devices on or off.

interface Command {
    void execute();
}

// Receiver
class Light {
    private String location;

    public Light(String location) {
        this.location = location;
    }

    public void turnOn() {
        System.out.println(location + " light is now ON.");
    }

    public void turnOff() {
        System.out.println(location + " light is now OFF.");
    }
}

// Concrete commands
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOn();
    }
}

class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOff();
    }
}

// Invoker
class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== COMMAND PATTERN: HOME AUTOMATION ===");

        Light livingRoomLight = new Light("Living Room");

        Command lightOn = new LightOnCommand(livingRoomLight);
        Command lightOff = new LightOffCommand(livingRoomLight);

        RemoteControl remote = new RemoteControl();

        System.out.println("\nPressing ON button:");
        remote.setCommand(lightOn);
        remote.pressButton();

        System.out.println("\nPressing OFF button:");
        remote.setCommand(lightOff);
        remote.pressButton();

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("The RemoteControl (invoker) issues commands without knowing");
        System.out.println("how each command actually turns the light on or off.");
    }
}
