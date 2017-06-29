package com.cx.coolweather.fragment;

/*
 *  @项目名：  CoolWeather 
 *  @包名：    com.cx.coolweather.fragment
 *  @文件名:   ChooseAreaFragment
 *  @创建者:   YHDN
 *  @创建时间:  2017/6/28 16:50
 *  @描述：    TODO
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cx.coolweather.R;
import com.cx.coolweather.db.City;
import com.cx.coolweather.db.County;
import com.cx.coolweather.db.Province;
import com.cx.coolweather.util.HttpUtil;
import com.cx.coolweather.util.ToastHelper;
import com.cx.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment
        extends Fragment
{
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY     = 1;
    public static final int LEVEL_COUNTY   = 2;

    private List<String> dataList = new ArrayList<>();
    private TextView             mTitleTx;
    private Button               mBackBtn;
    private ListView             mLv;
    private ArrayAdapter<String> mAdapter;
    //当前选中的级别
    private int                  currentlevenLevel;
    //选中的省份
    private Province             selectedProvince;
    //选中的城市
    private City                 selectedCity;
    //省列表
    private List<Province>       provinceList;
    //市列表
    private List<City>           cityList;
    //县列表
    private List<County>         countyList;
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        mTitleTx = (TextView) view.findViewById(R.id.title_text);
        mBackBtn = (Button) view.findViewById(R.id.back_button);
        mLv = (ListView) view.findViewById(R.id.list_view);

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        mLv.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentlevenLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentlevenLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentlevenLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentlevenLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如没有再去服务器上查询
     */
    private void queryProvinces() {
        mTitleTx.setText("中国");
        mBackBtn.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mLv.setSelection(0);
            currentlevenLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }

    }


    /**
     * 查询选中的省内所有的市，优先从数据库查询，如没有再去服务器上查询
     */
    private void queryCities() {
        mTitleTx.setText(selectedProvince.getProvinceName());
        mBackBtn.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid=?", String.valueOf(selectedProvince.getId()))
                              .find(City.class);

        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mLv.setSelection(0);
            currentlevenLevel = LEVEL_CITY;
        } else {
            int    provinceCode = selectedProvince.getProvinceCode();
            String address      = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询选中的市内所有的县，优先从数据库查询，如没有再去服务器上查询
     */
    private void queryCounties() {
        mTitleTx.setText(selectedCity.getCityName());
        mBackBtn.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid=?", String.valueOf(selectedCity.getId()))
                                .find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mLv.setSelection(0);
            currentlevenLevel = LEVEL_COUNTY;
        } else {
            int    provinceCode = selectedProvince.getProvinceCode();
            int    cityCode     = selectedCity.getCityCode();
            String address      = "http://guolin.tech/api/china/" + provinceCode +"/"+ cityCode;
            queryFromServer(address, "county");
        }
    }

    /**.
     * 根据传入的地址和类型从服务器上查询省市县数据
     * @param address
     * @param type
     */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过 runOnUiTread() 方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        ToastHelper.show(getContext(), "加载失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException
            {
                String responesText = response.body()
                                              .string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responesText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responesText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responesText, selectedCity.getId());
                }

                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
