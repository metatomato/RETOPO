package gl.iglou.studio.retopo.TOPO;

/**
 * Created by metatomato on 08.05.15.
 */
public class TopoSavedState {

    static public final int TOPO_DISPLAY_MODE = 0;
    static public final int TOPO_CAPTURE_MODE = 1;

    private int LastMode = -1;
    private String LastTopo = "";

    public TopoSavedState() {
    }

    public int getLastMode() {
        return LastMode;
    }

    public void setLastMode(int lastMode) {
        LastMode = lastMode;
    }

    public String getLastTopo() {
        return LastTopo;
    }

    public void setLastTopo(String lastTopo) {
        LastTopo = lastTopo;
    }

    public String toString() {
        String retString = "Last Mode: " + String.valueOf(LastMode) + ", Last Topo: " + LastTopo;
        return retString;
    }
}
