public class Friend {
  private String name;
	private String id;
	private String image;
	public Friend(String name,String id,String image){
		this.name=name;
		this.id=id;
		this.image=image;
	}
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public String getImage() {
		return image;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Friend other = (Friend) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
