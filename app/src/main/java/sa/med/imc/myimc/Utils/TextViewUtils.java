package sa.med.imc.myimc.Utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.ColorInt;

import java.util.List;

import kotlin.Pair;

public class TextViewUtils {

    public static CharSequence colorText(String before,String apply,String after, @ColorInt int color){
        ForegroundColorSpan spanColor = new ForegroundColorSpan(color);
        SpannableStringBuilder builder= new SpannableStringBuilder();
        builder.append(before);
        SpannableString spannableString = new SpannableString(apply);
        spannableString.setSpan(spanColor,0,apply.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        builder.append(after);
        return builder;
    }

    public static CharSequence colorText(List<android.util.Pair<String,Integer>> sources){
        SpannableStringBuilder builder= new SpannableStringBuilder();
        for (android.util.Pair<String,Integer> pair : sources) {
            ForegroundColorSpan spanColor = new ForegroundColorSpan(pair.second);
            SpannableString spannableString = new SpannableString(pair.first);
            spannableString.setSpan(spanColor, 0, pair.first.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(spannableString);
        }
        return builder;
    }
}