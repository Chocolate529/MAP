public class SortingTask extends Task {
    private Integer[] numbers;
    private SortingStrat strategie;
    private Sorter<Integer> sorter;

    public SortingTask(String taskID, String descriere, Integer[] numbers, SortingStrat strategie) {
        super(taskID, descriere);
        this.numbers = numbers;
        setStrategie(strategie); // also sets the sorter
    }

    public SortingStrat getStrategie() {
        return strategie;
    }

    public void setStrategie(SortingStrat strategie) {
        this.strategie = strategie;

        // Choose sorting algorithm
        switch (strategie) {
            case BUBBLE_SORT:
                sorter = new BubbleSort();
                break;
            case QUICK_SORT:
                sorter = new QuickSort();
                break;
            default:
                sorter = null;
        }
    }

    public Integer[] getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer[] numbers) {
        this.numbers = numbers;
    }

    @Override
    public void execute() {
        if (sorter == null) {
            System.out.println("No sorting strategy selected!");
            return;
        }

        sorter.sort(numbers);

        System.out.println("Sorted array (" + strategie + "):");
        for (Integer i : numbers) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
