package domain;

public class Duck implements Comparable<Duck> {
    private final Integer rezistena;
    private final Double viteza;
    private final Integer duckId;

    @Override
    public int compareTo(Duck o) {
        int cmp = this.rezistena.compareTo(o.rezistena);
        if (cmp == 0) {
            return this.viteza.compareTo(o.viteza);
        }
        return cmp;
    }

    public Duck(Integer rezistena, Double viteza, Integer duckId) {
        this.rezistena = rezistena;
        this.viteza = viteza;
        this.duckId = duckId;
    }

    public Integer getRezistena() {
        return rezistena;
    }

    @Override
    public String toString() {
        return "Duck{" +
                "rezistena=" + rezistena +
                ", viteza=" + viteza +
                ", duckId=" + duckId +
                '}';
    }

    public Double getViteza() {
        return viteza;
    }

    public Integer getDuckId() {
        return duckId;
    }
}
