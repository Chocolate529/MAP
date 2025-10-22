package factory;

import container.Container;
import container.QueueContainer;
import container.SortedArrayContainer;
import container.StackContainer;

public class TaskContainerFactory implements Factory{
    private static TaskContainerFactory instance = null;

    private TaskContainerFactory(){

    }

    @Override
    public Container createContainer(Strategy strategy) {
        if(strategy == Strategy.LIFO){
            return new StackContainer();
        }else if (strategy == Strategy.FIFO){
            return new QueueContainer();
        } else {
            return new SortedArrayContainer();
        }
    }

    public static TaskContainerFactory getInstance(){
        if(instance == null){
            return new TaskContainerFactory();
        }

        return instance;
    }
}
