package com.me.firstapp.utils;

import com.me.firstapp.entity.City;
import com.me.firstapp.entity.Province;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class XmlParserHandler extends DefaultHandler {
    /**
     * 存储所有的解析对象
     */
    private List<Province> provinceList = new ArrayList<Province>();

    public XmlParserHandler() {

    }

    public List<Province> getDataList() {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    Province province;
    City city;
    ArrayList<City> cityList ;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // 当遇到开始标记的时候，调用这个方法
        if (qName.equals("province")) {
            province = new Province();
            province.name = attributes.getValue(0);
            cityList = new ArrayList<>();
            province.cityList = cityList;
        } else if (qName.equals("city")) {
            city = new City();
            city.name = attributes.getValue(0);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("city")) {
            province.cityList.add(city);
        } else if (qName.equals("province")) {
            provinceList.add(province);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }
}
