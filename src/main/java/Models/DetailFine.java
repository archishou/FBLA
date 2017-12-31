package Models;

import java.util.ArrayList;
import java.util.List;

public class DetailFine {
    List<String> bookNames = new ArrayList<>();
    List<Double> fines = new ArrayList<>();
    List<Integer> daysLate = new ArrayList<>();

    public DetailFine(List<String> bookNames, List<Double> fines, List<Integer> daysLate) {
        this.bookNames = bookNames;
        this.fines = fines;
        this.daysLate = daysLate;
    }

    public List<String> getBookNames() {
        return bookNames;
    }

    public void setBookNames(List<String> bookNames) {
        this.bookNames = bookNames;
    }

    public List<Double> getFines() {
        return fines;
    }

    public void setFines(List<Double> fines) {
        this.fines = fines;
    }

    public List<Integer> getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(List<Integer> daysLate) {
        this.daysLate = daysLate;
    }
}
