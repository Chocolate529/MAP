package taskrunner;

import factory.Strategy;

public class DuckTaskRunner extends StrategyTaskRunner{



    public DuckTaskRunner(Strategy strategy) {
        super(strategy);
    }

    @Override
    public void executeOneTask() {
        super.executeOneTask();
    }

    @Override
    public void executeAll() {
        super.executeAll();
    }
}
