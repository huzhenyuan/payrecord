package z.p;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import z.p.data.OrderEntity;

public class LogAdapter extends BaseAdapter {


    private final LayoutInflater mInflater;
    private List<OrderEntity> orderEntityList;
    private Context context;

    public LogAdapter(Context context, List<OrderEntity> orderEntityList) {
        this.context = context;
        this.orderEntityList = orderEntityList;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderEntityList.size();
    }

    @Override
    public OrderEntity getItem(int i) {
        return orderEntityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.log_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        OrderEntity orderEntity = getItem(position);
        holder.text_id.setText(orderEntity.getOrderId());
        holder.text_time.setText(sdf.format(new Date(orderEntity.getCreate())));
        holder.text_depositor.setText(orderEntity.getDepositor());
        holder.text_money.setText(orderEntity.getRechargeAmount());
        holder.text_update_time.setText(sdf.format(new Date(orderEntity.getUpdate())));
        holder.text_sms.setText(orderEntity.getSmsContent());

        return convertView;
    }


    private static class ViewHolder {
        TextView text_id;
        TextView text_time;
        TextView text_depositor;
        TextView text_money;
        TextView text_update_time;
        TextView text_sms;

        ViewHolder(View view) {
            text_id = view.findViewById(R.id.text_id);
            text_time = view.findViewById(R.id.text_time);
            text_depositor = view.findViewById(R.id.text_depositor);
            text_money = view.findViewById(R.id.text_money);
            text_update_time = view.findViewById(R.id.text_update_time);
            text_sms = view.findViewById(R.id.text_sms);
            view.setTag(this);
        }
    }
}
