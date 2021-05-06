import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class StateD implements Serializable {
    private static final long serialVersionUID = 1L;
    private Commands commands;
    private Set<PlayerServer> players = new HashSet<>();


    public Set<PlayerServer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerServer> players) {
        this.players = players;
    }

    public Commands getCommands() {
        return commands;
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    public static enum Commands implements Serializable {
        NEW_PLAYER,
        KILL_PLAYER,
        NEW_BOT,
    }


}