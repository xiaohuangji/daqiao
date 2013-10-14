package com.wills.clientproxy;

/**
 * @author huangsiping
 *
 */
public class ClusterServiceRegistry {
	private String clusterServiceId = "";

	public String getClusterServiceIdId() {
		return clusterServiceId;
	}

	public ClusterServiceRegistry(String clusterId) {
		super();
		this.clusterServiceId = clusterId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((clusterServiceId == null) ? 0 : clusterServiceId.hashCode());
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
		ClusterServiceRegistry other = (ClusterServiceRegistry) obj;
		if (clusterServiceId == null) {
			if (other.clusterServiceId != null)
				return false;
		} else if (!clusterServiceId.equals(other.clusterServiceId))
			return false;
		return true;
	}
	
	
	 
	
}
