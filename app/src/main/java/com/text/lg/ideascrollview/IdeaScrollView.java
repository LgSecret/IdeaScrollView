package com.text.lg.ideascrollview;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ScrollView;

import java.util.ArrayList;

public class IdeaScrollView extends ScrollView {

    private final Point point;
    private IdeaViewPager viewPager;

    private int position = 0;

    ArrayList<Integer> arrayDistance = new ArrayList<>();
    private int headerHeight;

    public IdeaScrollView(Context context) {
        this(context,null,0);
    }

    public IdeaScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IdeaScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (viewPager != null && t != oldt) {
            viewPager.setTranslationY(t/2);
        }

        if(viewPager!=null&&t<=point.x-headerHeight&&getOnScrollChangedColorListener()!=null){

            getOnScrollChangedColorListener().onChanged(Math.abs(t)/Float.valueOf(point.x-headerHeight));
            if(t<=(point.x-headerHeight)/2){
                getOnScrollChangedColorListener().onChangedFirstColor(t/(point.x-headerHeight)/2);
            }else{
                getOnScrollChangedColorListener().onChangedSecondColor((t-(point.x-headerHeight)/2)/(point.x-headerHeight)/2);
            }

        }

        int currentPosition = getCurrentPosition(t,arrayDistance);
        if(currentPosition!=position&&getOnSelectedIndicateChangedListener()!=null){
            getOnSelectedIndicateChangedListener().onSelectedChanged(currentPosition);
        }
        this.position = currentPosition;
    }

    private int getCurrentPosition(int t, ArrayList<Integer> arrayDistance) {

        int index = 0;
        for (int i=0;i<arrayDistance.size();i++){
            if(i==arrayDistance.size()-1){
                index = i;
            }else {
                if(t>=arrayDistance.get(i)&&t<arrayDistance.get(i+1)){
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private void scrollToPosition() {
        scrollToPosition(position);
    }

    private void scrollToPosition(int position){
        scrollTo(0,arrayDistance.get(position));
    }

    public void setViewPager(IdeaViewPager viewPager,int headerHeight){
        this.viewPager = viewPager;
        this.headerHeight = headerHeight;
    }

    public interface OnScrollChangedColorListener{

        void onChanged(float percentage);

        void onChangedFirstColor(float percentage);

        void onChangedSecondColor(float percentage);

    }

    public interface OnSelectedIndicateChangedListener{

        void onSelectedChanged(int position);
    }

    private OnSelectedIndicateChangedListener onSelectedIndicateChangedListener;

    private OnScrollChangedColorListener onScrollChangedColorListener;

    public OnScrollChangedColorListener getOnScrollChangedColorListener() {
        return onScrollChangedColorListener;
    }

    public void setOnScrollChangedColorListener(OnScrollChangedColorListener onScrollChangedColorListener) {
        this.onScrollChangedColorListener = onScrollChangedColorListener;
    }

    public IdeaViewPager getViewPager() {
        return viewPager;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        scrollToPosition();
    }

    public ArrayList<Integer> getArrayDistance() {
        return arrayDistance;
    }

    public void setArrayDistance(ArrayList<Integer> arrayDistance) {
        this.arrayDistance = arrayDistance;
    }

    public OnSelectedIndicateChangedListener getOnSelectedIndicateChangedListener() {
        return onSelectedIndicateChangedListener;
    }

    public void setOnSelectedIndicateChangedListener(OnSelectedIndicateChangedListener onSelectedIndicateChangedListener) {
        this.onSelectedIndicateChangedListener = onSelectedIndicateChangedListener;
    }
}
