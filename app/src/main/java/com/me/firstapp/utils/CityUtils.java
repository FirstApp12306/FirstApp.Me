package com.me.firstapp.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.me.firstapp.entity.City;
import com.me.firstapp.entity.Province;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class CityUtils {
    /**
     * 所有省
     */
    public  String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    public  Map<String, String[]> mCitisDatasMap;

    /**
     * 当前省的名称
     */
    public String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    public String mCurrentCityName;

    private Context context;

    public CityUtils(Context context) {
        this.context = context;
        initProvinceDatas();
    }

    /**
     * 解析省市区的XML数据
     */
    private void initProvinceDatas()
    {
        List<Province> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_city.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).name;
                List<City> cityList = provinceList.get(0).cityList;
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).name;
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            mCitisDatasMap = new HashMap<>();
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).name;
                List<City> cityList = provinceList.get(i).cityList;
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).name;
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).name, cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
}
