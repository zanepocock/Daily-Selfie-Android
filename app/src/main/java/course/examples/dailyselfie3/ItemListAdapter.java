package course.examples.dailyselfie3;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter {

    private ArrayList<ItemRecord> list = new ArrayList<ItemRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public ItemListAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        ViewHolder holder;

        ItemRecord curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.item_view, null);
            holder.picture = (ImageView) newView.findViewById(R.id.picture);
            holder.date = (TextView) newView.findViewById(R.id.date);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.picture.setImageBitmap(curr.getPicture());
        holder.date.setText("Date: " + curr.getDate());

        return newView;
    }

    static class ViewHolder {

        ImageView picture;
        TextView date;

    }

    public void add(ItemRecord listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public ArrayList<ItemRecord> getList() {
        return list;
    }

    public void removeAllViews() {
        list.clear();
        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        for (File f : storageDir.listFiles()) {
            if (f.isFile()) {
                f.delete();
            }
        }
        this.notifyDataSetChanged();
    }

}