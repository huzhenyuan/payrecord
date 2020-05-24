package xyz.loadnl.payrecord.model;

import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;

public class Data{

	@JSONField(name="total")
	private int total;

	@JSONField(name="size")
	private int size;

	@JSONField(name="totalPage")
	private int totalPage;

	@JSONField(name="pageSize")
	private int pageSize;

	@JSONField(name="pageNum")
	private int pageNum;

	@JSONField(name="content")
	private List<ContentItem> content;

	public int getTotal(){
		return total;
	}

	public int getSize(){
		return size;
	}

	public int getTotalPage(){
		return totalPage;
	}

	public int getPageSize(){
		return pageSize;
	}

	public int getPageNum(){
		return pageNum;
	}

	public List<ContentItem> getContent(){
		return content;
	}
}