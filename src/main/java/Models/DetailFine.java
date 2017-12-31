package Models;

import java.util.ArrayList;
import java.util.List;

public class DetailFine {
    String bookNames;
    double fines;
    int daysLate;

    public DetailFine(String bookNames, double fines, int daysLate) {
        this.bookNames = bookNames;
        this.fines = fines;
        this.daysLate = daysLate;
    }

    public String getBookNames() {
        return bookNames;
    }

    public void setBookNames(String bookNames) {
        this.bookNames = bookNames;
    }

    public double getFines() {
        return fines;
    }

    public void setFines(double fines) {
        this.fines = fines;
    }

    public int getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(int daysLate) {
        this.daysLate = daysLate;
    }
}
