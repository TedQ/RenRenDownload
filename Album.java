public class Album {
  private String albumName;// 相册名
	private String albumCover;// 相册封面地址
	private String url;// 相册地址
	private String num;// 相册内相片数量
	private boolean lock;// 是否加密
	public Album(String albumCover, String num, String url, boolean lock,
			String albumName) {
		this.albumCover = albumCover;
		this.num = num;
		this.url = url;
		this.lock = lock;
		this.albumName = albumName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((albumName == null) ? 0 : albumName.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	public String getAlbumName() {
		return albumName;
	}
	public String getAlbumCover() {
		return albumCover;
	}
	public String getUrl() {
		return url;
	}
	public String getNum() {
		return num;
	}
	public boolean isLock() {
		return lock;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (albumName == null) {
			if (other.albumName != null)
				return false;
		} else if (!albumName.equals(other.albumName))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
