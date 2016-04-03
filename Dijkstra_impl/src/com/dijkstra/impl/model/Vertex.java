package com.dijkstra.impl.model;

public class Vertex implements Comparable<Vertex> {
	/**
	 * �ڵ�����(A,B,C,D)
	 */
	private String name;
	/**
	 * ���·������
	 */
	private int path;
	/**
	 * �ڵ��Ƿ��Ѿ�����(�Ƿ��Ѿ��������)
	 */
	private boolean isMarked;

	public Vertex(String name) {
		this.name = name;
		this.path = Integer.MAX_VALUE; // ��ʼ����Ϊ�����
		this.setMarked(false);
	}

	public Vertex(String name, int path) {
		this.name = name;
		this.path = path;
		this.setMarked(false);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPath() {
		return path;
	}

	public void setPath(int path) {
		this.path = path;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isMarked ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + path;
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
		Vertex other = (Vertex) obj;
		if (isMarked != other.isMarked)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path != other.path)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vertex [name=" + name + ", path=" + path + ", isMarked="
				+ isMarked + "]";
	}

	@Override
	public int compareTo(Vertex o) {
		return o.path > path ? -1 : 1;
	}

}
