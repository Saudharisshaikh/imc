package sa.med.imc.myimc.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sa.med.imc.myimc.HealthTips.model.HealthTipsResponse;
import sa.med.imc.myimc.Network.Constants;
import sa.med.imc.myimc.Network.SharedPreferencesUtils;
import sa.med.imc.myimc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Health Tips list.
 */

public class HealthTipsAdapter extends RecyclerView.Adapter<HealthTipsAdapter.Viewholder> {
    List<HealthTipsResponse.HealthModel> list;
    Context context;
    int pos_select = -1;
    OnItemClickListener onItemClickListener;
    String lang;

    public HealthTipsAdapter(Context context, List<HealthTipsResponse.HealthModel> list) {
        this.context = context;
        this.list = list;
        lang = SharedPreferencesUtils.getInstance(context).getValue(Constants.KEY_LANGUAGE, Constants.ENGLISH);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_health_tips, parent, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        String videoID = list.get(position).getYoutubeUrl().replace("https://www.youtube.com/embed/", "");
        Picasso.get().load("https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg").resize(400,210).centerCrop().into(holder.image);

        if (lang.equalsIgnoreCase(Constants.ARABIC))
            holder.tv_title.setText(list.get(position).getTitleAr());
        else
            holder.tv_title.setText(list.get(position).getTitleEn());
//
//        holder.view.initialize("AIzaSyBnvm6ECV7aikL82V1_M3THnst6Dz-iKZ8", new YouTubeThumbnailView.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
////                youTubeThumbnailLoader.setVideo(videoID);
//
//                if (youTubeThumbnailLoader == null) {
//                    holder.view.setTag(videoID);
//                } else {
//                    holder.view.setImageBitmap(null);
//                    youTubeThumbnailLoader.setVideo(videoID);
//                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//                        @Override
//                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                            youTubeThumbnailLoader.release();
//                        }
//
//                        @Override
//                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                            Log.e("Youtube ", "Youtube Thumbnail Error");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                //print or show error when initialization failed
//                Log.e("Youtube", "Youtube Initialization Failure");
//
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onSingleClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.image)
        ImageView image;
//        @BindView(R.id.youtubeview)
//        YouTubeThumbnailView view;

        Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnItemClickListener {
        void onSingleClick(int position);
    }
}
