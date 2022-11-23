package default_package;

import java.util.List;

public class Resource {
	
	String resourceNameString;
	List<Peer> store_resourceList;
	String resourceGUIDString;
	
	public Resource(String name,List l) {
		store_resourceList = l;
		resourceNameString = name;
		resourceGUIDString = HashKit.sha1(name);
	}
	
	public Resource(String name) {
		resourceGUIDString = HashKit.sha1(name);
		resourceNameString = name;
	}
	
	public List<Peer> getHolder() {
		return store_resourceList;
	}
	
	public String getName() {
		return resourceNameString;
	}
	
	public String getGUID() {
		return resourceGUIDString;
		
	}
	
	public void addMyList(Peer p) {
		store_resourceList.add(p);
	}
	
	public void removeMyList(Peer p) {
		store_resourceList.remove(p);
	}
	

}
