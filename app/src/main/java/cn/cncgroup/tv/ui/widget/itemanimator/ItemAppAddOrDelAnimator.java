package cn.cncgroup.tv.ui.widget.itemanimator;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * Created by Wu on 2015/10/26.
 */
public class ItemAppAddOrDelAnimator extends DefaultItemAnimator
{
	/*
	 * this ItemAnimator is work for App add or delete , params : if you set
	 * Animation to oldHolder,and effection is that old item will express the
	 * animation if you set animation ro newHolder,and effection is that new
	 * Item will express the animation.
	 */
	@Override
	public boolean animateChange(RecyclerView.ViewHolder oldHolder,
	        RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop,
	        int toLeft, int toTop)
	{
		boolean flag = super.animateChange(oldHolder, newHolder, fromLeft,
		        fromTop, toLeft, toTop);
		Animation oldItemScale = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
		        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		        0.5f);
		oldItemScale.setDuration(1000);
		Animation oldItemAlph = new AlphaAnimation(1.0f, 0.0f);
		oldItemAlph.setDuration(1000);
		AnimationSet oldItemAnimSet = new AnimationSet(true);
		oldItemAnimSet.addAnimation(oldItemScale);
		oldItemAnimSet.addAnimation(oldItemAlph);
		oldHolder.itemView.setAnimation(oldItemAnimSet);

		Animation newItemScale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
		        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		        0.5f);
		newItemScale.setDuration(1000);
		Animation newItemAlph = new AlphaAnimation(0.3f, 1.0f);
		newItemAlph.setDuration(1000);
		AnimationSet newItemAnimSet = new AnimationSet(true);
		newItemAnimSet.addAnimation(newItemScale);
		newItemAnimSet.addAnimation(newItemAlph);
		newHolder.itemView.setAnimation(newItemAnimSet);
		return flag;
	}
}
