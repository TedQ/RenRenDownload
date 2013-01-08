public class AlbumDetail {
  private String name;//相片名
	private String shortUrl;//相片缩略图url
	private String largeUrl;//相片完整图url
	private String ord;//相片保存时可能的后续编号
	public AlbumDetail(String name,String shortUrl,String largeUrl,String ord){
		this.name=name;
		this.shortUrl=shortUrl;
		this.largeUrl=largeUrl;
		this.ord=ord;
	}
	public String getOrd() {
		return ord;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((largeUrl == null) ? 0 : largeUrl.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((shortUrl == null) ? 0 : shortUrl.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlbumDetail other = (AlbumDetail) obj;
		if (largeUrl == null) {
			if (other.largeUrl != null)
				return false;
		} else if (!largeUrl.equals(other.largeUrl))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (shortUrl == null) {
			if (other.shortUrl != null)
				return false;
		} else if (!shortUrl.equals(other.shortUrl))
			return false;
		return true;
	}
	public String getName() {
		return name;
	}
	public String getShortUrl() {
		return shortUrl;
	}
	public String getLargeUrl() {
		return largeUrl;
	}
}
