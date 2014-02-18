package saltr.resource;

/**
 * Created by Khachatur on 2/13/14.
 */
public class Params {
    private String command;
    private String arguments;

    public Params(String command, String arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getCommand() {

        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
