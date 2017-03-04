package myCustomView;

import com.uniqueweather.app.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
public class CircleImageView extends ImageView {
  private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;  //按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽) 
  private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;   //ARGB_8888就是由4个8位组成即32位,ARGB_8888——代表32位ARGB位图
  private static final int COLORDRAWABLE_DIMENSION = 2;
  private static final int DEFAULT_BORDER_WIDTH = 0;
  private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
  private final RectF mDrawableRect = new RectF();
  private final RectF mBorderRect = new RectF();
  private final Matrix mShaderMatrix = new Matrix();   //矩阵
  private final Paint mBitmapPaint = new Paint();    
  private final Paint mBorderPaint = new Paint();
  private int mBorderColor = DEFAULT_BORDER_COLOR; //边界为黑色
  private int mBorderWidth = DEFAULT_BORDER_WIDTH;   
  private Bitmap mBitmap;
  private BitmapShader mBitmapShader;   //调用这个方法来产生一个画有一个位图的渲染器（Shader）
  private int mBitmapWidth;
  private int mBitmapHeight;
  private float mDrawableRadius;
  private float mBorderRadius;
  private boolean mReady;
  private boolean mSetupPending;
  public CircleImageView(Context context) {
   super(context);
   init();
  }
  public CircleImageView(Context context, AttributeSet attrs) {
   this(context, attrs, 0);
  }
  public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
   super(context, attrs, defStyle);
   TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
   mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
   mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
   a.recycle();
   init();
  }
  private void init() {
   super.setScaleType(SCALE_TYPE);  //设置按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽) 
   mReady = true;    
   if (mSetupPending) {     //刚开始默认为true 
     setup();
     mSetupPending = false;
   }
  }
  @Override
  public ScaleType getScaleType() {
   return SCALE_TYPE;
  }
  @Override
  public void setScaleType(ScaleType scaleType) {
   if (scaleType != SCALE_TYPE) {
     throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
   }
  }
  @Override
  public void setAdjustViewBounds(boolean adjustViewBounds) {
   if (adjustViewBounds) {
     throw new IllegalArgumentException("adjustViewBounds not supported.");
   }
  }
  @Override
  protected void onDraw(Canvas canvas) {
   if (getDrawable() == null) {
     return;
   }
   canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
   if (mBorderWidth != 0) {
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
   }
  }
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
   super.onSizeChanged(w, h, oldw, oldh);
   setup();
  }
  public int getBorderColor() {
   return mBorderColor;
  }
  public void setBorderColor(int borderColor) {
   if (borderColor == mBorderColor) {
     return;
   }
   mBorderColor = borderColor;
   mBorderPaint.setColor(mBorderColor);
   invalidate();
  }
  public int getBorderWidth() {
   return mBorderWidth;
  }
  public void setBorderWidth(int borderWidth) {
   if (borderWidth == mBorderWidth) {
     return;
   }
   mBorderWidth = borderWidth;
   setup();
  }
  @Override
  public void setImageBitmap(Bitmap bm) {
   super.setImageBitmap(bm);
   mBitmap = bm;
   setup();
  }
  @Override
  public void setImageDrawable(Drawable drawable) {
   super.setImageDrawable(drawable);
   mBitmap = getBitmapFromDrawable(drawable);
   setup();
  }
  @Override
  public void setImageResource(int resId) {
   super.setImageResource(resId);
   mBitmap = getBitmapFromDrawable(getDrawable());
   setup();
  }
  @Override
  public void setImageURI(Uri uri) {
   super.setImageURI(uri);
   mBitmap = getBitmapFromDrawable(getDrawable());
   setup();
  }
  private Bitmap getBitmapFromDrawable(Drawable drawable) {
   if (drawable == null) {
     return null;
   }
   if (drawable instanceof BitmapDrawable) {
     return ((BitmapDrawable) drawable).getBitmap();
   }
   try {
     Bitmap bitmap;
     if (drawable instanceof ColorDrawable) {
      bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
     } else {
      bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
     }
     Canvas canvas = new Canvas(bitmap);
     drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
     drawable.draw(canvas);
     return bitmap;
   } catch (OutOfMemoryError e) {
     return null;
   }
  }
  private void setup() {
   if (!mReady) {       //这个时候mReady为true,反过来为false
     mSetupPending = true;
     return;
   }
   if (mBitmap == null) {
     return;
   }
   mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);//CLAMP的意思就是边缘拉伸的意思
   mBitmapPaint.setAntiAlias(true);  //抗锯齿功能
   mBitmapPaint.setShader(mBitmapShader);   
   mBorderPaint.setStyle(Paint.Style.STROKE);  //设置画笔空心，也就是中间为空，这里是边界画笔
   mBorderPaint.setAntiAlias(true);    
   mBorderPaint.setColor(mBorderColor);   
   mBorderPaint.setStrokeWidth(mBorderWidth);
   mBitmapHeight = mBitmap.getHeight();   
   mBitmapWidth = mBitmap.getWidth();
   mBorderRect.set(0, 0, getWidth(), getHeight());//这里设置边界矩形的具体值,后面getWidth()表示得到该imageview的width(单位px)   
   mBorderRadius = Math.min((mBorderRect.height() -2* mBorderWidth) / 2, (mBorderRect.width() -2* mBorderWidth) / 2);
   mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
   mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);
   updateShaderMatrix();
   invalidate();
  }
  private void updateShaderMatrix() {
   float scale;
   float dx = 0;
   float dy = 0;
   mShaderMatrix.set(null);
   if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
     scale = mDrawableRect.height() / (float) mBitmapHeight;
     dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
   } else {
     scale = mDrawableRect.width() / (float) mBitmapWidth;
     dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
   }
   mShaderMatrix.setScale(scale, scale);
   mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
   mBitmapShader.setLocalMatrix(mShaderMatrix);
  }
}
