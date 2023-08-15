package sa.med.imc.myimc.SYSQUO.Chat.chat.channels;

import com.twilio.chat.Channel;

import java.util.Comparator;

import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.ImcApplication;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;


public class CustomChannelComparator  implements Comparator<Channel> {
  private String defaultChannelName;

  CustomChannelComparator() {
    String mrnNumber = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_MRN, null);
    String physician = SharedPreferencesUtils.getInstance(ImcApplication.getInstance()).getValue(Constants.KEY_VIDEO_PHYSICIAN, null);
    defaultChannelName = mrnNumber+"_"+physician;
  }

  @Override
  public int compare(Channel lhs, Channel rhs) {
    if (lhs.getFriendlyName().contentEquals(defaultChannelName)) {
      return -100;
    } else if (rhs.getFriendlyName().contentEquals(defaultChannelName)) {
      return 100;
    }
    return lhs.getFriendlyName().toLowerCase().compareTo(rhs.getFriendlyName().toLowerCase());
  }
}
