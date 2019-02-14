package com.example.momomo.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.clj.fastble.data.BleDevice;
import com.example.momomo.myapplication.R;

import java.util.List;

public class deviceshowAdapter extends RecyclerView.Adapter<deviceshowAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<BleDevice> devicesitemList;
    private OnDeviceClickListener onDeviceClickListener;
    public deviceshowAdapter(Context context,List<BleDevice> devicesitemList){
        this.devicesitemList=devicesitemList;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);

    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        View view;
        TextView name;
        TextView mac;
        Button connect;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            name=(TextView) view.findViewById(R.id.device_name);
            mac=(TextView) view.findViewById(R.id.device_mac);
            connect=(Button) view.findViewById(R.id.connect);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.deviceitem,viewGroup,false);
        return new ViewHolder(view);
    }
    public interface OnDeviceClickListener{
        void onClick( int position);
    }
    public void adddevice(BleDevice device) {
        devicesitemList.add(device);
        notifyItemInserted(devicesitemList.size());
    }
    public void clear(){
        devicesitemList.clear();
        notifyDataSetChanged();
    }
    public void setOnDeviceClickListener(OnDeviceClickListener
                                               onDeviceClickListener ){
        this.onDeviceClickListener=onDeviceClickListener;
    }
    @Override
    public void onBindViewHolder( ViewHolder viewHolder, final int position) {
        final BleDevice bleDevice=devicesitemList.get(position);
        viewHolder.name.setText(bleDevice.getName());
        viewHolder.mac.setText(bleDevice.getMac());
        if( onDeviceClickListener!= null) {
            viewHolder.connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeviceClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return devicesitemList.size();
    }
}
