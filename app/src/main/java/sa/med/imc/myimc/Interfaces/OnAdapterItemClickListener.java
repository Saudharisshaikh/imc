package sa.med.imc.myimc.Interfaces;

import android.view.View;
public interface OnAdapterItemClickListener {
    void onItemClick(int position, View view, String tappedItemName,int quantity);
}