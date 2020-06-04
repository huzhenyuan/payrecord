package z.p.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Data {

    @JSONField(name = "total")
    private int total;

    @JSONField(name = "size")
    private int size;

    @JSONField(name = "totalPage")
    private int totalPage;

    @JSONField(name = "pageSize")
    private int pageSize;

    @JSONField(name = "pageNum")
    private int pageNum;

    @JSONField(name = "content")
    private List<ContentItem> content;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public List<ContentItem> getContent() {
        return content;
    }

    public void setContent(List<ContentItem> content) {
        this.content = content;
    }

    @Override
    public String toString() {
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