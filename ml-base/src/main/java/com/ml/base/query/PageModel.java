package com.ml.base.query;

import java.util.List;

/**
 * <pre>
 * 分页对象
 * 
 * </pre>
 * 
 * @author mecarlen.wang 2018年5月12日 下午8:20:51
 */
public class PageModel<T> {
	private int size = 20;
	private int total = 0;
	private int currPage = 1;
	private List<T> data;

	public PageModel() {
		
	}
	
	public PageModel(List<T> data) {
		this.data = data;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	/**
	 * <pre>
	 * 获取总业数
	 * 
	 * </pre>
	 * 
	 * @return Integer
	 */
	public int getPageTotal() {
		int pageTotal = 0;
		if (total > 0 && size > 0) {
			pageTotal = total / size;
			if (total % size != 0) {
				pageTotal++;
			}
		}
		return pageTotal;
	}

	/**
	 * <pre>
	 * 下一页
	 * 
	 * </pre>
	 * 
	 * @return int
	 */
	public int next() {
		int nextPage = currPage + 1;
		if (nextPage > lastPage()) {
			return currPage;
		}
		return nextPage;
	}

	/**
	 * <pre>
	 * 上一页
	 * 
	 * </pre>
	 * 
	 * @param int
	 */
	public int pre() {
		int prePage = currPage - 1;
		if (prePage <= 0) {
			return currPage;
		}
		return prePage;
	}

	/**
	 * <pre>
	 * 第一页
	 * 
	 * </pre>
	 */
	public int firstPage() {

		return total > 0 ? 1 : 0;
	}

	/**
	 * <pre>
	 * 最后一页
	 * 
	 * </pre>
	 */
	public int lastPage() {
		int endPageNo = total / size;
		if (total / size != 0) {
			endPageNo++;
		}
		return endPageNo;
	}

	/**
	 * <pre>
	 * 判断是否为空页
	 * 
	 * 描述
	 * data为空,total为0
	 * </pre>
	 */
	public boolean isEmpty() {
		if (null == data || data.isEmpty() || total == 0) {
			return true;
		}
		return false;
	}
}
