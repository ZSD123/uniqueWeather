package adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import db.FaceText;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseArrayListAdapter extends BaseAdapter {

	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<FaceText> mDatas = new ArrayList<FaceText>();

	public BaseArrayListAdapter(Context context, FaceText... datas) {//...表示可以传入多个参数
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (datas != null && datas.length > 0) {
			mDatas = Arrays.asList(datas);//将一个数组转化为一个List对象，这个方法会返回一个ArrayList类型的对象， 这个ArrayList类并非java.util.ArrayList类，而是Arrays类的静态内部类！用这个对象对列表进行添加删除更新操作，就会报UnsupportedOperationException异常。
		}
	}

	public BaseArrayListAdapter(Context context, List<FaceText> datas) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (datas != null && datas.size() > 0) {
			mDatas = datas;
		}
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}
