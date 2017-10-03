package cn.cncgroup.tv.cncplayer.eventbus;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/9/1.
 */
public class PlayerEventBus {
    private static volatile PlayerEventBus defaultInstance;
    private IPlayerEvent mPlayerEvent;
    private FragmentVideoListener mListener;

    public static PlayerEventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (PlayerEventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new PlayerEventBus();
                }
            }
        }
        return defaultInstance;
    }

    public void setOnFragmentVideoListner(FragmentVideoListener listner){
        mListener = listner;
    }


    public void register(IPlayerEvent playerEvent) {
        mPlayerEvent = playerEvent;
        register();
    }

    public void unRegister() {
        EventBus.getDefault().unregister(PlayerEventBus.this);
        defaultInstance = null;
    }

    public void register() {
        try {
            EventBus.getDefault().register(PlayerEventBus.this);
        } catch (Exception e) {
        }
    }

    public void setOnPlayerEventListener(IPlayerEvent listener) {
        mPlayerEvent = listener;
    }

    /**
     * 点击选集集数Item调用
     */
    public void onEventMainThread(VideoClickEvent event) {
        mPlayerEvent.onVideoItemClick(event);
        if(mListener!=null)
            mListener.onVideoItemClick(event);
    }

    /**
     * 选集控件Item获取焦点时调用
     * @param event
     */
    public void onEventMainThread(FocuseEvent event) {
        mPlayerEvent.onVideoItemFocuseChange(event);
    }


    public void onEventMainThread(PlayOverEvent event) {
        mPlayerEvent.onVideoItemPlayOver(event);
        if(mListener!=null){
            mListener.onVideoPlayeOver(event);
        }
    }

    /**
     * 点击清晰度调用
     */
    public void onEventMainThread(DefinitionEvent event) {
        mPlayerEvent.onDefinitionClick(event);
    }

    public interface IPlayerEvent {
        void onDefinitionClick(DefinitionEvent event);

        void onVideoItemClick(VideoClickEvent event);

        void onVideoItemFocuseChange(FocuseEvent event);

        void onVideoItemPlayOver(PlayOverEvent event);
    }

    public interface FragmentVideoListener{
        void onVideoPlayeOver(PlayOverEvent event);
        void onVideoItemClick(VideoClickEvent event);
    }

}
