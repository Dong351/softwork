package softwork.pojo.entities;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "certificate")
@Document(indexName = "certificate")
public class Certificate {
    @Id
    private Integer id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String name;

    @Field(store = true,type = FieldType.Text,analyzer = "ik_max_word")
    private String info;

    @Field(index = false,type = FieldType.Text)
    private String office_web;

    @Field(index = false,type = FieldType.Date)
    private Date enroll_start;

    @Field(index = false,type = FieldType.Date)
    private Date enroll_end;

    @Field(index = false,type = FieldType.Date)
    private Date contest_start;

    @Field(index = false,type = FieldType.Date)
    private Date contest_end;

    @Field(index = false, type = FieldType.Keyword)
    private Integer collected;

    @Field(index = false, type = FieldType.Keyword)
    private Integer watched;
}
