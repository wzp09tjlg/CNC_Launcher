package cn.cncgroup.tv.player.data;

/**
 * Created by JIF on 2015/6/30.
 */
public class VideoPlayInfo {
	private String url;
	private int headTime;
	private int tailTime;
	public VideoPlayInfo(String url, int headTime, int tailTime) {
		this.url = url;
		this.headTime = headTime;
		this.tailTime = tailTime;
	}

	public String getUrl() {
		return url;
	}

	public int getHeadTime() {
		return headTime;
	}

	public int getTailTime() {
		return tailTime;
	}

	@Override
	public String toString() {
		return "url = " + url +";headTime = " + headTime + ";tailTime=" + tailTime;
	}
}
