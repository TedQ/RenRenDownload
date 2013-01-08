public class Blog {
  private String blogName;
	private String url;
	private String body;
	public Blog(String blogName,String url,String body){
		this.blogName=blogName;
		this.url=url;
		this.body=body;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((blogName == null) ? 0 : blogName.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Blog other = (Blog) obj;
		if (blogName == null) {
			if (other.blogName != null)
				return false;
		} else if (!blogName.equals(other.blogName))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	public String getBlogName() {
		return blogName;
	}
	public String getUrl() {
		return url;
	}
	public String getBody() {
		return body;
	}
}
