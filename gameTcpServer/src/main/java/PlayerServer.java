import java.io.Serializable;
import java.util.Objects;

public class PlayerServer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String password;
    private int posX;
    private int posY;
    private boolean killed = false;
    private boolean bot = false;

    public String getPlayer() {
        return id;
    }

    public PlayerServer id(String player) {
        this.id = player;
        return this;
    }

    public int getPosX() {
        return posX;
    }

    public PlayerServer posX(int posX) {
        this.posX = posX;
        return this;
    }

    public int getPosY() {
        return posY;
    }

    public PlayerServer posY(int posY) {
        this.posY = posY;
        return this;
    }

    public boolean isKilled() {
        return killed;
    }

    public PlayerServer killed(boolean killed) {
        this.killed = killed;
        return this;
    }

    public boolean isBot() {
        return bot;
    }

    public PlayerServer bot(boolean bot) {
        this.bot = bot;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerServer)) return false;
        PlayerServer that = (PlayerServer) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}