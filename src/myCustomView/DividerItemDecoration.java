package myCustomView;

import android.R.anim;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

public class DividerItemDecoration extends ItemDecoration {
	private Context mContext;
    private Drawable mDivider;
    private int mOrientation;
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    //����ͨ����ȡϵͳ�����е�listDivider����ӣ���ϵͳ�е�AppTheme������
    public static final int[] ATRRS  = new int[]{
            android.R.attr.listDivider
    };

    public DividerItemDecoration(Context context, int orientation) {
        this.mContext = context;
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
        setOrientation(orientation);
    }

    //������Ļ�ķ���
    public void setOrientation(int orientation){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");        }        mOrientation = orientation;
    } 

   @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST){
            drawVerticalLine(c, parent, state);
        }else {
            drawHorizontalLine(c, parent, state);
        }
    }

    //������, �����parent��ʵ����ʾ����Ļ��ʾ���ⲿ��
    public void drawHorizontalLine(Canvas c, RecyclerView parent, RecyclerView.State state){
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i);

            //���child�Ĳ�����Ϣ
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            //Log.d("wnw", left + " " + top + " "+right+"   "+bottom+" "+i);
        }
    }

    //������
    public void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state){
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i); 

           //���child�Ĳ�����Ϣ
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //����DividerҲ�г���ߣ�ÿһ��Item��Ҫ���»�������ƫ��
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == HORIZONTAL_LIST){
            //�����ߣ���������ƫ��һ���ָ��ߵĸ߶�
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }else {
            //�����ߣ���������ƫ��һ���ָ��ߵĿ��
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}


      

