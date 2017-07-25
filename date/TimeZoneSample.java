package date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneSample {
    private static final TimeZone GMT00 = TimeZone.getTimeZone("GMT+00");
    private static final TimeZone GMT09 = TimeZone.getTimeZone("GMT+09");
    
    private void formatVariousTimeZone() throws ParseException {
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        df.setTimeZone(GMT00);
        System.out.println(String.format("[%s](%s)", df.getTimeZone().getDisplayName(), df.format(now)));
        df.setTimeZone(GMT09);
        System.out.println(String.format("[%s](%s)", df.getTimeZone().getDisplayName(), df.format(now)));
    }
    
    private void parseVariousTimeZone() throws ParseException {
        String now = "2017-01-01 00:00:00+0900";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        df.setTimeZone(GMT00);
        Date date = df.parse(now);
        System.out.println(String.format("[%s](%s)", df.getTimeZone().getDisplayName(), df.format(date)));

        df.setTimeZone(GMT09);
        date = df.parse(now);
        System.out.println(String.format("[%s](%s)", df.getTimeZone().getDisplayName(), df.format(date)));
    }
    
    public static void main(String[] args) {
        TimeZoneSample sample = new TimeZoneSample();
        try {
            sample.formatVariousTimeZone();
            sample.parseVariousTimeZone();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }    
}
