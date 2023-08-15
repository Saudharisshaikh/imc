package sa.med.imc.myimc.Base;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sa.med.imc.myimc.BuildConfig;
import timber.log.Timber;

public abstract class BaseAdaptor<T,E ,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final List<T> dataList = new ArrayList<>();
    private final Context context;
    private RecyclerViewListener<T,E> listener;
    private final Timber.Tree logger = new Timber.DebugTree(){
        @Nullable
        @Override
        protected String createStackElementTag(@NonNull StackTraceElement element) {
            Class<?> k = BaseAdaptor.this.getClass();
            return k!=null ? k.getSimpleName() : super.createStackElementTag(element);

        }

        @Override
        protected void log(int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable t) {
            if (BuildConfig.DEBUG)
                super.log(priority, tag, message, t);
        }
    };

    public BaseAdaptor(Context context, RecyclerViewListener<T, E> listener) {
        this.context = context;
        this.listener = listener;
    }

    public BaseAdaptor(Context context){
        this.context=context;
    }




    public Context getContext() {
        return context;
    }

    public void setData(List<T> dataList ){
        if (dataList==null){
            return;
        }
        this.dataList.addAll(dataList);
    }

    public void setAndClearData(List<T> dataList ){
        if (dataList==null){
            return;
        }
        this.dataList.clear();
        this.dataList.addAll(dataList);
    }

    public List<T> internalDataList() {
        return dataList;
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface RecyclerViewListener<T,E>{
        void onClick(E e,T t);
    }
    public T getItem(int position){
        return this.dataList.get(position);
    }

    public Timber.Tree getLogger() {
        return logger;
    }
    public interface Event{

    }


    public List<T> getDataList() {
        return dataList;
    }


}
