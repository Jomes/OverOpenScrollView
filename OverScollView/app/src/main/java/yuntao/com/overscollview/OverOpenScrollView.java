package yuntao.com.overscollview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by jomeslu
 * QQ:58260504
 * github:https://github.com/Jomes
 */
public class OverOpenScrollView extends ScrollView {
    private View headView;
    private Scroller scroller;
    private Context context;
    private float currentY;
    private float startY;
    private int mViewHeight;
    private static int TITLE_HEIGHT = 60;
    private boolean couldScroll;
    private boolean isScrolling;
    private boolean isHeadOpening = false;
    private HeadStateListenter listener;
    private int statusBarHeight;
    private int startHeight;
    private int endHeight;
    private int titleHeight;
    private final int ANIMATIONTIME = 600;

    public OverOpenScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public OverOpenScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public OverOpenScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void setListener(HeadStateListenter listener) {
        this.listener = listener;
    }

    /**
     * 状态栏的高度
     *
     * @param activity
     * @return
     */
    public int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 =
                        Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
                                .toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    @SuppressWarnings("deprecation")
    public void setHeadImage(int resId) {
        headView = (View) ((Activity) context).findViewById(resId);
        scroller = new Scroller(context);
//        startHeight = context.getResources().getDimensionPixelSize(R.dimen.detail_image_height);
//        titleHeight = context.getResources().getDimensionPixelSize(R.dimen.title_view_height);
        statusBarHeight = getStatusHeight((Activity) context);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        endHeight =
                wm.getDefaultDisplay().getHeight() - statusBarHeight
                        - titleHeight;
//        endHeight =
//                wm.getDefaultDisplay().getHeight()
//                - context.getResources().getDimensionPixelSize(
//                        R.dimen.build_detail_bottom_area_height) - statusBarHeight
//                        - titleHeight;
    }
    public void setStateChanger() {
        if (!isHeadOpening) {
            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                    endHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
        } else {
            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                    startHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
        }
        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        currentY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = currentY;
                mViewHeight = headView.getBottom() - headView.getTop();
                couldScroll = (getScrollY() == 0 || isHeadOpening);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!couldScroll)
                    break;
                currentY = ev.getY();
                if ((couldScroll && currentY - startY > 60 && !isHeadOpening)
                        || (couldScroll && isHeadOpening && startY - currentY > 60)) {
                    isScrolling = true;
                    int targetY = (int) (mViewHeight + ((currentY - startY)) / 1.5F);
                    RelativeLayout.LayoutParams lp =
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT, targetY);
                    headView.setLayoutParams(lp);
                    invalidate();
                    if (listener != null) {
                        listener.headTouch();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                currentY = ev.getY();
                if (couldScroll && isScrolling) {
                    if (!isHeadOpening) {
                        if ((currentY - startY > TITLE_HEIGHT)) {
                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                                    endHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
                        } else {
                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                                    startHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
                        }
                    } else {
                        if (startY - currentY > TITLE_HEIGHT) {
                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                                    startHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
                        } else {
                            scroller.startScroll(headView.getLeft(), headView.getBottom(), 0,
                                    endHeight - (headView.getBottom() - headView.getTop()), ANIMATIONTIME);
                        }
                    }
                }
                invalidate();
                break;
            default:
                break;
        }
        if (isScrolling) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            RelativeLayout.LayoutParams lp =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, y);
            headView.setLayoutParams(lp);
            headView.requestLayout();
            invalidate();
            if (y == endHeight) {
                couldScroll = false;
                isHeadOpening = true;
                scroller.abortAnimation();
                isScrolling = false;
                if (listener != null) {
                    listener.headOpen();
                }
            } else if (y == startHeight) {
                couldScroll = false;
                isHeadOpening = false;
                isScrolling = false;
                scroller.abortAnimation();
                if (listener != null) {
                    listener.headClosed();
                }
            }

        }
    }

    public interface HeadStateListenter {
        void headOpen();

        void headClosed();

        void headTouch();
    }
}
