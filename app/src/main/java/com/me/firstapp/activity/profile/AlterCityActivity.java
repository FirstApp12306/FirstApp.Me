package com.me.firstapp.activity.profile;

import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.me.firstapp.R;
import com.me.firstapp.activity.BaseActivity;
import com.me.firstapp.application.MyApplication;
import com.me.firstapp.entity.User;
import com.me.firstapp.global.GlobalContants;
import com.me.firstapp.service.LocationService;
import com.me.firstapp.utils.CityUtils;
import com.me.firstapp.utils.DatabaseUtils;
import com.me.firstapp.utils.DialogUtils;
import com.me.firstapp.utils.Event;
import com.me.firstapp.utils.LogUtils;
import com.me.firstapp.wheel.OnWheelChangedListener;
import com.me.firstapp.wheel.WheelView;
import com.me.firstapp.wheel.adapters.ArrayWheelAdapter;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
@ContentView(R.layout.activity_alter_city)
public class AlterCityActivity extends BaseActivity implements View.OnClickListener, OnWheelChangedListener {

    @ViewInject(R.id.activity_alter_city_btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.activity_alter_city_btn_ok)
    private Button btnOK;
    @ViewInject(R.id.activity_alter_city_ll_location)
    private LinearLayout llLocation;
    @ViewInject(R.id.activity_alter_city_tv_location)
    private TextView tvLocation;
    @ViewInject(R.id.activity_alter_city_wv_province)
    private WheelView wvProvince;
    @ViewInject(R.id.activity_alter_city_wv_city)
    private WheelView wvCity;

    private String[] mProvinceDatas;
    private Map<String, String[]> mCitisDatasMap;
    private  String mCurrentProviceName;
    private String mCurrentCityName;
    private CityUtils cityUtils;

    private String userID;
    private Dialog loadingDialog;
    private DatabaseUtils databaseUtils;
    private User user;

    private String city;

    private LocationService locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userID = getIntent().getStringExtra("user_id");
        databaseUtils = new DatabaseUtils(this);
        user = databaseUtils.queryUser(userID);

        btnBack.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        llLocation.setOnClickListener(this);
        wvProvince.addChangingListener(this);
        wvCity.addChangingListener(this);

        setWheel();

        //获取locationservice实例
        locationService = ((MyApplication)getApplication()).locationService;
        //注册监听
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }

    private void setWheel(){
        cityUtils = new CityUtils(this);
        mProvinceDatas = cityUtils.mProvinceDatas;
        mCitisDatasMap = cityUtils.mCitisDatasMap;
        mCurrentProviceName = cityUtils.mCurrentProviceName;
        mCurrentCityName = cityUtils.mCurrentCityName;
        city = mCurrentProviceName +","+ mCurrentCityName;
        LogUtils.d("citycity", city);
        wvProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        // 设置可见条目数量
        wvProvince.setVisibleItems(7);
        wvCity.setVisibleItems(7);
        updateCities();
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private String[] cities;
    private void updateCities() {
        int pCurrent = wvProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        LogUtils.d("mCurrentProviceName", mCurrentProviceName);
        cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        wvCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        wvCity.setCurrentItem(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    /*****
     * @see copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                tvLocation.setText(location.getProvince()+","+location.getCity());
            }
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_alter_city_btn_back :
                finish();
                break;
            case R.id.activity_alter_city_btn_ok :
                sendDataToServer();
                break;
            case R.id.activity_alter_city_ll_location :
                if (!TextUtils.isEmpty(tvLocation.getText().toString())){
                    city = tvLocation.getText().toString();
                }
                sendDataToServer();
                break;
        }
    }

    //更新服务器
    //更新本地数据
    private void sendDataToServer(){
        loadingDialog = DialogUtils.creatLoadingDialog(this, "请稍后...");
        loadingDialog.show();
        RequestParams params = new RequestParams(GlobalContants.UPDATE_USER_CITY_URL);
        params.addQueryStringParameter("user_id", userID);
        params.addQueryStringParameter("user_city", city);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d("result", result);
                updateLocalUser();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                loadingDialog.cancel();
            }
        });
    }

    //更新极光
    private void updateCityInJPush(){
        //暂时先不做
    }

    //更新本地信息
    private void updateLocalUser(){
        ContentValues cv = new ContentValues();
        if (user != null){
            user.user_city = city;
            cv.put("city", user.user_city);
            databaseUtils.updateTable("user", cv, "id = ?", new String[]{user.user_id});
            EventBus.getDefault().post(new Event.CompleteAlterCityEvent(user.user_city));
            finish();
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wvProvince) {
            updateCities();
            city = mCurrentProviceName + "," +cities[wvCity.getCurrentItem()];

        }

        if (wheel == wvCity) {
            city = mCurrentProviceName +","+  cities[wvCity.getCurrentItem()];

        }
        LogUtils.d("citycity", city);
    }
}
