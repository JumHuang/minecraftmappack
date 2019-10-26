package com.jumhuang.minecraftmappack.adapter;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.jumhuang.minecraftmappack.*;
import com.jumhuang.minecraftmappack.base.*;
import com.jumhuang.minecraftmappack.ui.activity.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>
{
    private Context context;
    private File[] list;
    private LayoutInflater mInflater;

    public MyAdapter(Context context, File[] list)
    {
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount()
    {
        return list != null ?list.length: 0;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = mInflater.inflate(R.layout.item_project, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyHolder holder, final int position)
    {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        File data=list[position];
		
		String HomePath=Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftWorlds/" + data.getName();
		String name=BaseActivity.ReadFile( HomePath+ "/levelname.txt");

		holder.item_icon.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftWorlds/" + data.getName() + "/world_icon.jpeg"));
        holder.item_name.setText(name);
		holder.item_date.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(new File(HomePath).lastModified())));
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
		ImageView item_icon;
        TextView item_name;
		TextView item_date;

        public MyHolder(final View v)
        {
            super(v);

			item_icon = (ImageView)v.findViewById(R.id.item_project_icon);
            item_name = (TextView)v.findViewById(R.id.item_project_name);
			item_date=(TextView)v.findViewById(R.id.item_project_date);

            v.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View p1)
                    {
						Intent intent=new Intent(context, ProjectActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("DATA", Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftWorlds/" + list[getAdapterPosition()].getName());
						context.startActivity(intent);
                    }
                });

			v.setOnLongClickListener(new View.OnLongClickListener()
				{
					@Override
					public boolean onLongClick(View p1)
					{
						String name=BaseActivity.ReadFile(Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftWorlds/" + list[getAdapterPosition()].getName() + "/levelname.txt");

						BaseActivity.Toast(context, BaseActivity.Zip(Environment.getExternalStorageDirectory() + "/games/com.mojang/minecraftWorlds/" + list[getAdapterPosition()].getName(), Environment.getExternalStorageDirectory() + "/" + name + ".zip", false, null));
						return false;
					}
				});
        }
    }
}
