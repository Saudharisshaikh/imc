package sa.med.imc.myimc.Utils;

/**
 * Created by Razia
 */

public interface Loading {

    void  showLoading();

    void hideLoading();

    void onFail(String msg);

    void onNoInternet();

    default void showLoading(int type){

    }

    default void hideLoading(int type){

    }

}
