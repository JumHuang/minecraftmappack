package com.jumhuang.minecraftmappack.ui.activity;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.jumhuang.minecraftmappack.*;
import com.jumhuang.minecraftmappack.base.*;

import android.support.v7.widget.Toolbar;

public class ProjectActivity extends BaseActivity
{
	private ImageView icon;
	private TextView name;
	private String DATA;

	private EditText output;
	private Button select;

	private CheckBox encrypt;
	private EditText encrypt_edt;

	private RadioGroup mode;
	private RadioButton mode1;
	private RadioButton mode2;

	private Button start;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project);

		icon = (ImageView)findViewById(R.id.project_icon);
		name = (TextView)findViewById(R.id.project_name);
		output = (EditText)findViewById(R.id.project_outputpath);
		select = (Button)findViewById(R.id.project_select);
		encrypt = (CheckBox)findViewById(R.id.project_encrypt);
		encrypt_edt = (EditText)findViewById(R.id.project_encrypt_edt);
		mode = (RadioGroup)findViewById(R.id.project_modegroup);
		mode1 = (RadioButton)findViewById(R.id.project_mode1);
		mode2 = (RadioButton)findViewById(R.id.project_mode2);
		start = (Button)findViewById(R.id.project_start);

		Toolbar toolbar = (Toolbar) findViewById(R.id.project_toolbar);
		toolbar.setTitle("地图管理");
        setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
			{ 
				@Override 
				public void onClick(View v)
				{ 
					finish(); 
				} 
			}); 

		DATA = getIntent().getStringExtra("DATA");
		if (DATA == null)
		{
			finish();
		}

		icon.setImageBitmap(BitmapFactory.decodeFile(DATA + "/world_icon.jpeg"));
		name.setText(ReadFile(DATA + "/levelname.txt"));

		name.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					EditText edt=new EditText(ProjectActivity.this);
					edt.setText(ReadFile(DATA + "/levelname.txt"));
					edt.setHintTextColor(Color.GRAY);
					edt.setHint("请输入地图名称");

					new AlertDialog.Builder(ProjectActivity.this)
						.setTitle("修改地图名称")
						.setView(edt)
						.setPositiveButton("确认", new AlertDialog.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2)
							{

							}
						}).setNegativeButton("取消", null).show();
				}
			});

		select.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{

				}
			});
	}
}
