package cn.nukkit.level.physics;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.scheduler.AsyncTask;

import java.util.ArrayList;

/**
 * @author PeratX
 */
public class PhysicsTask extends AsyncTask{
    @Override
    public void onRun() {
        for (Level level : new ArrayList<>(Server.getInstance().getLevels().values())){
            level.runPhysics();
        }
    }

    @Override
    public void onCompletion(Server server) {
        server.physicsDone = true;
    }
}
