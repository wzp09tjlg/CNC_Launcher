package cn.cncgroup.tv.network.request;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.cncgroup.tv.network.DiskCache;
import cn.cncgroup.tv.network.NetworkManager;
import cn.cncgroup.tv.network.utils.HttpUtil;
import cn.cncgroup.tv.utils.LogUtil;

/**
 * Created by zhang on 2015/4/21. Request基类，做一些通用化操作，主要对OkHttp的Callback做了一些封装
 */
public abstract class BaseRequest<T> implements Callback {
    /**
     * 连接超时错误
     */
    public static final int ERROR_SOCKET_TIME_OUT = 0;
    /**
     * 盒子连的网不通外网
     */
    public static final int ERROR_CONNECT = 1;
    /**
     * 盒子没联网
     */
    public static final int ERROR_ACTIVE_NETWORK = 2;
    /**
     * 读缓存出现问题
     */
    public static final int ERROR_CACHE = 3;
    /**
     * 其他错误
     */
    public static final int ERROR_OTHER = 4;
    /**
     * 访问地址
     */
    public String url;
    /**
     * 请求tag，用于取消访问
     */
    public Object tag;
    /**
     * 是否缓存数据
     */
    public boolean isCacheResult;
    /**
     * 要获取的结果
     */
    private T mResult;
    /**
     * UI线程结果回调
     */
    private Listener<T> mListener;
    /**
     * 是否已经重试
     */
    private boolean isTry;
    private RequestBody mRequestBody;
    private int mMethod = Method.GET;

    /**
     * 缓存构造请求
     */
    public BaseRequest(String url, Object tag, Listener listener,
                       boolean isCacheResult) {
        this.url = url;
        this.tag = tag;
        this.mListener = listener;
        this.isCacheResult = isCacheResult;
    }

    /**
     * OkHttp访问失败回调方法，运行在网络访问线程中
     */
    @Override
    public void onFailure(final Request request, final IOException e) {
        e.printStackTrace();
        final int errorCode;
        if (e instanceof SocketTimeoutException) {
            errorCode = ERROR_SOCKET_TIME_OUT;
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            errorCode = ERROR_CONNECT;
        } else if (e instanceof NoActiveNetworkException) {
            errorCode = ERROR_ACTIVE_NETWORK;
        } else if (e instanceof CacheException) {
            errorCode = ERROR_CACHE;
        } else {
            errorCode = ERROR_OTHER;
        }
        if (errorCode != ERROR_ACTIVE_NETWORK && errorCode != ERROR_CACHE) {
            if (!isTry) {
                isTry = true;
                LogUtil.i("framework try");
                performNetwork(mMethod, false, false);
            } else {
                NetworkManager.getInstance().UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onFailure(errorCode, request);
                    }
                });
            }
        } else {
            NetworkManager.getInstance().UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onFailure(errorCode, request);
                }
            });
        }
    }

    /**
     * OkHttp访问成功回掉方法，运行在网络访问线程中
     */
    @Override
    public void onResponse(final Response response) throws IOException {
        if (response != null) {
            byte[] bytes = response.body().bytes();
            mResult = parseNetworkResponse(bytes);
            if (mResult != null && isCacheResult) {
                DiskCache.Entry entry = new DiskCache.Entry();
                entry.data = bytes;
                NetworkManager.getInstance().diskCache.put(url, entry);
            }
        }
        final T result = mResult;
        NetworkManager.getInstance().UIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mResult != null) {
                    mListener.onResponse(result, response == null);//response == null:结果来自缓存
                } else {
                    mListener.onFailure(ERROR_CONNECT, response == null ? null : response.request());
                }
            }
        });
    }

    public void onCacheResponse(byte[] data) {
        mResult = parseNetworkResponse(data);
        try {
            onResponse(null);
        } catch (IOException e) {
            e.printStackTrace();
            onCacheFailure();
        }
    }

    public void onCacheFailure() {
        onFailure(null, new CacheException("缓存读取错误"));
    }

    /**
     * 子类根据各自功能，重写此方法解析response
     */
    abstract protected T parseNetworkResponse(byte[] data);

    public BaseRequest<T> setRequestBody(RequestBody body) {
        this.mRequestBody = body;
        return this;
    }

    public void performNetwork(int method, boolean isUseCache,
                               boolean isOnlyUseCache) {
        this.mMethod = method;
        switch (mMethod) {
            case Method.GET:
                dispatchGetRequest(this, isUseCache, isOnlyUseCache);
                break;
            case Method.POST:
                HttpUtil.post(this, mRequestBody);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public void performNetwork(int method) {
        performNetwork(method, false, false);
    }

    private void dispatchGetRequest(BaseRequest request, boolean isUseCache,
                                    boolean isOnlyUseCache) {
        if (isUseCache) {
            if (isOnlyUseCache) {
                HttpUtil.getOnlyByCache(request);
            } else {
                HttpUtil.getFirstByCache(request);
            }
        } else {
            HttpUtil.get(request);
        }
    }

    /**
     * 最终结果回掉，在UI线程中运行
     */
    public interface Listener<T> {
        void onResponse(T result, boolean isFromCache);
        void onFailure(int errorCode, Request response);
    }

    /**
     * 访问方式
     */
    public interface Method {
        int GET = 0;
        int POST = 1;
    }

    /**
     * 没有可用网络错误
     */
    public static class NoActiveNetworkException extends IOException {
        public NoActiveNetworkException(String msg) {
            super(msg);
        }
    }

    /**
     * 缓存读取错误
     */
    public static class CacheException extends IOException {
        public CacheException(String msg) {
            super(msg);
        }
    }

}
