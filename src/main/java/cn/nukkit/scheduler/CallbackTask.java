package cn.nukkit.scheduler;

class CallbackTask extends Task{

    protected Object[] method;
    protected Object[] args;

    public void CallbackTask(Object[] method, Object[] args){
        this.method = method;
        this.args = args;
    }

    @Override
    public void onRun(int currentTick){
        Method m = method[0].getClass().getMethod(method[1], method[2]); //method[2] is parameter types
        m.invoke(method[0], ...args);
    }
}
