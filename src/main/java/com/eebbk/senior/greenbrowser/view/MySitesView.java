package com.eebbk.senior.greenbrowser.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eebbk.greenbrowser.dialog.RecommandDialog;
import com.eebbk.greenbrowser.image.ImageLoader;
import com.eebbk.greenbrowser.item.MySiteItem;
import com.eebbk.greenbrowser.item.MySiteItem.Type;
import com.eebbk.greenbrowser.model.BaseObject;
import com.eebbk.senior.greenbrowser.R;

/**
 * 
 * 说明：我的导航显示VIEW<br>
 * 公司名称 ：步步高教育电子<br>
 * 
 * @author 李修金
 * @version 1.0
 */
@SuppressLint("NewApi")
public class MySitesView extends LinearLayout {

	/**
	 * 
	 * 说明：网址导航<br>
	 * 公司名称 ：步步高教育电子<br>
	 * 
	 * @author 李修金
	 * @version 1.0
	 */
	class MySiteItemOnSiteNavigationListener implements
			MySiteItem.OnSiteNavigationListener {

		@Override
		public void onSiteNavigation(View v) {
			// TODO Auto-generated method stub
			onSiteNavigationListener.onSiteNavigation(v);
		}

	}

	/**
	 * 
	 * 说明：移除站点<br>
	 * 公司名称 ：步步高教育电子<br>
	 * 
	 * @author 李修金
	 * @version 1.0
	 */
	class MySiteItemOnSiteRemoveListener implements
			MySiteItem.OnSiteRemoveListener {

		@Override
		public void onSiteRemove(View v) {
			// TODO Auto-generated method stub
			onSiteRemoveListener.onSiteRemove(v);
		}
	}

	/**
	 * 
	 * 说明：网址导航接口<br>
	 * 公司名称 ：步步高教育电子<br>
	 * 
	 * @author 李修金
	 * @version 1.0
	 */
	public interface OnSiteNavigationListener {
		public void onSiteNavigation(View v);
	}

	/**
	 * 
	 * 说明：网址移除接口<br>
	 * 公司名称 ：步步高教育电子<br>
	 * 
	 * @author 李修金
	 * @version 1.0
	 */
	public interface OnSiteRemoveListener {
		public void onSiteRemove(View v);
	}

	/**
	 * 定义类型
	 */
	public class SitesType {
		/**
		 * 经常访问
		 */
		public final static int FREQUENTSITES = 0;
		/**
		 * 我的网站
		 */
		public final static int MYSITES = 1;
		/**
		 * 新网推荐
		 */
		public final static int NEWSITES = 2;
	}

	/** 单个网址固定宽度 */
	private static final int FIXEDWIDTH = 155;

	/** 显示列数 */
	private int columnCount = 4;

	/** 我要推荐 */
	private RecommandDialog dialog;

	private Button mButton;

	private Context mContext;

	private GridLayout mGridLayout;

	private ImageLoader mImageLoader;

	/** 頁面布局 */
	private LinearLayout mLinearLayout;

	/** 临时存储网址ITEM */
	private List<MySiteItem> mMySiteItems = new ArrayList<MySiteItem>();

	/** 模式 */
	private int mode;

	private TextView mTextView;

	private View mView;

	/** 网址导航监听器 */
	protected OnSiteNavigationListener onSiteNavigationListener;

	/** 网址删除监听器 */
	protected OnSiteRemoveListener onSiteRemoveListener;

	/** 类型 */
	private int type;

	public MySitesView(Context context) {
		this(context, null);
	}

