package z.p.model;

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

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getSize(){
		return size;
	}

	public void setTotalPage(int totalPage){
		this.totalPage = totalPage;
	}

	public int getTotalPage(){
		return totalPage;
	}

	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	public int getPageSize(){
		return pageSize;
	}

	public void setPageNum(int pageNum){
		this.pageNum = pageNum;
	}

	public int getPageNum(){
		return pageNum;
	}

	public void setContent(List<ContentItem> content){
		this.content = content;
	}

	public List<ContentItem> getContent(){
		return content;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"total = '" + total + '\'' + 
			",size = '" + size + '\'' + 
			",totalPage = '" + totalPage + '\'' + 
			",pageSize = '" + pageSize + '\'' + 
			",pageNum = '" + pageNum + '\'' + 
			",content = '" + content + '\'' + 
			"}";
		}
}