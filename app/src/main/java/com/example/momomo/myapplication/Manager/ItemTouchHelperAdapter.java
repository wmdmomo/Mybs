package com.example.momomo.myapplication.Manager;

public interface ItemTouchHelperAdapter {
    //移动item
    void onItemMove(int fromPosition,int toPosition);
    //删除item
    void onItemDelete(int position);
}