	public MySitesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.MySiteItem);
		type = typedArray.getInt(R.styleable.MySiteItem_type, 0);
		typedArray.recycle();

		LayoutInflater.from(context).inflate(R.layout.mysitesview, this);
		mTextView = (TextView) findViewById(R.id.mysitesview_title);
		mButton = (Button) findViewById(R.id.mysitesview_button);
		mView = findViewById(R.id.mysitesview_splitline);
		mGridLayout = (GridLayout) findViewById(R.id.mysitesview_gridlayout);
		if (type != SitesType.FREQUENTSITES) {
			LayoutParams mLayoutParams = (LayoutParams) mGridLayout
					.getLayoutParams();
			mLayoutParams.leftMargin = 20;
			mLayoutParams.rightMargin = 20;
			mGridLayout.setLayoutParams(mLayoutParams);
		}
		setDidScreenColumnCount();
		setOnClickListener();
	}

	/**
	 * 添加一个网站到我的网站
	 * 
	 * @param baseObject
	 * 
	 */
	public void addView(BaseObject baseObject) {
		addView(false, baseObject);
	}

	/**
	 * 添加一个网站
	 * 
	 * @param hasLogo
	 * @param baseObject
	 */
	public void addView(boolean hasLogo, BaseObject baseObject) {
		createSiteItem(hasLogo, baseObject);
		if (hasLogo && mImageLoader != null) {
			mImageLoader.doTask();
		}
		mGridLayout.requestLayout();
	}

	/**
	 * 添加多个网站
	 * 
	 * @param baseObjects
	 */
	public void addViews(boolean hasLogo, List<BaseObject> baseObjects) {

		if (baseObjects == null) {
			return;
		}

		for (BaseObject baseObject : baseObjects) {
			createSiteItem(hasLogo, baseObject);
		}

		if (hasLogo && mImageLoader != null) {
			mImageLoader.doTask();
		}
	}

	/**
	 * 创建一个网站
	 * 
	 * @param hasLogo
	 * @param baseObject
	 */
	private void createSiteItem(boolean hasLogo, BaseObject baseObject) {
		MySiteItem mSiteItem;
		GridLayout.LayoutParams param = new GridLayout.LayoutParams();
		param.width = FIXEDWIDTH;
		param.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		if (hasLogo && mImageLoader != null) {
			mSiteItem = new MySiteItem(mContext, null, MySiteItem.LOGO_STYLE);
			mSiteItem.setTextBackgroundResource(baseObject.getResId());
			mSiteItem.setType(Type.LOGO);
			if (baseObject.getResId() == R.drawable.logo_default) {
				mSiteItem.setText(baseObject.getName());
				if (!TextUtils.isEmpty(baseObject.getLogo())) {
					mImageLoader.addTask(baseObject, mSiteItem);
				}
			}

			if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				param.rightMargin = 41;
			} else {
				param.rightMargin = 23;
			}
		} else {
			mSiteItem = new MySiteItem(mContext);
			mSiteItem.setText(baseObject.getName());

			param.setMargins(0, 10, 15, 10);
			param.width = FIXEDWIDTH - 5;
		}

		mSiteItem.setTag(baseObject);
		mSiteItem.setMode(mode);
		mSiteItem.setOnSiteRemoveListener(new MySiteItemOnSiteRemoveListener());
		mSiteItem
				.setOnSiteNavigationListener(new MySiteItemOnSiteNavigationListener());
		mMySiteItems.add(mSiteItem);
		mGridLayout.addView(mSiteItem, param);
	}

	/**
	 * 获取模式
	 * 
	 * @return mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * 获得标题文本
	 * 
	 * @return String
	 */
	public String getTitle() {
		return (String) mTextView.getText();
	}

	/**
	 * 获取类型
	 * 
	 * @return type
	 */
	public int getType() {
		return type;
	}

	/**
	 * 是否存在
	 * 
	 * @param baseObject
	 * @return boolean
	 * 
	 */
	public boolean isExist(BaseObject baseObject) {
		for (int i = 0; i < mGridLayout.getChildCount(); i++) {
			MySiteItem siteItem = (MySiteItem) mGridLayout.getChildAt(i);
			if (((BaseObject) siteItem.getTag()).getUrl().equals(
					baseObject.getUrl())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

		setDidScreenResetContentView();

		Log.i(this.getClass().getName(), "onConfigurationChanged");
	}

	/**
	 * 移除一个网站
	 */
	@Override
	public void removeView(View view) {
		mMySiteItems.remove(view);
		mGridLayout.removeView(view);
		mGridLayout.requestLayout();
		mGridLayout.invalidate();
		setDidScreenResetContentView();
	}

	/**
	 * 移除多个网站
	 */
	@Override
	public void removeViewsInLayout(int start, int count) {
		for (int i = 0; i < count; i++) {
			mMySiteItems.remove(mMySiteItems.size() - 1);
		}
		mGridLayout.removeViewsInLayout(start, count);
		mGridLayout.requestLayout();
		mGridLayout.invalidate();
	}

	/**
	 * 设置BUTTON显示 (我要推荐)
	 * 
	 * @param visible
	 */
	public void setButtonVisible(int visible) {
		mButton.setVisibility(visible);
	}

	/**
	 * 设置显示内容
	 * 
	 * @param hasLogo
	 * @param list
	 * 
	 */
	public void setConentView(boolean hasLogo, List<BaseObject> list) {
		setConentView(hasLogo, list, null);
	}

	/**
	 * 设置显示内容
	 * 
	 * @param hasLogo
	 * @param list
	 * @param mImageLoader
	 * 
	 */
	public void setConentView(boolean hasLogo, List<BaseObject> list,
			ImageLoader mImageLoader) {
		this.mImageLoader = mImageLoader;

		mMySiteItems.clear();
		mGridLayout.removeAllViews();
		mGridLayout.requestLayout();

		addViews(hasLogo, list);
	}

	/**
	 * 适配横竖屏一行显示数据列数
	 */
	private void setDidScreenColumnCount() {
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			columnCount = 5;
		} else if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			columnCount = 4;
		}
		mGridLayout.setColumnCount(columnCount);
	}

	private void setDidScreenResetContentView() {
		int total = mMySiteItems.size();
		if (total == 0) {
			return;
		}

		mGridLayout.removeAllViews();
		setDidScreenColumnCount();
		for (int j = 0, c = 0, r = 0; j < total; j++, c++) {
			if (c == columnCount) {
				c = 0;
				r++;
			}
			GridLayout.LayoutParams param = new GridLayout.LayoutParams();
			param.width = FIXEDWIDTH;
			param.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
			if (type == SitesType.FREQUENTSITES) {
				if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					param.rightMargin = 41;
				} else {
					param.rightMargin = 23;
				}
			} else {
				param.setMargins(0, 10, 15, 10);
				param.width = FIXEDWIDTH - 5;
			}
			param.columnSpec = GridLayout.spec(c);
			param.rowSpec = GridLayout.spec(r);
			mGridLayout.addView(mMySiteItems.get(j), param);
		}
		mGridLayout.requestLayout();
	}

	/**
	 * 设置背景资源
	 * 
	 * @param resid
	 */
	public void setLayoutBackgroundResource(int resid) {
		mLinearLayout.setBackgroundResource(resid);
	}

	/**
	 * 设置模式
	 * 
	 * @param mode
	 */
	public void setMode(int mode) {
		this.mode = mode;

		for (int i = 0; i < mGridLayout.getChildCount(); i++) {
			MySiteItem siteItem = (MySiteItem) mGridLayout.getChildAt(i);
			siteItem.setMode(mode);
		}
	}

	/**
	 * 我要推荐
	 */
	private void setOnClickListener() {
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dialog == null) {
					dialog = new RecommandDialog(mContext,
							R.style.RecommandDialog);
				}

				if (!dialog.isShowing()) {
					dialog.show();
				}
			}
		});
	}

	/**
	 * 设置网址导航监听
	 * 
	 * @param onSiteNavigationListener
	 * 
	 */
	public void setOnSiteNavigationListener(
			OnSiteNavigationListener onSiteNavigationListener) {
		this.onSiteNavigationListener = onSiteNavigationListener;
	}

	/**
	 * 设置网址移除监听
	 * 
	 * @param onSiteRemoveListener
	 * 
	 */
	public void setOnSiteRemoveListener(
			OnSiteRemoveListener onSiteRemoveListener) {
		this.onSiteRemoveListener = onSiteRemoveListener;
	}

	/**
	 * 设置折分线背景资源
	 * 
	 * @param resid
	 * 
	 */
	public void setSplitlineBackgroundResource(int resid) {
		mView.setBackgroundResource(resid);
	}

	/**
	 * 设置颜色
	 * 
	 * @param color
	 * 
	 */
	public void setTextColor(int color) {
		mTextView.setTextColor(color);
	}

	/**
	 * 设置控件标题 (经常访问、我的网站、新网推荐)
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		mTextView.setText(text);
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
}
