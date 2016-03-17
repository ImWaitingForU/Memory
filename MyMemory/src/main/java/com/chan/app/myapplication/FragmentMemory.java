package com.chan.app.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.chan.app.utils.ChildEntity;
import com.chan.app.utils.GroupEntity;
import com.chan.app.utils.Memory;
import com.chan.app.utils.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class FragmentMemory extends Fragment {
    private View v;
    private ExpandableListView expandableListView;
    private List<GroupEntity> lists;
    private MyAdapter adapter;

    private SwipeRefreshLayout mRefreshLayout;
    private static final int REFRESH_COMPLETE = 0X111;

    private Bundle bundle;
    public static ArrayList<String> timeList;
    public static ArrayList<String> msgList;
    public static ArrayList<String> objectId;
    private List<GroupEntity> groupList;
    private BmobQuery<Memory> bmobQuery; //查询数据工具
    private static final int QUERY_FINISH = 0x222;

    private Memory memory;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    toast("刷新完成");
                    mRefreshLayout.setRefreshing(false);//停止刷新
                    break;
                case QUERY_FINISH:
                    initView(); //查询完了再执行初始化视图
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_memory, container, false);
        myQuery();
        return v;
    }

    private void initView() {
        //初始化ExpandableListView
        lists = initList();
        adapter = new MyAdapter(getActivity(), lists);
        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null); /* 去掉默认带的箭头*/
        expandableListView.setSelection(0);
        /* 设置默认选中项 遍历所有group,将所有项设置成默认展开*/
        final int groupCount = expandableListView.getCount();
        for (int i = 0; i < groupCount; i++)

            expandableListView.expandGroup(i);

        expandableListView.setClickable(false);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long
                    id) {
                Intent i = new Intent(getActivity(),ShowMemory.class);
                i.putExtra("time",timeList.get(groupPosition));
                i.putExtra("allMsg",msgList.get(groupPosition));
                i.putExtra("objectId",objectId.get(groupPosition));
                startActivity(i);
                return true;
            }
        });

        /*expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });*/

        //初始化下拉刷新组件
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.mSwipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myQuery();
                handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 500);
            }

        });

    }

    private List<GroupEntity> initList() {

        groupList = new ArrayList<GroupEntity>();

        for (int i = 0; i < timeList.size(); i++) {
            GroupEntity groupEntity = new GroupEntity(timeList.get(i));
            List<ChildEntity> childList = new ArrayList<ChildEntity>();
            ChildEntity childStatusEntity = new ChildEntity(msgList.get(i));
            childList.add(childStatusEntity);
            groupEntity.setChildEntities(childList);
            groupList.add(groupEntity);
        }

        return groupList;
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    /*查询数据*/
    private void myQuery() {
        timeList = new ArrayList<String>();
        msgList = new ArrayList<String>();
        objectId = new ArrayList<String>();
        bmobQuery = new BmobQuery<Memory>();
        //启用缓存功能
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.IGNORE_CACHE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                bmobQuery.findObjects(getActivity(), new FindListener<Memory>() {
                    @Override
                    public void onSuccess(List<Memory> list) {
                        toast("查询成功：共" + list.size() + "条数据。");
                        for (Memory memory : list) {
                            objectId.add(memory.getObjectId());
                            timeList.add(memory.getCreatedAt());
                            msgList.add(memory.getAllMsg());
                        }
                        Log.d("objectId", "-----" + objectId);
                        Log.d("myList", "-----" + timeList);
                        Log.d("myChildList", "-----" + msgList);
                        handler.sendEmptyMessage(QUERY_FINISH);
                    }

                    @Override
                    public void onError(int i, String s) {
                        toast("查询失败：" + s);
                    }
                });
            }
        }).start();

    }

    /*删除数据*/
    private void myDelete(String objcectId){
        memory = new Memory();
        memory.setObjectId(objcectId);
        memory.delete(getActivity(), new DeleteListener() {
            @Override
            public void onSuccess() {
                toast("删除成功");
            }

            @Override
            public void onFailure(int i, String s) {
                toast("删除失败");
            }
        });
    }


}
