package com.ml.base.query;

/**
 * <pre>
 * 分页查询默认参数
 * 
 * </pre>
 * 
 * @author mecarlen 2018年9月30日 下午4:06:12
 */
public class PageParams<T> {
	private int pageNo = 1;
	private int pageSize = 20;
	private Integer shardIdx;
	private Integer shards;
	private T params;

	public int getStartRow() {
		return pageNo <= 0 ? 0 : (pageNo - 1) * pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getShardIdx() {
		return shardIdx;
	}

	public void setShardIdx(Integer shardIdx) {
		this.shardIdx = shardIdx;
	}

	public Integer getShards() {
		return shards;
	}

	public void setShards(Integer shards) {
		this.shards = shards;
	}

	public T getParams() {
		return params;
	}

	public void setParams(T params) {
		this.params = params;
	}

}
