package softwork.pojo.dto;

import lombok.Data;
import org.apache.ibatis.session.RowBounds;

@Data
public class PageDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String orderBy = "id DESC";
    private String sortBy;

    public RowBounds toRow(){
        return new RowBounds(pageSize * (pageNum - 1), pageSize);
    }
}
