import java.util.ArrayList;
import java.util.List;

public class SignInView extends Signin {
    public List<String> creds = new ArrayList<>();
    public SignInView () {
        creds.add("foo:hello");
        creds.add("hello:foo");
    }
    public boolean isCred(String u, String p){
        return creds.contains(u + ":" + p);
    }

}
