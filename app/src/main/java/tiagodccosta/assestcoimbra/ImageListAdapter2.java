package tiagodccosta.assestcoimbra;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageListAdapter2 extends RecyclerView.Adapter<ImageListAdapter2.ImageViewHolder> {


    private Context mContext;
    private List<UploadInfo> mUploads;

    public ImageListAdapter2(Context context, List<UploadInfo> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_schedule, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        UploadInfo uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getDescricao());
        Glide.with(mContext)
                .load(uploadCurrent.getUrl())
                .fitCenter()
                .centerCrop()
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.horarioText);
            imageView = itemView.findViewById(R.id.horarioImage);
        }
    }
}
