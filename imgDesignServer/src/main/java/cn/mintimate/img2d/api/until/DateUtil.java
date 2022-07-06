package cn.mintimate.img2d.api.until;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 *
 * @author Mintimate
 */
public class DateUtil {
    /**
     * 格式化日期
     *
     * @param StrDate
     * @return
     */
    public static Date parseDate(String StrDate) {
        // 获得2009年06月01日 的Date对象
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate1 = null;
        try {
            myDate1 = dateFormat1.parse(StrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myDate1;
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        // 获得2009年06月01日 的Date对象
        DateFormat dateFormat1 = new SimpleDateFormat(format);
        String myDate1 = dateFormat1.format(date);

        return myDate1;
    }

    /**
     * 根据日期获取日期所在月份的天数
     *
     * @param date
     * @return
     */
    public static int dayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

}
