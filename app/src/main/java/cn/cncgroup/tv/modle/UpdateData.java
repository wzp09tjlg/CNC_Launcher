package cn.cncgroup.tv.modle;

import java.io.Serializable;

/**
 * Created by zhang on 2015/6/4.
 */
public class UpdateData implements Serializable{
//			{"version":"2.5.1",
//			"versioncode":"110",
//			"updateinfo":"测试",
//			"filesize":15238231,
//			"forceupdate":"false",
//			"url":"http://192.168.1.200:8080/upload/app-channel-debug.apk",
//			"ret":1}
    private String releasedata;
    private String version;
    private int  versioncode;
    private String updateinfo;
    private long filesize;
    private boolean forceupdate;
    private String url;
    private int ret;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public String getReleasedata() {
        return releasedata;
    }

    public void setReleasedata(String releasedata) {
        this.releasedata = releasedata;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public String getUpdateinfo() {
        return updateinfo;
    }

    public void setUpdateinfo(String updateinfo) {
        this.updateinfo = updateinfo;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public boolean isForceupdate() {
        return forceupdate;
    }

    public void setForceupdate(boolean forceupdate) {
        this.forceupdate = forceupdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }
}
