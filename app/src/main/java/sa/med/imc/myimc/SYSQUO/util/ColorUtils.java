package sa.med.imc.myimc.SYSQUO.util;

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {


    public static Integer getColorWithAlpha(){
        Random random = new Random();
        return Color.argb( 25,random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

}
