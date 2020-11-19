package softwork.pojo.entities;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name = "contest")
@Document(indexName = "contest",type = "docs", shards = 1, replicas = 0)
public class Contest{

    @Id
    private Integer id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String originator;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Keyword)
    private String level;

    @Field(store = true,type = FieldType.Text)
    private String info;

    @Field(index = false,type = FieldType.Date)
    private Timestamp enrollStart;

    @Field(index = false,type = FieldType.Date)
    private Timestamp enrollEnd;

    @Field(index = false,type = FieldType.Date)
    private Timestamp contestStart;

    @Field(index = false,type = FieldType.Date)
    private Timestamp contestEnd;

    @Field(index = false, type = FieldType.Keyword)
    private String picUrl;

}
