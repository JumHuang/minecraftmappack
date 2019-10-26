package com.jumhuang.minecraftmappack.ui.activity;

import android.*;
import android.content.pm.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.widget.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.jumhuang.minecraftmappack.*;
import com.jumhuang.minecraftmappack.adapter.*;
import com.jumhuang.minecraftmappack.base.*;
import com.jumhuang.minecraftmappack.permission.*;
import com.jumhuang.minecraftmappack.permission.request.*;
import com.jumhuang.minecraftmappack.permission.requestresult.*;
import java.io.*;

import android.support.v7.widget.Toolbar;
import com.jumhuang.minecraftmappack.R;

public class MainActivity extends BaseActivity 
{
	private RecyclerView recycler;
    private SwipeRefreshLayout swipeview;
    private MyAdapter adapter;

	public String SIGN="308201fc30820165a003020102020101300d06092a864886f70d01010b05003043310b30090603550406130238363110300e060355040813075369636875616e310f300d060355040713065a69676f6e673111300f060355040313084a756d4875616e673020170d3136303832373030313331365a180f32313136303830333030313331365a3043310b30090603550406130238363110300e060355040813075369636875616e310f300d060355040713065a69676f6e673111300f060355040313084a756d4875616e6730819f300d06092a864886f70d010101050003818d0030818902818100abb296f60f48713010cca7598302776dd716ce051685b3b813389829a80f646f55613e63cf10722d73f131dac2fa021876cc37e56f643de44e6aaabee42ce38099f43c36f8f2187e7a65d6de2ac96639da539308b052daefa1ab859a72f12f2be306ba048372cf7ef082873557d22096aed732be5f24ee440eb14bc0d95179eb0203010001300d06092a864886f70d01010b0500038181002502af8647f2f8ec17c7d080270cb7ed50fe263171d56ec92a11109162279f9daf457e632b677252f1d5a0cc904cc28544863e7467eb5f40d774b4e69a30c1b0bffd76ac3202400665b55ebbbdd941cc92a1d611ff23f79811b429a5c03ae620f422730e5066d874a855fc3d75518d195e6ed9aeb756d7f2b7820e9d2f84e96b";

	private IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
    private IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();//动态权限请求结果处理
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		recycler = (RecyclerView)findViewById(R.id.main_recycler);
        swipeview = (SwipeRefreshLayout)findViewById(R.id.main_swiperefresh);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        swipeview.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light);

        swipeview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() 
            {
                @Override
                public void onRefresh()
                {
                    swipeview.setRefreshing(false);
                    new Handler().postDelayed(new Runnable() 
                        {
                            @Override
                            public void run()
                            {
                                Read();
                            }
                        }, 3000);
                }
            });

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
		recycler.setItemAnimator(new DefaultItemAnimator());

        recycler.setOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override  
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {  
                    int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();  
                    swipeview.setEnabled(topRowVerticalPosition >= 0);  
                }  

                @Override  
                public void onScrollStateChanged(RecyclerView recyclerView, int newState)
                {  
                    super.onScrollStateChanged(recyclerView, newState);
                }  
			}); 

		try
		{
			if (getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures[0].toCharsString().equals(SIGN))
			{
				if (Build.VERSION.SDK_INT >= 23)
				{
					if (requestPermissions())
					{
						init();
					}
				}
				else
				{
					init();
				}
			}
			else
			{
				finish();
			}
		}
		catch (PackageManager.NameNotFoundException e)
		{
            e.printStackTrace();
        }
    }

	private void init()
	{
		Read();
	}

	private void Read()
    {
		File f=new File(Environment.getExternalStorageDirectory(), "/games/com.mojang/minecraftWorlds");
		if (f.exists())
		{
			File[] file=f.listFiles();
			adapter = new MyAdapter(getApplicationContext(), file);
			recycler.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		else
		{
			Toast("您未安装国际版或存档丢失！");
			f.mkdirs();
		}
    }

	//请求权限
    private boolean requestPermissions()
    {
        //需要请求的权限
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //开始请求权限
        return requestPermissions.requestPermissions(this, permissions, PermissionUtils.ResultCode1);
    }

    //用户授权操作结果（可能授权了，也可能未授权）
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if (requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults))
        {
            //请求的权限全部授权成功，此处可以做自己想做的事了
            //输出授权结果
            Toast("授权成功！", Toast.LENGTH_LONG);
            init();
        }
        else
        {
            //输出授权结果
            Toast("请给APP授权，否则某些功能无法正常使用！", Toast.LENGTH_LONG);
            finish();
        }
    }

	@Override
	protected void onStart()
	{
		Read();
		super.onStart();
	}

	static boolean isExit; 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (!isExit)
			{
				isExit = true;
				Toast("再按一次退出软件!"); 
				new Thread(new Runnable() 
					{
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(3000);
								isExit = false;
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}).start();
			}
			else
			{
				finish();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
