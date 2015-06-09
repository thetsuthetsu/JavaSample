import java.util.ArrayList;
import java.util.Collection;

/**
 * Java8以降、以下のコードはNPEを発生させる。
 */
public class CollectionNPETest {
	   public static void main(String[] args) {
	        try {
	            Collection<String> col = new ArrayList<String>();

	            col.removeAll(null);
	            col.retainAll(null);
	        } catch (Throwable th) {
	            th.printStackTrace();
	        }
	    }
}
