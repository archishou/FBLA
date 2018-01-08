import java.util.ArrayList;
import java.util.List;

public class SQLController {
    public List<Integer> bookid, userid, transactionid, schoolid, settingid, numbooks, lim, transUserId, transBookId, userSchoolId;
    public List<Double> fine;
    public List<String> author, bookName, schoolname, trnDate, rDate, userName;
    public List<Boolean> checkOut, teacherYN;
    public SQLController () {
        userSchoolId = new ArrayList<>();
        bookid = new ArrayList<>();
        userid = new ArrayList<>();
        transactionid = new ArrayList<>();
        schoolid = new ArrayList<>();
        settingid = new ArrayList<>();
        numbooks = new ArrayList<>();
        lim = new ArrayList<>();
        transUserId = new ArrayList<>();
        transBookId = new ArrayList<>();
        fine = new ArrayList<>();
        author = new ArrayList<>();
        bookName = new ArrayList<>();
        schoolname = new ArrayList<>();
        trnDate = new ArrayList<>();
        rDate = new ArrayList<>();
        userName = new ArrayList<>();
        checkOut = new ArrayList<>();
        teacherYN = new ArrayList<>();
        userid.add(3769891);
        userName.add("Archishmaan");
        teacherYN.add(true);
        lim.add(10);
        numbooks.add(0);
        userSchoolId.add(58);
        bookid.add(9900);
        bookName.add("Harry Potter");
        author.add("JKROWLING");
        checkOut.add(false);
    }
}
